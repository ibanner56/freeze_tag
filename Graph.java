/**
 * Data Structure to maintain the graph information necessary to the Traveling
 * Salesman evaluation.
 *
 * Created by Isaac on 2/7/2015.
 */

import java.text.DecimalFormat;
import java.util.Random;
import java.util.Stack;

public class Graph {

    // Graph types because I can never remember how to write an actual enum...
    public static final int OPTIMAL = 0;
	public static final int GREEDY = 1;
	public static final int MST = 2;
    public static final int BITONIC = 3;

    private int n;                  // Number of points in the graph
    public Point[] points;         // Array of points in graph
    public Edge[] edges;           // Array of edges in graph
    public boolean[] occupiedX;    // Array of occupied x coordinates
    public double[] adjacency;     // Adjacency matrix representation of the graph
    
	private int edgeCount;			// How many edges are in the graph

	private int gType;	// Whether this is a greedy graph
	private int[] parents;			// Parent list for cycle detection
	private int[] rank;				// Rank list for union by rank	
	private int[] traffic;			// Keeps track of # of edges out from verts
	private int[] connection;		// Keeps track of the edge order in the path
	
    /**
     * Generates the graph for the given n and seed without an edge list.
     * @param n - number of vertices in graph
     * @param seed - seed to generate the "random" verts
     */
    public Graph(int n, int seed) {
        this(n, seed, false);
    }

    /**
     * Generates the graph for the given n and seed. Makes an edge list if asked.
     * @param n - number of vertices in graph
     * @param seed - seed to generate the "random" verts
     * @param edgeList - whether to generate an edge list for the graph
     */
    public Graph(int n, int seed, boolean edgeList) {
        this.n = n;
        points = new Point[n];
        occupiedX = new boolean[n];
        adjacency = new double[n*n];
		gType = 0;

        // Generate the points.
        Random rx = new Random(seed);
        Random ry = new Random(2*seed);
        for(int i = 0; i < n; i++){
            int x = rx.nextInt(n);
            int y = ry.nextInt(n);
            while(occupiedX[x]){
                x = rx.nextInt(n);
                y = ry.nextInt(n);
            }
            occupiedX[x] = true;
            points[i] = new Point(x, y, i);
        }

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                if (j < i) adjacency[n*i + j] = adjacency[n*j + i];
                else if (i == j) adjacency[n*i + j] = 0.;
                else adjacency[n*i + j] = Point.distance(points[i], points[j]);
            }
        }

        for(int i = 1; i < n; i++) {
            points[i].setPriority(adjacency[i], points[0].i);
        }

        if(edgeList) {
            this.edges = new Edge[(n) * (n-1) / 2];
            int index = 0;
            for(int i = 0; i < n; i++){
                for(int j = n -1; j > i; j--) {
                    edges[index++] = new Edge(points[i], points[j]);
                }
            }
        }
    }
	
	/**
	 * Starts a graph given a single edge.
	 * @param edge - the first edge in the graph
	 * @param n - how many points are in the graph
     * @param gType - what kind of graph we have
	 */ 
	public Graph(Edge edge, int n, int gType) {
		this.n = n;
		this.gType = gType;
		points = new Point[n];
		edges = new Edge[n];
		edgeCount = 0;
		parents = new int[n];
		rank = new int[n];
		traffic = new int[n];
		adjacency = new double[n*n];
		connection = new int[2*n];

		for(int i = 0; i < n; i++) {
			parents[i] = i;
			rank[i] = 0;
			connection[2*i] = -1;
			connection[2*i + 1] = -1;
		}
		
		// Add v1 to the list
		int u = edge.v1.i;
		points[u] = edge.v1;
		rank[u] = 1;		
		traffic[u]++;

		// Add v2 to the list, with a as the root
		int v = edge.v2.i;
		points[v] = edge.v2;
		parents[v] = u;
		traffic[v]++;
		
		connection[2*u] = v;
		connection[2*v] = u;
		adjacency[u*n + v] = edge.weight;
		adjacency[v*n + u] = edge.weight;
		edges[edgeCount++] = edge;	
	}

	/**
	 * Union-by-rank algorithm from the notes.
	 * @param u - first node
	 * @param v - second node
	 */
	private void union(int u, int v) {
		int i = find(u);
		int j = find(v);
		if (rank[i] > rank[j]) {
			parents[j] = i;
		} else {
			parents[i] = j;
			if (rank[i] == rank[j]) rank[j]++;
		}
	}	

	/**
	 * Find algorithm from the notes.
	 * @param v - node to root.
	 */ 
	private int find(int v) {
		if(parents[v] != v) {
			parents[v] = find(parents[v]);
		}

		return parents[v];
	}

	/**
	 * Attempts to add an edge to the graph. Fails if it creates a cycle
	 * or allows too many edges out from a single node.
	 *
	 * @param edge - the edge to add.
	 */
	public boolean add(Edge edge) {
		int u = edge.v1.i;
		int v = edge.v2.i;
		points[u] = edge.v1;
		points[v] = edge.v2;
		if(gType == GREEDY || gType == BITONIC) {
			int root1 = find(u);
			int root2 = find(v);	
			if(!(root1 == root2 && edgeCount != n - 1) 
					&& traffic[u] < 2 && traffic[v] < 2) {
				edges[edgeCount++] = edge;
				adjacency[u*n + v] = edge.weight;
				adjacency[v*n + u] = edge.weight;
				traffic[u]++;
				traffic[v]++;
				union(root1, root2);
				if(connection[2*u] == -1) connection[2*u] = v;
				else connection[2*u + 1] = v;
				if(connection[2*v] == -1) connection[2*v] = u;
				else connection[2*v + 1] = u;
				return true;
			} else return false;
		} else {
			edges[edgeCount++] = edge;
			adjacency[u*n + v] = edge.weight;
			adjacency[v*n + u] = edge.weight;
			return true;
		}
	}

    /**
     * Returns the distance between two points in the graph.
     * @param a - First point
     * @param b - Second point
     * @return distance from a to b
     */
    public double distance(int a, int b) {
        if (a < 0 || a >= n || b < 0 || b >= n) {
            throw new IllegalArgumentException("a and b must be points in the graph");
        }

        return adjacency[n*a + b];
    }

	public Edge[] getEdges() {
		return edges;
	}

    /**
     * Replaces objects toString - prints out as required the list of points
     * and the adjacency matrix for the graph.
     */
    public String toString() {
		String result = "";
	    DecimalFormat df = new DecimalFormat("0.00");
		
		if (gType == 0) {
			result = "X-Y Coordinates:\n";
	
			for(int i = 0; i < points.length; i++) {
			    result += "v" + i + ": " + points[i] + " ";
			}

			result += "\n\n";
        } else if (gType == 1) {
			result = "Greedy Graph: \n";
		} else if (gType == 2) {
			result = "Minimum Spanning Tree: \n";
		}

		result += "Adjacency matrix of graph weights:"+ "\n\n";
		for(int i = 0; i < n; i++) {
			result += "      " + i;
		}

        for(int i = 0; i < n; i++) {
            result += "\n\n" + i;

            for(int j = 0; j < n; j++) {
                result += "   " + df.format(adjacency[n*i + j]);
            }

            result += "  ";
        }
		
		if(gType == 1) result += "\n\n" + greedyString();
		else if (gType == 2) result += "\n\n" + mstString();
        else result += "\n";
		return result;
    }

	/**
	 * Generates a string representation of the edges in the graph
	 * as per the writeup guidelines.
	 */
	private String greedyString() {
		String result = "Edges of tour from greedy graph:";
		for(Edge e : edges) {
			result += "\n" + e.toString();
		}
		return result;
	}

    /**
     * @return a string representation of the MST information to be printed.
     */
	public String mstString() {
		String result = "Total weight of mst: ";
		DecimalFormat df = new DecimalFormat("0.00");

		double weight = 0.;
		for (Edge e : edges) {
			if(e != null)
			weight += e.weight;
		}
		
		result += df.format(weight) + "\n\n";
		result += "Pre-order Traversal:";
		for(int i = 0; i < points.length; i++) {
			result += "\nParent of " + i + " is " + points[i].pVert;
		}

		return result;
	}

	/**
	 * Generates the path info printout.
	 */	
	public String edgePath() {
		DecimalFormat df = new DecimalFormat("0.00");
		double dist = 0.;
		for(Edge e: edges) {
			dist += e.weight;
		}
		
		boolean[] added = new boolean[n];
		int i = (connection[0] < connection[1])? connection[0] : connection[1];
		added[i] = true;
		String pathString = "0 " + i;
		while(i != 0) {
			int next = connection[2*i];
			if(added[next] || next == 0 && !added[connection[2*i + 1]]) {
				next = connection[2*i + 1];
			}
			
			pathString += " " + next;
			added[next] = true;
			i = next;	
		}

		String graphType = (gType == GREEDY)? "greedy" : "bitonic";

		return "\nDistance using " + graphType + ": " + df.format(dist) 
			+ " for path " + pathString;
	}

	/**
	 * Generates a path info printout.
	 */
	public String mstPath() {
		Stack<Integer> path = new Stack<Integer>();
		double distance = 0.;
		String traversal = "";
		DecimalFormat df = new DecimalFormat("0.00");

		path.push(0);
		int last;
		int node = -1;

		while(!path.empty()) {
			last = node;
			node = path.pop();
			traversal += node + " ";
			if(last >= 0) 
				distance += Point.distance(points[last], points[node]);

            Integer[] children = new Integer[points[node].children.size()];
            points[node].children.toArray(children);
            Quicksortr.sort(children);

			for(int i = children.length - 1; i >= 0; i--) {
				path.push(children[i]);
			}
		}

		traversal += "0";
		distance += Point.distance(points[node], points[0]);

		return "\nDistance using mst: " + df.format(distance) 
			+ " for path " + traversal;
	}
}
