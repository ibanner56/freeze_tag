import java.util.Collection;

/**
 * Created by Isaac on 4/12/2015.
 */
public class VHeap{

    private Point[] heap;   // Heap data structure
    private int N;          // Length of the heap

    // Constructors

    /**
     * Creates a heap with an initial capacity
     * @param capacity - Amount of memory to allocate for the heap
     */
    public VHeap(int capacity) {
        N = 0;
        heap = new Point[capacity + 1];
    }

    /**
     * Builds a heap with the contents of a collection and a capacity equal to the collection's size.
     * @param elements - collection to put onto a heap
     */
    public VHeap(Collection<Point> elements) {
        N = 0;
        heap = new Point[elements.size() + 1];
        for(Point v : elements) {
            insert(v);
        }
    }

    // Public Methods

    /**
     * Inserts a point into the heap
     * @param v - point to insert
     */
    public void insert(Point v) {
        heap[++N] = v;
        swim(N);
    }

    /**
     * @return and remove the top element on the heap.
     */
    public Point pop() {
        Point max = heap[1];
        swap(1, N--);
        sink(1);
        heap[N + 1] = null;
		return max;
    }

    /**
     * Takes an invalid heap and makes it whole once again.
     * @param h - the heap to -ify.
     */
    public static void reheapify(VHeap h) {
		Point[] a = h.heap;
        int N = h.N;
        
		for(int k = N/2; k >= 1; k--) {
			int j = k*2;
			if(less(a, k, j)) {
				if((j + 1) <= N && less(a, j, j+1)){
					swap(a, k, j + 1);
				}
				else {
					swap(a, k, j);	
				}
			}
		}
    }

    /**
     * @return if there are no additional elements in the heap.
     */
    public boolean isEmpty() {
        return N == 0;
    }

    /**
     * @return a string representation of the internal array.
     */
	public String toString() {
		String result = "";
		for(int i = 0; i < N + 1; i++){
			result += heap[i] + "   ";
		}
		return result + "\n";
	}

    // Private Methods

    /**
     * Floats the element at k up the heap
     * @param k - index to swim
     */
    private void swim(int k) {
        while(k > 1 && less(k/2, k)) {
            swap(k, k/2);
            k = k/2;
        }
    }

    /**
     * Drops the element at k down the heap
     * @param k - index to sink
     */
    private void sink(int k) {
        while(2*k <= N) {
            int j = 2*k;
            if (j < N && less(j, j+1)) j++;
            if (!less(k, j)) break;
            swap(k, j);
            k = j;
        }
    }

    /**
     * Static sink
     * @param a - heap array
     * @param k - index to sink
     * @param N - number of elements in the heap
     */
    private static void sink(Point[] a, int k, int N) {
        while(2*k <= N) {
            int j = 2*k;
            if (j < N && less(a, j, j+1)) j++;
            if (!less(a, k, j)) break;
            swap(a, k, j);
            k = j;
        }
    }

    /**
     * @param i - index in the heap
     * @param j - index in the heap
     * @return whether i is GREATER than j
     */
    private boolean less(int i, int j) {
        return heap[i].compareTo(heap[j]) > 0;
    }

    /**
     * Static less
     * @param a - array to look at
     * @param i - index in array
     * @param j - index in array
     * @return - whether i is GREATER than j
     */
    private static boolean less(Point[] a, int i, int j) {
        return a[i].compareTo(a[j]) > 0;
    }

    /**
     * Jake Woods swaps your a's and b's
     * @param a - thing to swap
     * @param b - other thing to swap
     */
    private void swap(int a, int b) {
        Point temp = heap[a];
        heap[a] = heap[b];
        heap[b] = temp;
    }

    /**
     * Static swap
     * @param elements - array to fuss with
     * @param i - thing to swap
     * @param j - other thing to swap
     */
    private static void swap(Point[] elements, int i, int j) {
        Point temp = elements[i];
        elements[i] = elements[j];
        elements[j] = temp;
    }

}
