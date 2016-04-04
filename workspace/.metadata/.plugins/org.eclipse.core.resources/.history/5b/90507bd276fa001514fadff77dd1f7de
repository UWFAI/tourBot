package create2_environment;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import worldObjects.WorldObject;

public class World {
	
	//
	static Controller controller;
	
	// the width of the world in meters
	double worldWidth;
	
	// the height of the world in meters
	double worldHeight;
	
	// how many pixels are in a meter
	private int pixelsPerMeter;
	
	// the buffer that will be drawn for the user to see
	BufferedImage worldBuffer;
	
	// creating a graphics object to draw on the buffer
	Graphics2D worldG;
	
	// this array will hold everthing that needs to be in the world
	ArrayList<WorldObject> objects = new ArrayList<WorldObject>();
	
	//
	public World(double worldWidth, double worldHeight, int pixelsPerMeter)
	{
		// 
		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;
		set_pixelsPerMeter(pixelsPerMeter);
		
		worldBuffer = new BufferedImage((int)worldWidth*pixelsPerMeter, (int)worldHeight*pixelsPerMeter, BufferedImage.TYPE_INT_RGB);
		worldG = worldBuffer.createGraphics();
	}
	
	//
	public void add(WorldObject obj)
	{
		obj.world = this;
		obj.pixelsPerMeter = pixelsPerMeter;
		objects.add(obj);
	}
	
	//
	public void set_pixelsPerMeter(int p)
	{
		pixelsPerMeter = p;
		for(WorldObject obj: objects)
		{
			obj.pixelsPerMeter = pixelsPerMeter;
		}
	}
	public int get_pixelsPerMeter()
	{
		return pixelsPerMeter;
	}
	
	// loops through each item in objects and calls its update method
	public void update()
	{
		
		for(WorldObject obj: objects)
		{
			obj.update();
		}
	}
	
	// loops through each item in objects and calls its draw method
	public void draw(Graphics2D g, int x, int y)
	{
		//
		worldG.setColor(Color.WHITE);
		worldG.fillRect(0, 0, (int)worldWidth*pixelsPerMeter, (int)worldHeight*pixelsPerMeter);
		worldG.setColor(Color.BLACK);
		//
		for(WorldObject obj: objects)
		{
			obj.draw(worldG);
		}
		
		//
		int newWidth = ((int)worldWidth)*pixelsPerMeter;
		int newHeight = ((int)worldHeight)*pixelsPerMeter;
		g.drawImage(worldBuffer, x, y, newWidth, newHeight, null);
	}

}
