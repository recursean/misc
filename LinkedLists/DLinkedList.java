package LinkedLists;

public class DLinkedList {
    Node head;
    Node tail;
    
    public class Node {
        Node prev;
        Node next;
        int val;

        public Node(int val) {
            this.val = val;
        }
    }

    // Insert new element at end of list
    public Node insert(int val) {
        Node n = new Node(val);

        if(head == null) {
            head = n;
            tail = head;
        }
        else {
            tail.next = n;
            n.prev = tail;
            tail = n;
        }

        return n;
    }

    // Search list for value
    public Node get(int val) {
        Node n = head;
        while(n != null) {
            if(n.val == val) {
                return n;
            }
            else {
                n = n.next;
            }
        }

        return null;
    }

    // Delete node with value
    public boolean delete(int val) {
        if(head == null) {
            return false;
        }
        
        Node n = head;
        if(n.val == val) {
            if(tail == n) {
                head = null;
                tail = null;
            }
            else {
                head = head.next;
                head.prev = null;
            }

            return true;
        }
        while(n.next != null && n.next.val != val) {
            n = n.next;
        }

        if(n.next != null && n.next.val == val) {
            if(n.next == tail) {
                n.next = null;
                tail = n;
            }
            else {
                n.next = n.next.next;
                n.next.prev = n;
            }
            
            return true;
        }
        else {
            return false;
        }
    }

    // Print out list
    public void traverse() {
        Node n = head;
        while(n != null) {
            System.out.print("(" + n.val + ") --> ");
            n = n.next;
        }
    }

    // Print the list in reverse order
    public void traverseReverse() {
        Node n = tail;
        while(n != null) {
            System.out.print(" <-- (" + n.val + ")");
            n = n.prev;
        }
    }
}