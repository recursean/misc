class Quicksort {

    void quicksort(int[] arr) {
        quicksort(arr, 0, arr.length - 1);
    }

    void quicksort(int[] arr, int low, int high) {
        if(low >= high) {
            return;
        }

        int pivot = partition(arr, low, high);
        quicksort(arr, low, pivot - 1);
        quicksort(arr, pivot + 1, high);
    }

    int partition(int[] arr, int low, int high) {
        int i = low;
        int j = high + 1;
        int pivot = arr[low];

        while(true) {
            while(arr[++i] <= pivot) {
                if(i == high) {
                    break;
                }
            }

            while(arr[--j] >= pivot) {
                if(j == low) {
                    break;
                }
            }

            if(i >= j) {
                break;
            }

            swap(arr, i, j);
        }

        swap(arr, low, j);

        return j;
    }

    void swap(int[] arr, int idx1, int idx2) {
        int tmp = arr[idx1];
        arr[idx1] = arr[idx2];
        arr[idx2] = tmp;
    }

    public static void main(String[] args) {
        int[] arr = {9,8,7,6,5,4,3,2,1};

        new Quicksort().quicksort(arr);

        for(int num: arr) {
            System.out.println(num);
        }
    }
}