/**
 * Created by Isaac on 4/12/2015.
 */
public class MstTSP {

    private int n, seed;        // Same thing as always
    private Graph graph;        // Same as always
	private boolean[] popped;   // Array to keep track of what
	                            // priorities don't need updating.

	private int[] numChildren;	// The number of child nodes for each node.

    /**
     * Spins up an MST run
     * @param n - number of points
     * @param seed - seed for Random
     */
    public MstTSP(int n, int seed) {
        this.n = n;
        this.seed = seed;
		this.popped = new boolean[n];
		popped[0] = true;
		this.numChildren = new int[n];
        this.graph = new Graph(n, seed, false);
    }

    /**
     * Generates the MST for the graph, then uses the preorder of the tree
     * to build the MST cycle to traverse the graph.
     */
    public void travel() {
		long startTime = System.currentTimeMillis();
        VHeap heap = new VHeap(n);
        for(int i = 1; i < graph.points.length; i++) {
			heap.insert(graph.points[i]);
        }

        Point p = graph.points[0];
        Point q = heap.pop();

		p.addChild(q);
		numChildren[0] = 1;

        Graph mstG = new Graph(new Edge(p, q), n, Graph.MST);

        // Pop the next thing, then update any priorities that
        // have changed a la prim
		while(!heap.isEmpty()) {
			popped[q.i] = true;
			numChildren[p.i]++;
			if(numChildren[p.i] >= 2) {
				for(int i = 0; i < n; i++) {
					if(!popped[i])
						graph.points[i].resetPriority();
				}
				for(int i = 0; i < n; i++) {
					for(int j = 0; j < n; j++) {
						if(!popped[j] && popped[i] && numChildren[i] < 2) {
							if(graph.points[i].priority - Point.d > 
								graph.adjacency[i*n + j]) {
								graph.points[j].setPriority(graph.adjacency[i*n + j], i);
							}
						}
					}
				}
				VHeap.reheapify(heap);
			}
			
			boolean pChange = false;
            for(int i = 0; i < n; i++) {
				if(!popped[i] &&
                        graph.points[i].priority - Point.d > graph.adjacency[q.i*n + i]) {
					graph.points[i].setPriority(graph.adjacency[q.i*n + i], q.i);
					pChange = true;
				}
			}

            if(pChange) VHeap.reheapify(heap);
			
            q = heap.pop();
            p = graph.points[q.pVert];
            mstG.add(new Edge(p, q));
			p.addChild(q);
        }

		if( n <= 10 ) {
			System.out.println(graph);
			System.out.println(mstG);
		}
		System.out.println(mstG.mstString());
		long endTime = System.currentTimeMillis();
		System.out.println("Runtime for Mst TSP   : " + (endTime - startTime) + " milliseconds");
	}

    /**
     * Runs input checking, then calls travel.
     * @param args - Command line input.
     */
    public static void main(String[] args) {
        if(args.length != 2) {
            System.out.println("Usage: java GreedyTSP n seed");
            return;
        }

        int n, seed;
        try {
            n = Integer.parseInt(args[0]);
            seed = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Command line args must be integers");
            return;
        }

        if(n < 1) {
            System.out.println("Number of vertices must be greater than 0");
            return;
        }

        MstTSP mst = new MstTSP(n, seed);
        mst.travel();
    }
}
