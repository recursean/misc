package LinkedLists;

import java.util.Stack;

public class Palindrome {
    boolean isPalindrome(LinkedList a) {
        LinkedList.Node curr = a.head;
        Stack<LinkedList.Node> s = new Stack<>();

        while(curr != null) {
            s.push(curr);

            curr = curr.next;
        }
        
        curr = a.head;
        
        while(curr != null) {
            if(curr.val != s.pop().val) {
                return false;
            }
            curr = curr.next;
        }

        return true;
    }
    public static void main(String[] args) {
        LinkedList list = new LinkedList();

        list.insert(1);
        list.insert(2);
        list.insert(3);
        list.insert(5);
        list.insert(3);
        list.insert(2);
        list.insert(1);

        System.out.println(new Palindrome().isPalindrome(list));
    }
}