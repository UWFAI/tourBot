package worldObjects;

import java.awt.Graphics2D;

import create2_environment.Controller;
import create2_environment.World;

public abstract class WorldObject {
	//
	public static Controller controller;
	
	// 
	public World world;
	
	// the world will handle all WorldObjects pixels per meter
	public int pixelsPerMeter = 0;
	
	// the name of the object
	String name = "";
	
	// the x pos in the world in meters
	double x = 0.0;
	
	// the y pos in the world in meters
	double y = 0.0;

	// the width of the object in meters
	double width = 0.0;

	// the height of the object in meters
	double height = 0.0;
	
	// The direction that a world object is pointing
	// 0 is pointing to the right and 90 is up
	double direction = 0.0;
	
	public WorldObject(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	public WorldObject(double x, double y, double width, double height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	public WorldObject(String name, double x, double y, double width, double height)
	{
		this.name = name;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	// some math helpers
	public double lengthdir_x(double length, double direction)
	{
		return Math.cos(Math.toRadians(direction))*length;
	}
	public double lengthdir_y(double length, double direction)
	{
		return -Math.sin(Math.toRadians(direction))*length;
	}
	
	//
	public abstract void update();
	public abstract void draw(Graphics2D g);
}
