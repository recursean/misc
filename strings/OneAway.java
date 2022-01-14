public class OneAway {
    boolean oneAway(String a, String b) {
        if(Math.abs(a.length() - b.length()) > 1) {
            return false;
        }
        else if(a.equals(b)) {
            return true;
        }
        else {
            int[] counts = new int[128];

            for(char c: a.toCharArray()) {
                counts[c]++;
            }

            for(char c: b.toCharArray()) {
                counts[c]--;
            }

            int found = 0;

            for(int count: counts) {
                if(count != 0) {
                    if(found > 1 || Math.abs(count) > 1) {
                        return false;
                    }
                    else {
                        found++;
                    }
                }
            }

            return true;
        }
    }
    public static void main(String[] args) {
        String a = "pale";
        String b = "bake";

        System.out.println(new OneAway().oneAway(a, b));
    }
}