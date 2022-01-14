package LinkedLists;

public class LoopDetection {
    public LinkedList.Node loopDetect(LinkedList a) {
        LinkedList.Node slow = a.head;
        LinkedList.Node fast = a.head;

        while(slow.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;

            if(slow == fast) {
                break;
            }
        }

        if(fast == null || fast.next == null) {
            return null;
        }

        slow = a.head;

        while(slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }

        return fast;
    }

    public static void main(String[] args) {
        LinkedList list = new LinkedList();

        list.insert(1);
        list.insert(2);
        LinkedList.Node n = list.insert(3);
        list.insert(4);
        list.insert(5);
        list.insert(n);

        LinkedList.Node loop = new LoopDetection().loopDetect(list);

        if(loop != null) {
            System.out.println(loop.val);
        }
    }
}