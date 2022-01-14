package LinkedLists;

public class RemoveMiddle {
    public void removeMiddle(LinkedList.Node n) {
        n.val = n.next.val;
        n.next = n.next.next;
    }

    public static void main(String[] args) {
        LinkedList list = new LinkedList();
    
        list.insert(1);
        list.insert(2);
        list.insert(3);
        list.insert(2);
        LinkedList.Node n5 = list.insert(5);
        list.insert(6);
        list.insert(5);
        list.insert(8);
        list.insert(5);
    
        new RemoveMiddle().removeMiddle(n5);
        list.traverse();
    }
}