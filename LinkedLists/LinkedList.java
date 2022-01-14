package LinkedLists;

public class LinkedList {
    Node head;
    Node tail;

    public class Node {
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
            tail = n;
        }

        return n;
    }

    // Insert new element at end of list
    public Node insert(Node n) {
        if(head == null) {
            head = n;
            tail = head;
        }
        else {
            tail.next = n;
            tail = n;
        }

        // this will cause an infinite loop if a node
        // already existing in this list gets added again.
        // commenting out for purposes of LoopDetection.java
        // while(tail.next != null) {
        //     tail = tail.next;
        // }

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
    public boolean delete(Node toDel) {
        if(head == null) {
            return false;
        }
        
        Node n = head;
        if(n == toDel) {
            if(tail == n) {
                head = null;
                tail = null;
            }
            else {
                head = head.next;
            }

            return true;
        }
        while(n.next != null && n.next != toDel) {
            n = n.next;
        }

        if(n.next != null && n.next == toDel) {
            if(n.next == tail) {
                n.next = null;
                tail = n;
            }
            else {
                n.next = n.next.next;
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
        System.out.println();
    }

    // Print the list in reverse order
    public void traverseReverse() {
        if(tail != null) {
            Node current = tail;

            while(current != head) {
                Node left = head;

                while(left.next != current) {
                    left = left.next;
                }

                System.out.print(" <-- (" + current.val + ")");
                current = left;
            }

            System.out.println(" <-- (" + current.val + ")");
        }
    }
}