package LinkedLists;

import LinkedLists.LinkedList;

public class Test {

    public static void main(String[] args) {
        LinkedList list = new LinkedList();

        LinkedList.Node n1 = list.insert(1);
        LinkedList.Node n2 = list.insert(2);
        LinkedList.Node n3 = list.insert(3);
        LinkedList.Node n4 = list.insert(4);
        LinkedList.Node n5 = list.insert(5);
        LinkedList.Node n6 = list.insert(6);
        LinkedList.Node n7 = list.insert(7);
        LinkedList.Node n8 = list.insert(8);
        LinkedList.Node n9 = list.insert(9);

        list.traverse();
        
        list.delete(n5);
        list.delete(n1);
        list.delete(n9);
        
        list.traverse();
        list.traverseReverse();
    }
}