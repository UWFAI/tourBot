package worldObjects;

import java.awt.Graphics2D;

public class Wobj_wall extends WorldObject{

	public Wobj_wall(double x, double y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update() {
		direction += .5;
	}

	@Override
	public void draw(Graphics2D g) {
		int x1 = (int) x;
		int y1 = (int) y;
		int x2 = (int) (x+lengthdir_x(16, direction));
		int y2 = (int) (x+lengthdir_y(16, direction));
		g.drawLine(x1, y1, x2, y2);
	}

}
