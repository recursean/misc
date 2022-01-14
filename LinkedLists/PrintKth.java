package LinkedLists;

public class PrintKth {
    // Print the kth-to-last element
    public int printKth(int k, LinkedList.Node n, LinkedList list) {
        if(n == list.tail) {
            return 1;
        }

        int current = printKth(k, n.next, list) + 1;

        if(current == k) {
            System.out.println(n.val);
        }

        return current;
    }
    public static void main(String[] args) {
        LinkedList list = new LinkedList();

        list.insert(1);
        list.insert(2);
        list.insert(3);
        list.insert(2);
        list.insert(5);
        list.insert(6);
        list.insert(5);
        list.insert(8);
        list.insert(5);

        new PrintKth().printKth(3, list.head, list);
    }
}