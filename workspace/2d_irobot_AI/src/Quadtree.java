
public class Quadtree {
	
	Node root;
	
	// 
	public Quadtree(int width, int height){
		root = new Node(width, height);
	}
	
	public void set_point(int x, int y){
		
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

		
		public Node(int width, int height){
			
		}
		public Node(Node parent, int width, int height){
			
		}
		
	}
}
