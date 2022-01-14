public class StringCompression {
    public String stringCompression(String s) {
        int count = 0;
        StringBuilder sb = new StringBuilder();
        char last = ' ';

        for(char c: s.toCharArray()) {
            if(c != last && last != ' ') {
                sb.append(last);
                sb.append(count);

                count = 1;
            }
            else {
                count++;
            }

            last = c;
        }

        // add remaining
        sb.append(last);
        sb.append(count);

        return s.length() > sb.length() ? sb.toString() : s;
    }

    public static void main(String[] args) {
        String s = "aabcccccaaa";

        System.out.println(new StringCompression().stringCompression(s));
    }
}