import java.util.Comparator;
import java.util.Random;

/**
 * Created by Isaac on 3/12/2015.
 * I'm using some really awful generics magic to make this compile without
 * warnings. On the plus side, my quicksorter can operate on any set of
 * a type that is comparable with itself. Woo modular coding.
 */
public class Quicksortr {

	/**
	 * The sorting inlet. All requests must go through here.
	 * Starts the quicksort algorithm on the passed in array.
	 *
	 * @param elements - Array of Comparable objects to be sorted.
	 */ 
    public static <T extends Comparable<T>> void sort(T[] elements) {
        quickSort(elements, 0, elements.length - 1);
    }

    /**
     * The sorting inlet with a comparator
     * Starts the quicksort on the passed array
     *
     * @param elements - Array of objects to be sorted
     * @param comparator - Comparator for those objects
     */
    public static <T extends Comparable<T>> void sort(T[] elements, Comparator<T> comparator) {
        quickSort(elements, 0, elements.length - 1, comparator);
    }

	/**
	 * @param elements - Array to be sorted.
	 * @param p, r - Start and end of the interval being quick'ed.
	 */ 
    private static <T extends Comparable<T>> void quickSort(T[] elements, int p, int r) {
        if(p < r)
        {
            int q=partition(elements, p, r);
            quickSort(elements, p, q);
            quickSort(elements, q+1, r);
        }
    }

    /**
     * @param elements - Array to be sorted
     * @param p, r - Start and end of the interval being quick'ed
     * @param comparator - Comparator to use to sort
     */
    private static <T extends Comparable<T>> void quickSort(T[] elements, int p, int r, Comparator<T> comparator) {
        if(p < r)
        {
            int q = partition(elements, p, r, comparator);
            quickSort(elements, p, q, comparator);
            quickSort(elements, q+1, r, comparator);
        }
    }

	/**
	 * Partition algorithm lovingly bastardized to work on Comparables 
	 * instead of on ints.
	 * @param elements - Array in question.
	 * @param p, r - Start and end of the interval.
	 */ 
    private static <T extends Comparable<T>> int partition(T[] elements, int p, int r) {
        Random rand = new Random();
        int pivot = p + rand.nextInt(r - p + 1);

        T a = elements[pivot];
        int i = p - 1;
        int j = r + 1;

        while (true) {
            i++;
            while ( i < r && elements[i].compareTo(a) < 0)
                i++;
            j--;
            while (j > p && elements[j].compareTo(a) > 0)
                j--;

            if (i < j)
                swap(elements, i, j);
            else
                return j;
        }
    }

    /**
     * Partition algorithm lovingly bastardized to work with Comparator.
     * @param elements - Array in question.
     * @param p, r - Start and end of the interval.
     * @param comparator - Comparator to leverage.
     */
    private static <T extends Comparable<T>> int partition(T[] elements, int p, int r, Comparator<T> comparator) {
        Random rand = new Random();
        int pivot = p + rand.nextInt(r - p + 1);

        T a = elements[pivot];
        int i = p - 1;
        int j = r + 1;

        while (true) {
            i++;
            while ( i < r && comparator.compare(elements[i], a) < 0)
                i++;
            j--;
            while (j > p && comparator.compare(elements[j], a) > 0)
                j--;

            if (i < j)
                swap(elements, i, j);
            else
                return j;
        }
    }

	/**
	 * Basic swap function.
	 * @param elements - the array in question.
	 * @param i, j - the indices to swap.
	 */ 
    private static <T extends Comparable<T>> void swap(T[] elements, int i, int j) {
        T temp = elements[i];
        elements[i] = elements[j];
        elements[j] = temp;
    }

}
