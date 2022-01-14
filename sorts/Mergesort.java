class Mergesort {
    int aux[];

    // container method
    void mergesort(int[] arr) {
        aux = new int[arr.length];

        mergesort(arr, 0, arr.length - 1);
    }

    // divide and conquer
    void mergesort(int[] arr, int low, int high) {
        // base case
        if(low >= high) {
            return;
        }

        // calculate middle index
        int mid = low + (high - low) / 2;

        // sort left half
        mergesort(arr, low, mid);

        // sort right half
        mergesort(arr, mid+1, high);

        // merge
        merge(arr, low, mid, high);
    }

    // merge in sorted order arr[low..mid] and arr[mid+1..high]
    void merge(int[] arr, int low, int mid, int high) {
        int i = low;
        int j = mid + 1;

        // copy to aux array
        for(int k = low; k <= high; k++) {
            aux[k] = arr[k];
        }

        // put back to arr in sorted order
        for(int k = low; k <= high; k++) {
            if(i > mid) {
                arr[k] = aux[j++];
            }
            else if(j > high) {
                arr[k] = aux[i++];
            }
            else if(aux[i] < aux[j]) {
                arr[k] = aux[i++];
            }
            else {
                arr[k] = aux[j++];
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = {5,3,7,1,8,2,9,4,6};

        new Mergesort().mergesort(arr);

        for(int num: arr) {
            System.out.println(num);
        }
    }
}