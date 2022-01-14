package LinkedLists;

import LinkedLists.LinkedList;

public class RemoveDuplicates {
    public void removeDuplicates(LinkedList list) {
        if(list.head == null || list.head == list.tail) {
            return;
        }
        LinkedList.Node target = list.head;
        

        while(target != null) {
            LinkedList.Node current = target;

            while(current.next != null) {
                if(target.val == current.next.val) {
                    current.next = current.next.next;
                }
                else {
                    current = current.next;
                }
            }
            
            target = target.next;
        }
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

        new RemoveDuplicates().removeDuplicates(list);

        list.traverse();
    } 
}