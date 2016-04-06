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
		direction+=theta;
		
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
		case 145:driveDirect();break;
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
	
	public void drive(){
		velocity=controller.io.getNextInput()/100;
		turnRadius = controller.io.getNextInput()/100;
		
		if(turnRadius!=0)
		theta=(velocity/turnRadius)/10;
		else theta=0;
		
	}
	
	public void reset(){
		x=300;
		y=300;
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
