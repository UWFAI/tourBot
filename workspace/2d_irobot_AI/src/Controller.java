public class Controller {
	
	Communication com;
	create2_environment.Controller sim_con = null;
	
	IRobot bot;
	volatile Quadtree quadtree;
	TabletComm tab;
	volatile Kinect kinect;
	
	boolean run = true;
	
	// some helper variables
	int tree_root_size = 2000; // this might be in mm, if it is it needs to be much bigger
	double bot_x = 0;//tree_root_size/4;
	double bot_y = 0;//tree_root_size/2;
	double bot_direction = 0;
	double bot_distance = 0;
	
	DebugWindow window;
	
	public Controller() {
		window = new DebugWindow(this);
		quadtree = new Quadtree(this, 0, 0, 5000, 5000);// this size is in cm
		
		com = new Communication(this);
		
		bot = new IRobot(com);
		
		kinect = new Kinect(this);
		
		window.scrollPane_Tree.getVerticalScrollBar().setValue(2500-200);
		window.scrollPane_Tree.getHorizontalScrollBar().setValue(2500-200);

		tab = new TabletComm(this);
		
		new Thread( new Runnable() {
            public void run() {
            	while(true) {
            		keep_tree_updated();
            	}
            }
        }).start();
		
		while (run) AI();
	}
	
	public void keep_tree_updated() {
		int tree_bot_x = (int)bot_x+2500;
		int tree_bot_y = (int)bot_y+2500;
		
		quadtree.set_circle( tree_bot_x, tree_bot_y, 12, "free");
		
		float[][] distance = kinect.distance_2d;
		for(int i=0; i<500; i++) { 
			if (distance[i][1] == 0)	
				continue;
			
			int end_x = 0;
			int end_y = 0;
			int hit_end_x = 0;
			int hit_end_y = 0;
			
			double ang = distance[i][0];
			double dis = distance[i][1];
			//kinect.distance_2d[i][0] = 0;
			//kinect.distance_2d[i][1] = 0;
			
			double min_dis = 50;

			int start_x = tree_bot_x;
			
			int start_y = tree_bot_y;
			
			end_x = (int) (start_x + lengthdir_x(dis, bot_direction) + lengthdir_x(ang, -90));//
			end_y = (int) (start_y + lengthdir_y(dis, bot_direction) + lengthdir_y(ang, -90));//
			//quadtree.set_circle(end_x, end_y, 5, "hit");
			
			
			quadtree.set_line(start_x, start_y, end_x, end_y, "free");
			//hit_end_x = (int) (end_x+lengthdir_x(400, bot_direction-152/2+i));
			//hit_end_y = (int) (end_y+lengthdir_y(400, bot_direction-152/2+i));
			quadtree.set_circle(end_x, end_y, 2, "hit");
			//quadtree.set_line(end_x, end_y, hit_end_x, hit_end_y, "hit");
			
		}
		
		quadtree.draw_bot_x = tree_bot_x;
		quadtree.draw_bot_y = tree_bot_y;
		
		update_bot_point();
	}
	
	public void update_bot_point() {
		
		if (bot.update_ang && bot.update_dis) {
			
			double dis_dif = update_bot_distance();
			double dir_dif = update_bot_direction();		
			
			bot_x += Math.cos(Math.toRadians(bot_direction))*dis_dif;
			bot_y += -Math.sin(Math.toRadians(bot_direction))*dis_dif;
			
			bot.update_ang = false;
			bot.update_dis = false;
		}
	}
	
	public int update_bot_direction(){
		double out = bot.getDirection();
		bot_direction += out;
		
		return (int)out;
	}
	
	public int update_bot_distance(){
		double out = bot.getDistance()/10;
		bot_distance += out;
		
		return (int)out;
	}
	
	String state = "move";
	public void AI(){
		/*
		window.label_distance.setText("distance = " + bot.getDistance());
		window.label_angle.setText("angle = " + bot.getDirection());
		//window.label_temperature.setText("direction = " + bot.);
		window.label_bumps.setText("bumps = " + bot.getBumpers());
		window.label_battery.setText("battery = " + bot.getBatteryCharge());
		*/
		//window.set_speed(5);
		
		// if quadtree complete set state to "complete"
		//bot.sensors_update();
		switch(state){
			case "move": AI_state_move(); break;
			case "reverse": AI_state_reverse(); break;
			case "turn left": AI_state_turn_left(); break;
			case "turn right": AI_state_turn_right(); break;
			default: run = false;
		}
	}
	
	public boolean follow = false;
	public int follow_direction = 0;
	
	public void AI_state_move(){
		
		// if bumpers not hitting 
		// // send move forward
		// else
		// // add point to quadtree
		// // if (only left) or (only front) bumper set state to "turn right"
		// // if (only right) or (right and front) bumper set state to "turn left"
		// // if all bumpers set state to "reverse"
		
		window.label_spDir_sent.setText(bot.CurMove);
		
		int bump = bot.getBumpers();
		
		int sp = window.get_speed();
		int dr = window.get_direction();

		bot.move(sp, dr);
		window.set_speed(dr);
		
		/*
		if (kinect.updated_skeleton>0){
			kinect.updated_skeleton--;
			sp = 10;
			dr = kinect.skeleton_x;
			if (Math.abs(dr) < 15) dr = 0;
		}
		*/
		
		//window.set_speed(dr);
		
		/*
		Integer[] people = kinect.get_skeletons_screen_x();
		if (kinect.skeleton_updated){
			for(int i = 0; i<people.length; i++) {
				if (people[i] != null){
					//System.out.println(i + " - " + people[i]);
					follow_direction = people[i];
					if (Math.abs(follow_direction) < 15) follow_direction = 0;
				}
			}
			follow = true;
		} else {
			follow = false;
		}
		*/
		//if (follow) {
			//sp = 10;
			//dr = follow_direction;
		//}
		
		//if (kinect.map != null) System.out.println(kinect.getFocalLengthX());	
		//if (kinect.map != null) kinect.map.smooth();
		/*
		String de = "";
		for(int i=50; i<55; i++){
			float d = kinect.get_pixel_depth(i, 0);
			
			de += (int)d + " ";
			
			if (d != 0) {
				System.out.println(
						de+""
						);
			}
		}*/
		//System.out.println(de + "");


		
		//if (bump == 0) {


	}
	
	public double lengthdir_x(double length, double direction) {
		return Math.cos(Math.toRadians(direction))*length;
	}
	public double lengthdir_y(double length, double direction) {
		return -Math.sin(Math.toRadians(direction))*length;
	}
	
	// I know this should be at the top but it only applies to AI_state_reverse()
	boolean reverse_start = false;
	double reverse_start_x = 0;
	double reverse_start_y = 0;
	
	public void AI_state_reverse(){
		update_bot_point();
		
		if(reverse_start == false){
			reverse_start_x = bot_x;
			reverse_start_y = bot_y;
			reverse_start = true;
		}
		
		double bxs = (bot_x-reverse_start_x)*(bot_x-reverse_start_x);
		double bys = (bot_y-reverse_start_y)*(bot_y-reverse_start_y);
		double reverse_distance = Math.sqrt(bxs + bys);
		
		//bot.move(-20);
		if (reverse_distance > 20) {
			reverse_start = false;
			state = "turn right";
		}
	}
	
	
	// I know this should be at the top but it only applies to AI_state_turn_left() and AI_state_turn_right()
	boolean turn_start = false;
	double turn_start_direction = 0;
	
	public void AI_state_turn_left(){
		update_bot_direction();
		
		if(turn_start == false){
			turn_start_direction = bot_direction;
			turn_start = true;
		}
		
		
		// send rotate left slowly
		// if angle > x
		// // set state to "move"
	}
	
	public void AI_state_turn_right(){
		update_bot_direction();
		// send rotate right slowly
		// if angle > x
		// // set state to "move"
	}
	
	
	// just main
	public static void main(String[] args) {new Controller();}
}
