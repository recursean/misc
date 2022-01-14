package Trees;

public class Test {
    public static void main(String[] args) {
        BinarySearchTree bst = new BinarySearchTree();

        bst.insert(5);
        bst.insert(2);
        bst.insert(6);
        bst.insert(1);

        bst.preorder();
        bst.inorder();
        bst.postorder();

        BinarySearchTreeNode n = bst.get(1);
        System.out.println(n.val);

        bst.delete(2);
        bst.preorder();

        bst.breadthFirstSearch();
    }
}