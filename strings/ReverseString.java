public class ReverseString {
    public String reverseString(String s) {
        char[] arr = s.toCharArray();

        for(int i = 0; i < arr.length / 2; i++) {
            char tmp = arr[i];
            arr[i] = arr[arr.length - i - 1];
            arr[arr.length - i - 1] = tmp;
        }

        return new String(arr);
    }
    public static void main(String[] args) {
        String nasty = "nasty";
        String nast = "nast";

        System.out.println(new ReverseString().reverseString(nasty));
        System.out.println(new ReverseString().reverseString(nast));
    }
}