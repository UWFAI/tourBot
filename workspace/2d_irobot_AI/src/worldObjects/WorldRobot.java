package worldObjects;

import java.awt.Color;
import java.awt.Graphics2D;

public class WorldRobot extends WorldObject{
	double velocity;
	double direction;
	double turnRadius;
	double theta;
	int x1;
	int y1;
	
	public WorldRobot(double a , double b, double width, double height){
		super(a,b,width, height);
		velocity=0;
		direction=0;
		theta=0;
	}
	
	public void update(){
		x+=lengthdir_x(velocity,direction);
		y+=lengthdir_y(velocity,direction);
		direction += theta;
		
		runNextOpCode();
	}
	
	public void draw(Graphics2D g){
		x1=(int)x + (int)(width*pixelsPerMeter/2);
		y1=(int)y + (int)(height*pixelsPerMeter/2);
		
		g.setColor(Color.ORANGE);
		g.fillOval(x1, y1, (int)(width*pixelsPerMeter), (int)(height*pixelsPerMeter));
		g.setColor(Color.BLACK);
		g.drawLine( x1+(int)(width*pixelsPerMeter/2),y1+(int)(height*pixelsPerMeter/2),
				(x1+(int)(width*pixelsPerMeter/2))+(int)(Math.cos(Math.toRadians(direction))*(int)(height*pixelsPerMeter/2)), 
				(y1+(int)(height*pixelsPerMeter/2))-(int)(Math.sin(Math.toRadians(direction))*(int)(height*pixelsPerMeter/2)));
		
	}
	
	public void runNextOpCode(){
		int opcode = (int)controller.io.getNextInput();
		switch(opcode){
			case 137:drive(); break;
			case 145:driveDirect(); break;
			case -2: stop(); break;
			case -1: reset(); break;
		}
	}
	
	public int twoComp(int n, int base){
		int j;
		j=0;
		if(n<0){
			j=(int)Math.pow(2.0, base)+n;
		}
		else 
			j=n;
		return j;
	}
	
	public int findVal(int a, int b)
	{
		int n;
		n=0;
		n=a*256 + b;
		if(n>32768){
			n-=65536;
			return n;
		}
		else return n;
		
	}
	
	public void drive() {
		
		int sp_h = (int) controller.io.getNextInput();
		int sp_l = (int) controller.io.getNextInput();
		
		int dr_h = (int) controller.io.getNextInput();
		int dr_l = (int) controller.io.getNextInput();
		
		velocity = ((short) ((sp_h << 8) | sp_l))/1000.0;
		turnRadius = ((short) ((dr_h << 8) | dr_l))/100.0;
		
		System.out.println(dr_h+" "+dr_l+" ");
		System.out.println("turnRadius = "+turnRadius+"  "+(short) ((dr_h << 8) | dr_l));
		
		theta = (turnRadius)*velocity;
		
		 // Special cases:
		 // Straight
		if ((dr_h == 0x80 && dr_l == 0x00) || (dr_h == 0x7F && dr_l == 0xFF)) {
			theta = 0;
		}
		 // Turn in place clockwise
		if (dr_h == 0xFF && dr_l == 0xFF) {
			velocity = 0;
		}
		 // Turn in place counter-clockwise
		if (dr_h == 0x00 && dr_l == 0x01) {
			velocity = 0;
		}
		
		/*
		if(turnRadius!=0) {
			theta=(velocity/turnRadius)/10;
		} else {
			theta=0;
		}
		*/
	}
	
	public void reset(){
		x = 300;
		y = 300;
		//velocity=0;
		//theta=0;
		//turnRadius=0;
	}
	public void stop(){
		velocity=0;
		theta=0;
		turnRadius=0;
	}
	public void maintain(){
		
	}
	
	public void driveDirect(){
		
	}
}
