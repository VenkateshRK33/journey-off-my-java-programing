
import java.util.*;


public class mergesort {
    
    static final int MAX = 10005;
    static int[] a = new int[MAX];

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter Max array size: ");
        int n = input.nextInt();
        Random random = new Random();
        System.out.println("Enter the array elements: ");
        
        for (int i = 0; i < n; i++) {
            // a[i] = input.nextInt(); // for keyboard entry
            a[i] = random.nextInt(1000); // generate random numbers - uniform distribution
        }

        long startTime = System.nanoTime();
        MergeSortAlgorithm(0, n - 1);
        long stopTime = System.nanoTime();

        long elapsedTime = stopTime - startTime;
        System.out.println("Time Complexity (ms) for n= " + n + " is: " + (double) elapsedTime / 1000000);

        System.out.println("Sorted Array (Merge Sort):");
        for (int i = 0; i < n; i++) {
            System.out.print(a[i] + " ");
        }
        input.close();
    }

    public static void MergeSortAlgorithm(int low, int high) {
        int mid;
        if (low < high) {
            mid = (low + high) / 2;
            MergeSortAlgorithm(low, mid);
            MergeSortAlgorithm(mid + 1, high);
            Merge(low, mid, high);
        }
    }

    public static void Merge(int low, int mid, int high) {
        int[] b = new int[MAX];
        int i, j, k;
        i = low;
        j = mid + 1;
        k = low;

        while (i <= mid && j <= high) {
            if (a[i] < a[j]) {
                b[k++] = a[i++];
            } else {
                b[k++] = a[j++];
            }
        }

        if (i > mid) {
            for (; j <= high; j++) {
                b[k++] = a[j];
            }
        } else {
            for (; i <= mid; i++) {
                b[k++] = a[i];
            }
        }

        for (k = low; k <= high; k++) {
            a[k] = b[k];
        }
    }
}


