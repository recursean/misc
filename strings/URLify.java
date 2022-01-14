public class URLify {
    String urlify(String s, int trueLen) {
        char[] sc = s.toCharArray();

        int end = s.length() - 1;
        int curr = end;

        while(sc[curr] == ' ') {
            curr--;
        }

        while(curr >= 0) {
            if(sc[curr] != ' ') {
                sc[end--] = sc[curr--];
            }
            else {
                sc[end--] = '0';
                sc[end--] = '2';
                sc[end--] = '%';

                curr--;
            }
        }

        return new String(sc);
    }

    public static void main(String[] args) {
        String s = "Mr John Smith    ";
        int trueLen = 13;

        System.out.println(new URLify().urlify(s, trueLen));
    }
}