public class UniqueCharacters {

    // Determines if all of the characters in s are unique
    boolean isUnique(String s) {
        boolean arr[] = new boolean[128];

        for(char c: s.toCharArray()) {
            if(arr[c]) {
                return false;
            }
            else {
                arr[c] = true;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        String s = "howdy";
        System.out.println(new UniqueCharacters().isUnique(s));
    }
}