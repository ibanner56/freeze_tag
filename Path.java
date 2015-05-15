/**
 * Data structure to hold the list of vertices in the order of the traversal
 * as well as the length of the traversal.
 *
 * Created by Isaac on 2/7/2015.
 */

import java.text.DecimalFormat;

public class Path {
    private int[] order;        // array of ints for the cycle, but omitting the repeated initial point.
    private double distance;    // Length of path.

    /**
     * Packages the order and distance
     * @param order - array of ints for the cycle, but omitting the repeated initial point.
     * @param distance - Length of path.
     */
    public Path(int[] order, double distance) {
        this.order = order;
        this.distance = distance;
    }

    /**
     * @return the length of the path
     */
    public double getDistance() {
        return distance;
    }

    /**
     * @return the string
     */
    public String getPath() {
        String result = "";
        for(int i = 0; i < order.length; i++) {
            result += order[i] + " ";
        }
        result += order[0] + " ";

        return result;
    }

    /**
     *
     * @param other - Path to compare to.
     * @return whether the called path is equal to the other path.
     */
    public boolean equals(Path other) {
        if(this.order.length != other.order.length) {
            return false;
        } else {
            for(int i = 0; i < this.order.length; i++){
                if(this.order[i] != other.order[i]) {
                    return false;
                }
            }

            return true;
        }
    }

    /**
     * @return the string representation of the path as laid out in the writeup
     */
    public String toString() {
        String result = "Path:";
        DecimalFormat df = new DecimalFormat("0.00");

        for(int i = 0; i < order.length; i++) {
            result += " " + order[i];
        }
        result += "  distance = " + df.format(distance);

        return result;
    }

    /**
     * @param n - the number of vertices in the graph
     * @param g - the graph
     * @return The first lexicographic path object for the class
     */
    public static Path firstLexPath(int n, Graph g) {
        int[] order = new int[n];
		double[] distances = new double[n];
		distances[0] = 0;
        for(int i = 1; i < n; i++){
            order[i] = i;
			// Children at 2*n + 1 and 2*n + 2
			int parent = (int) Math.floor((i - 1) / 2.);
            distances[i] = distances[parent] + g.distance(parent, i);
        }

		double distance = 0;
		for(int i = 0; i < n; i++) {
			if(distances[i] > distance)
				distance = distances[i];
		}

        return new Path(order, distance);
    }

    /**
     * @param p - the current lexicographic path
     * @param g - the graph
     * @return the next lexicographic path in the graph
     */
    public static Path nextLexPath(Path p, Graph g) {
        int[] newpath = new int[p.order.length];
        System.arraycopy(p.order, 0, newpath, 0, newpath.length);

        int n =  newpath.length - 1;

        int j = n - 1;
        while( newpath[j] >  newpath[j+1]) {
            j--;
            if (j <= 0) return null;
        }

        int k = n;
        while( newpath[j] >  newpath[k]) {
            k--;
        }

        int temp =  newpath[j];
         newpath[j] =  newpath[k];
         newpath[k] = temp;

        int r = n;
        int s = j + 1;
        while (r > s) {
            temp =  newpath[r];
             newpath[r] =  newpath[s];
             newpath[s] = temp;
            r--;
            s++;
        }

        // Now that we've got the new path, get the distance.
        double distance = 0.;
        double[] distances = new double[newpath.length];
		distances[0] = 0;
		for(int i = 1; i <  newpath.length; i++) {
			int parent = (int) Math.floor((i - 1) / 2.);
            distances[i] = distances[parent] + g.distance(parent, i);
        }

		for(int i = 0; i < n; i++) {
			if(distances[i] > distance)
				distance = distances[i];
		}

        return new Path(newpath, distance);
    }
}
