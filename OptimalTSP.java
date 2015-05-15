/**
 * Program to run the first (the awful) algorithm for Traveling Salesman
 *
 * Created by Isaac on 2/7/2015.
 */

import java.text.DecimalFormat;

public class OptimalTSP {

    private int n, seed;    // Number of verts and the seed
    private Graph graph;    // The graph

    /**
     * Spins up a run with the given settings
     * @param n - number of verts
     * @param seed - seed to generate the points
     */
    public OptimalTSP(int n, int seed) {
        this.n = n;
        this.seed = seed;
        this.graph = new Graph(n, seed);
    }

    /**
     * Runs the algorithm on the instance's graph
     */
    public long travel() {
        DecimalFormat df = new DecimalFormat("0.00");
        double min = Integer.MAX_VALUE;
        Path minPath = null;

        if (n <= 10) {
             System.out.println(graph + "\n");
        }

        long startTime = System.currentTimeMillis();
        Path firstPath = Path.firstLexPath(n, graph);
        if (n <= 9) System.out.println(firstPath);

        Path newPath = Path.nextLexPath(firstPath, graph);
        while(newPath != null && !firstPath.equals(newPath)) {
            if (n <= 9) System.out.println(newPath);
            if(newPath.getDistance() < min) {
                min = newPath.getDistance();
                minPath = newPath;
            }

            newPath = Path.nextLexPath(newPath, graph);
        }
        long endTime = System.currentTimeMillis();

         System.out.println("\n" + "Optimal distance: " + df.format(minPath.getDistance())
               + " for path " + minPath.getPath());
        System.out.println("Runtime for optimal TSP   : " + (endTime - startTime) + " milliseconds");
    
		return endTime - startTime;
	}

    /**
     * Makes an OptimalTSP instance and runs it.
     * @param args - command line args: n and seed
     */
    public static void main(String[] args) {
        // Check for 2 args
        if (args.length != 2) {
            System.out.println("Usage: Usage: java OptimalTSP n seed");
            return;
        }

        // Check for int args
        int n, seed;
        try {
            n = Integer.parseInt(args[0]);
            seed = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException ex) {
            System.out.println("Command line args must be integers");
            return;
        }

        // Check for n within range
        if (n < 1 || n > 13) {
            System.out.println("Number of vertices must be between 1 and 13");
            return;
        }

        OptimalTSP otsp = new OptimalTSP(n, seed);
        otsp.travel();
    }
}
