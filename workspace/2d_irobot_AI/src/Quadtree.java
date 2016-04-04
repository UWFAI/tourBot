import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Timer;
import java.util.TimerTask;

public class Quadtree {
	
	// for debugging, it needs to talk to everyone
	public Controller controller = null;
	
	// the starting node
	Node root;
	
	// max depth that the tree can be
	int max_depth = 10;
	
	// I was thinking that this has a way of knowing if a place is free, !free, !Checked
	// if we are to do this we need to send a point every step and not just at the 
	// time of a hit
	boolean complete = true;
	
	// 
	public Quadtree(Controller con, int x1, int y1, int x2, int y2){
		controller = con;
		root = new Node(x1,y1,x2,y2);
		
		controller.window.panel_tree.painter = this;
		controller.window.panel_tree.reSize(root.x2 - root.x1, root.y2 - root.y1);
		
		set_point(200, 200, "hit");
		set_point(400, 200, "free");
		
		
		// a simple timer to repaint the tree
		Timer timer = new Timer();
		TimerTask myTask = new TimerTask() {
		    @Override
		    public void run() {
		    	controller.window.panel_tree.repaint();
		    }
		};
		timer.schedule(myTask, 1000, 1000);
	}
	
	//
	public void set_point(int x, int y, String state){
		set_point(0, root, x, y, state);
	}
	private boolean set_point(int depth, Node node, int x, int y, String state){
		if (depth > max_depth) {
			return true;
		}
		
		Node newNode = node.split(x, y);
		boolean last = set_point(depth+1, newNode, x, y, state);
		if (last) {
			node.state = state;
		}
		return false;
	}
	
	// 
	public void set_circle(int x1, int y1, int x2, int y2, String state){
		set_point(0, root, x1, y1, state);
	}
	public void set_circle(int depth, Node node, int x1, int y1, int x2, int y2, String state){
		set_point(0, root, x1, y1, state);
	}
	
	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		// draw all the nodes recursively
		draw_node(root, g2);
	}
	
	private void draw_node(Node node, Graphics2D g2){

		// fill
		if (node.state == "not checked"){g2.setColor(Color.WHITE);}else
		if (node.state == "free"){g2.setColor(Color.GREEN);}else
		if (node.state == "hit") {g2.setColor(Color.RED);}else{g2.setColor(Color.YELLOW);}
		
		if (node.state == "hit" || node.state == "free")
			g2.fillRect(node.x1, node.y1, node.x2-node.x1, node.y2-node.y1);
		
		g2.setColor(Color.BLACK);
		g2.drawRect(node.x1, node.y1, node.x2-node.x1, node.y2-node.y1);
		// draw the current nodes outline
		/*
		g2.setColor(Color.BLACK);
		g2.drawLine(node.x1, node.y1, node.x2, node.y1);// top
		g2.drawLine(node.x1, node.y2, node.x2, node.y2);// bottom
		g2.drawLine(node.x1, node.y1, node.x1, node.y2);// left
		g2.drawLine(node.x2, node.y1, node.x2, node.y2);// right
		*/
		//
		if (node.UL != null) draw_node(node.UL, g2);
		if (node.UR != null) draw_node(node.UR, g2);
		if (node.DL != null) draw_node(node.DL, g2);
		if (node.DR != null) draw_node(node.DR, g2);
		
	}	
	
	//
	private class Node {
		/**
		 * There are three state:
		 * "not checked"
		 * "hit"
		 * "free"
		 */
		public String state = "not checked";
		
		Node UL = null;
		Node UR = null;
		Node DL = null;
		Node DR = null;
		
		Node parent = null;
		
		int depth = 0;
		
		int x1 = 0;// top left point
		int y1 = 0;
		int x2 = 0;// bottom right point
		int y2 = 0;
		
		int hx = 0; // half x
		int hy = 0; // half y

		public Node split(int x, int y){
			
			if (UL == null) UL = new Node(x1, y1, hx, hy);
			if (UR == null) UR = new Node(hx, y1, x2, hy);
			if (DL == null) DL = new Node(x1, hy, hx, y2);
			if (DR == null) DR = new Node(hx, hy, x2, y2);
			
			if (x <= hx && y <= hy) return UL;
			if (x >= hx && y <= hy) return UR;
			if (x <= hx && y >= hy) return DL;
			if (x >= hx && y >= hy) return DR;
			
			// there has been a really big error if this returns
			return UL;
		}
		
		public Node(int x1, int y1, int x2, int y2){
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
			
			this.hx = (x1+x2)/2;
			this.hy = (y1+y2)/2;
		}

	}
	

	
}