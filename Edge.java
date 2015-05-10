import java.text.DecimalFormat;

/**
 * Created by Isaac on 3/12/2015.
 * Class representation of a graph edge.
 */
public class Edge implements Comparable<Edge>{
    public final Point v1, v2;		// Edge endpoints
    public final double weight;		// Edge weight

	/**
	 * Creates an edge and calculates its weight.
	 * @param v1, v2 - the edge endpoints
	 */
    public Edge(Point v1, Point v2) {
        this.v1 = v1;
        this.v2 = v2;
        this.weight = Point.distance(v1, v2);
    }

	/**
	 * Compares the edges as described in the project writeup.
	 * @param other - the other edge
	 */ 
    public int compareTo(Edge other) {
        if (this.weight < other.weight) return -1;
        else if (this.weight > other.weight) return 1;
        else if (this.v1.i < other.v1.i) return -1;
        else if (this.v1.i > other.v1.i) return 1;
        else if (this.v2.i < other.v2.i) return -1;
        else if (this.v2.i > other.v2.i) return 1;
        else return 0;
    }

    public String toString() {
		DecimalFormat df = new DecimalFormat("0.00");
        return "" + v1.i + " " + v2.i + " weight " + df.format(weight);
    }
	
	/**
	 * Was just testing the compare operation. Move along.
	 */ 
    public static void main(String[] args) {
        Point v1 = new Point(1, 2, 1);
        Point v2 = new Point(2, 1, 2);
        Point v3 = new Point(3, 2, 3);
        Point v4 = new Point(4, 6, 4);
        Edge a = new Edge(v1, v2);
        Edge b = new Edge(v2, v3);
        Edge c = new Edge(v1, v4);
        Edge d = new Edge(v2, v4);
        Edge e = new Edge(v3, v4);
        Edge[] edges = {a, b, c, d, e};
        Quicksortr.sort(edges);
        for(int i = 0; i < edges.length; i++){
            System.out.println(edges[i]);
        }
    }
}
