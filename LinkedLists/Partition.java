package LinkedLists;

public class Partition {
    LinkedList.Node partition(int val, LinkedList.Node head) {
        LinkedList.Node left = head;
        LinkedList.Node right = head;
        LinkedList.Node n = head;

        while(n != null) {
            LinkedList.Node next = n.next;

            if(n.val < val) {
                n.next = left;
                left = n;
            }
            else {
                right.next = n;
                right = n;
            }

            n = next;
        }

        right.next = null;

        return left;
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

        list.head = new Partition().partition(5, list.head);
        list.traverse();
    }
}