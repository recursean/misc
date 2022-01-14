package LinkedLists;

// This doesn't work because LinkedList does not update tail if some other
// LinkedList that contains the same node, inserts at the end. The tail for
// the 2nd list will be accurate, but the first LinkedList will have a 
// stale pointer to old head.
public class Intersection {
    LinkedList.Node findIntersection(LinkedList a, LinkedList b) {
        if(a.tail != b.tail) {
            return null;
        }

        // count lists
        LinkedList.Node currA = a.head;
        LinkedList.Node currB = b.head;
        int lenA = 0;
        int lenB = 0;

        while(currA != null) {
            lenA++;
            currA = currA.next;
        }

        while(currB != null) {
            lenB++;
            currB = currB.next;
        }

        currA = a.head;
        currB = b.head;

        if(lenA > lenB) {
            for(int i = 0; i < lenA - lenB; i++) {
                currA = currA.next;
            }
        }
        else if(lenA < lenB) {
            for(int i = 0; i < lenB - lenA; i++) {
                currB = currB.next;
            }
        }

        while(currA != currB) {
            currA = currA.next;
            currB = currB.next;
        }

        return currA;
    }
    public static void main(String[] args) {
        LinkedList list1 = new LinkedList();
        LinkedList list2 = new LinkedList();

        list1.insert(7);
        list1.insert(1);
        list1.insert(6);
        LinkedList.Node n = list1.insert(6);
        list1.insert(5);
        list1.insert(4);
        list1.insert(3);

        list2.insert(5);
        list2.insert(9);
        list2.insert(n);
        list2.insert(2);
        list2.insert(1);

        list1.traverse();
        list2.traverse();
        
        LinkedList.Node intersector = new Intersection().findIntersection(list1, list2);
        System.out.println(intersector.val);
    }
}