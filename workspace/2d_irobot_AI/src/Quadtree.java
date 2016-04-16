import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Timer;
import java.util.TimerTask;

public class Quadtree {
	
	// for debugging, it needs to talk to everyone
	public Controller controller = null;
	
	// the starting node
	public Node root;
	
	// max depth that the tree can be
	int max_depth = 12;
	
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
		
		//set_point(200, 200, "hit");
		//set_point(400, 200, "free");
		//set_circle( 200, 200, 50, "hit");
		//set_circle( 400, 400, 25, "free");
		
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
	
	// start
	public void set_point(int x, int y, String state){
		set_point(0, root, x, y, state);
		tree_fix(root);
	}
	// run
	private boolean set_point(int depth, Node node, int x, int y, String state){
		
		if (depth > max_depth) {
			return true;
		}
		
		Node newNode = node.split(x, y);
		set_point(depth+1, newNode, x, y, state);
		
		//boolean last = false;
		if (depth == max_depth) {
			node.state = state;
		}
		return false;
	}
	
	// start
	public void set_circle( int cx, int cy, int cr, String state){
		set_circle(0, root,  cx, cy, cr, state);
		tree_fix(root);
	}
	// run
	public boolean set_circle(int depth, Node node, int cx, int cy, int cr, String state){
		if (depth > max_depth) {
			return true;
		}
		
		/*
		if (node.UL.state == state && node.UR.state == state && node.DL.state == state && node.DR.state == state){
			return true;
		}*/
		
		if (node.state == state)
			return true;
		
		if (intersects(cx, cy, cr, node)) {
			node.split();
			set_circle(depth+1, node.UL, cx, cy, cr, state);
			set_circle(depth+1, node.UR, cx, cy, cr, state);
			set_circle(depth+1, node.DL, cx, cy, cr, state);
			set_circle(depth+1, node.DR, cx, cy, cr, state);
		}
		
		if (depth == max_depth) {
			node.state = state;
		}
		return false;
	}
	
	public boolean intersects(int cx, int cy, int cr, Node node)
	{
	    int cd_x = Math.abs(cx - (node.x1 + node.x2)/2);
	    int cd_y = Math.abs(cy - (node.y1 + node.y2)/2);

	    int w = node.x2 - node.x1;
	    int h = node.y2 - node.y1;
	    
	    if (cd_x > (w/2 + cr)) { return false; }
	    if (cd_y > (h/2 + cr)) { return false; }

	    if (cd_x <= (w/2)) { return true; } 
	    if (cd_y <= (h/2)) { return true; }

	    int cod_sq = (cd_x - w/2)*(cd_x - w/2) + (cd_y - h/2)*(cd_y - h/2);

	    return (cod_sq <= (cr*cr));
	}
	
	// 
	private void tree_fix(Node node) {
		if (node.state == "not checked") {
			
			int ch_UL = 0;
			int ch_UR = 0;
			int ch_DL = 0;
			int ch_DR = 0;

			int count = 0;

			if (node.UL != null) {tree_fix(node.UL); ch_UL = 1; count++;}
			if (node.UR != null) {tree_fix(node.UR); ch_UR = 1; count++;}
			if (node.DL != null) {tree_fix(node.DL); ch_DL = 1; count++;}
			if (node.DR != null) {tree_fix(node.DR); ch_UR = 1; count++;}
			
			if (count == 4) {
				if ((node.UL.state == "hit" && node.UR.state == "hit" && node.DL.state == "hit" && node.DR.state == "hit") ||
					(node.UL.state == "free" && node.UR.state == "free" && node.DL.state == "free" && node.DR.state == "free")){
					node.state = node.UL.state;
					node.free_children();
				}
				/*
				if (node.UL.state != "not checked") {
					if ((node.UL.state == node.UR.state) && (node.UL.state == node.DR.state) && (node.UL.state == node.DL.state)){
						node.state = node.UL.state;
						node.free_children();
					}
				}*/
			}
		}
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
		
		//
		public void split(){
			int count = 0;
			if (UL == null) {UL = new Node(x1, y1, hx, hy); count++;}
			if (UR == null) {UR = new Node(hx, y1, x2, hy); count++;}
			if (DL == null) {DL = new Node(x1, hy, hx, y2); count++;}
			if (DR == null) {DR = new Node(hx, hy, x2, y2); count++;}
			
			if (count == 4) {
				UL.state = state;
				UR.state = state;
				DL.state = state;
				DR.state = state;
				state = "not checked";
			}
		}

		// split the node and get the node at that point
		public Node split(int x, int y){
			this.split();
			
			if (x <= hx && y <= hy) return UL;
			if (x >= hx && y <= hy) return UR;
			if (x <= hx && y >= hy) return DL;
			if (x >= hx && y >= hy) return DR;
			
			// there has been a really big error if this returns
			return UL;
		}
		
		public void free_children() {
			if (UL != null) {UL.free_children(); UL = null;}
			if (UR != null) {UR.free_children(); UR = null;}
			if (DL != null) {DL.free_children(); DL = null;}
			if (DR != null) {DR.free_children(); DR = null;}
		}
		
		//
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
