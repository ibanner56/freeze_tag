import java.util.LinkedList;

/**
 * It's a point. What else do you want from me?
 *
 * Created by Isaac on 2/7/2015.
 */

public class Point implements Comparable<Point> {
    public final int x, y;                  // X and Y
    public final int i;                     // The index of the point

    public double priority;                 // The MST vertex priority
    public int pVert;                       // Parent vertex in the MST.
    public LinkedList<Integer> children;    // Children in the MST.
    public static final double d = 0.000005;

    /**
     * Makes a point
     *
     * @param x - the x coordinate
     * @param y - the y coordinate
     */
    public Point(int x, int y, int i) {
        this.x = x;
        this.y = y;
        this.i = i;
        priority = Integer.MAX_VALUE;
        pVert = -1;
        children = new LinkedList<Integer>();
    }

    /**
     * Sets the point's new priority value.
     * @param newP - the new priority
     * @param pVert - the parent vertex
     */
    public void setPriority(double newP, int pVert) {
        this.priority = newP;
        this.pVert = pVert;
    }

	public void resetPriority() {
		this.priority = Integer.MAX_VALUE;
		this.pVert = -1;
	}

    /**
     * Adds an mst child to this point
     * @param p - child to add
     */
    public void addChild(Point p) {
        children.add(p.i);
    }

    @Override
    public int compareTo(Point o) {
        if (this.priority < o.priority - d) return -1;
        else if (this.priority - d > o.priority) return 1;
        else return 0;
    }

    /**
     * @return the string representation of the point as given in the writeup.
     */
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    /**
     * @param p - a point
     * @param q - another point
     * @return the distance from p to q
     */
    public static double distance(Point p, Point q) {
        double x1 = p.x;
        double y1 = p.y;
        double x2 = q.x;
        double y2 = q.y;

        return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
    }

}
