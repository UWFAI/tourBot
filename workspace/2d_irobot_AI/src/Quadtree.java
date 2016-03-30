
public class Quadtree {
	
	// the starting node
	Node root;
	
	// max depth that the tree can be
	int max_depth = 10;
	
	// I was thinking that this has a way of knowing if a place is free, !free, !Checked
	// if we are to do this we need to send a point every step and not just at the 
	// time of a hit
	boolean complete = true;
	
	// 
	public Quadtree(int x1, int y1, int x2, int y2){
		root = new Node(x1,y1,x2,y2);
	}
	
	public void set_point(int x, int y){
		set_point(0, root, x, y);
	}
	
	private void set_point(int depth, Node node, int x, int y){
		if (depth > max_depth) return;
		
		set_point(depth+1, node.split(x, y), x, y);
	}
	
	//
	private class Node {
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
			UL = new Node(x1, y1, hx, hy);
			UR = new Node(hx, y1, x2, hy);
			DL = new Node(x1, hy, hx, y2);
			DR = new Node(hx, hy, x2, y2);
			
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
		
		public Node(Node parent, int x1, int y1, int x2, int y2){
			this.parent = parent;
			this.depth = parent.depth+1;
			
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
			
			this.hx = (x1+x2)/2;
			this.hy = (y1+y2)/2;
		}
		
	}
}
