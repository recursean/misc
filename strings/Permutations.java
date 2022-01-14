public class Permutations {
    // Determines if Strings a and b are permutations of each other.
    boolean isPermutation(String a, String b) {
        if(a.length() != b.length()) {
            return false;
        }
        else {
            int counts[] = new int[128];

            for(char c: a.toCharArray()) {
                counts[c]++;
            }

            for(char c: b.toCharArray()) {
                counts[c]--;
            }

            for(int count: counts) {
                if(count != 0) {
                    return false;
                }
            }

            return true;
        }
    }

    // Determines if string is a permutation of a palindrome
    boolean isPalindromePermutation(String s) {
        boolean[] arr = new boolean[128];

        for(char c: s.toCharArray()) {
            arr[c] = !arr[c];
        }

        boolean foundOdd = false;

        for(boolean odd: arr) {
            if(odd) {
                if(foundOdd) {
                    return false;
                }
                else {
                    foundOdd = true;
                }
            }
        }

        return s.length() % 2 == 0 ? !foundOdd : foundOdd;
    }

    public static void main(String[] args) {
        String a = "abcdfdcbf";
        String b = "dcbd";

        // System.out.println(new Permutations().isPermutation(a, b));
        System.out.println(new Permutations().isPalindromePermutation(a));
    }
}