package LinkedLists;

public class SumLists {
    public LinkedList sumLists(LinkedList a, LinkedList b) {
        LinkedList sumList = new LinkedList();

        LinkedList.Node currA = a.head;
        LinkedList.Node currB = b.head;

        int carry = 0;
        int sum = 0;

        while(currA != null || currB != null) {
            if(currA != null) {
                if(currB != null) {
                    sum = currA.val + currB.val;
                    currB = currB.next;
                }
                else {
                    sum = currA.val;
                }
                currA = currA.next;
            }
            else {
                sum = currB.val;
                currB = currB.next;
            }

            sum += carry;
            carry = sum / 10;

            sumList.insert(sumList.new Node(sum % 10).val);
        }

        return sumList;
    }
    public static void main(String[] args) {
        LinkedList list1 = new LinkedList();
        LinkedList list2 = new LinkedList();

        list1.insert(7);
        list1.insert(1);
        list1.insert(6);

        list2.insert(5);
        list2.insert(9);
        list2.insert(2);

        LinkedList sumList = new SumLists().sumLists(list1, list2);
        sumList.traverse();
    }
}