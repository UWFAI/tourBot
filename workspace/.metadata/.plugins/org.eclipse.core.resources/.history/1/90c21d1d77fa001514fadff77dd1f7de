package create2_environment;

import java.util.Timer;
import java.util.TimerTask;

import worldObjects.WorldObject;

/**
 * This is the main class that will be the link between all the others
 * Each class needs to know who there controller is, preferably by having
 * a static variable that points to it.
 */
public class Controller {
	
	// this controls the timing of a step in milliseconds
	final int startTimeDelay = 1;
	final int timePeriod = 5;
	
	//
	Window window;
	World world;
	public Robot_IO io;
	
	public Controller()
	{
		// This can be done because controller is static in each one
		Robot_IO.controller = this;
		World.controller = this;
		WorldObject.controller = this;
		Window.controller = this;
		
		// creating each aspect of the emulator
		io = new Robot_IO();
		world = new World(12, 10, 60);
		window = new Window(world);
		
		// adds all the objects with a static method
		WorldCreate.create(world);
		
		// a timer that calls the step method every timePeriod
		new Timer().schedule(new TimerTask() 
		{
			public void run() 
			{
				step();
			}
		}, startTimeDelay, timePeriod);
	}
	
	// 
	public void step()
	{
		world.update();
		window.update();
		window.draw();
	}
	
	// no need to even look at
	public static void main(String[] args) {new Controller();}
}
