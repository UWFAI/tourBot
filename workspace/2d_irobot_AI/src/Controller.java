
public class Controller {
	
	Communication com;
	create2_environment.Controller sim_con = null;
	
	IRobot bot;
	Quadtree quadtree;
	TabletComm tab;
	boolean run = true;
	
	// some helper variables
	int tree_root_size = 2000; // this might be in mm, if it is it needs to be much bigger
	double bot_x = tree_root_size/2;
	double bot_y = tree_root_size/2;
	double bot_direction = 0;
	
	DebugWindow window;
	
	public Controller() {
		window = new DebugWindow(this);
		quadtree = new Quadtree(this, 0, 0, 5000, 5000);// this size is in cm
		
		com = new Communication(this);
		
		bot = new IRobot(com);
		
		tab = new TabletComm(this);
		while (run) AI();
	}
	
	public void update_bot_point(){
		int dis = bot.getDistance();
		System.out.println(dis);
		bot_x = Math.cos(Math.toRadians(bot_direction))*dis;
		bot_y = -Math.sin(Math.toRadians(bot_direction))*dis;
	}
	
	public void update_bot_direction(){
		bot_direction = bot.getDirection();
	}
	
	String state = "move";
	public void AI(){
		/*
		window.label_distance.setText("distance = " + bot.getDistance());
		window.label_angle.setText("direction = " + bot.getDirection());
		//window.label_temperature.setText("direction = " + bot.);
		window.label_bumps.setText("bumps = " + bot.getBumpers());
		window.label_battery.setText("battery = " + bot.getBatteryCharge());
		*/
		window.set_speed(5);
		
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
	
	public void AI_state_move(){
		update_bot_point();
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
		
		//if (bump == 0) {
			bot.move(sp, dr);
			quadtree.set_circle( (int)bot_x+50, (int)bot_y+50, 25, "free");
			//System.out.println(sp+", "+dr);
		//} 
		/*
		else {
			quadtree.set_point((int) bot_x, (int) bot_y);
			if (bump == 1 || bump == 2) {
				state = "turn right";
			} else if (bump == 4 || bump == 6) {
				state = "turn left";
			} else {
				state = "reverse";
			}
		}*/
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
