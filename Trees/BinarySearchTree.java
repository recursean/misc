package Trees;

import java.util.Queue;
import java.util.LinkedList;

public class BinarySearchTree {
    BinarySearchTreeNode root;
    int count;

    public void insert(int val) {
        BinarySearchTreeNode n = new BinarySearchTreeNode(val);
        
        if(root == null) {
            root = n;
        }
        else {
            insert(n, root);    
        }

        count++;
    }

    private void insert(BinarySearchTreeNode n, BinarySearchTreeNode current) {
        if(n.val < current.val) {
            if(current.left == null) {
                current.left = n;
            }
            else {
                insert(n, current.left);
            }
        }
        else {
            if(current.right == null) {
                current.right = n;
            }
            else {
                insert(n, current.right);
            }
        }
    }

    public BinarySearchTreeNode get(int val) {
        return get(val, root);
    }

    private BinarySearchTreeNode get(int val, BinarySearchTreeNode current) {
        if(current == null) {
            return null;
        }

        if(val == current.val) {
            return current;
        }
        else if(val < current.val) {
            return get(val, current.left);
        }
        else {
            return get(val, current.left);
        }
    }

    public BinarySearchTreeNode getParent(int val) {
        return getParent(val, root);
    }

    private BinarySearchTreeNode getParent(int val, BinarySearchTreeNode current) {
        if(current == null) {
            return null;
        }
        
        if(val < current.val) {
            if(current.left == null) {
                return null;
            }
            else if(current.left.val == val) {
                return current;
            }
            else {
                return getParent(val, current.left);
            }
        }
        else if(val > current.val) {
            if(current.right == null) {
                return null;
            }
            else if(current.right.val == val) {
                return current;
            }
            else {
                return getParent(val, current.right);
            }
        }

        return null;
    }

    public boolean delete(int val) {
        BinarySearchTreeNode n = get(val);

        if(n == null) {
            return false;
        }
        else if(count == 1) {
            root = null;
            return true;
        }

        BinarySearchTreeNode parent = getParent(val);

        if(n.left == null && n.right == null) {
            if(n.val < parent.val) {
                parent.left = null;
            }
            else {
                parent.right = null;
            }
        }
        else if(n.left == null && n.right != null) {
            if(n.val < parent.val) {
                parent.left = n.right;
            }
            else {
                parent.right = n.right;
            }
        }
        else if(n.left != null && n.right == null) {
            if(n.val < parent.val) {
                parent.left = n.left;
            }
            else {
                parent.right = n.left;
            }
        }
        else {
            BinarySearchTreeNode large = n.right;

            while(large.right != null) {
                large = large.right;
            }

            BinarySearchTreeNode tmp = getParent(large.val);
            tmp.right = null;
            n.val = large.val;
        }

        count--;
        return true;
    }

    public void preorder() {
        System.out.print("PREORDER: ");
        preorder(root);
        System.out.println();
    }
    
    private void preorder(BinarySearchTreeNode n) {
        if(n == null) {
            return;
        }
        System.out.print(n.val + " ");
        preorder(n.left);
        preorder(n.right);
    }
    
    public void inorder() {
        System.out.print("INORDER: ");
        inorder(root);
        System.out.println();
    }
    
    private void inorder(BinarySearchTreeNode n) {
        if(n == null) {
            return;
        }
        inorder(n.left);
        System.out.print(n.val + " ");
        inorder(n.right);
    }
    
    public void postorder() {
        System.out.print("POSTORDER: ");
        postorder(root);
        System.out.println();
    }

    private void postorder(BinarySearchTreeNode n) {
        if(n == null) {
            return;
        }
        postorder(n.left);
        postorder(n.right);
        System.out.print(n.val + " ");
    }

    public void breadthFirstSearch() {
        Queue<BinarySearchTreeNode> q = new LinkedList<>();

        BinarySearchTreeNode n = root;
        System.out.print("BFS: ");
        
        while(n != null) {
            System.out.print(n.val + " ");
            
            if(n.left != null) {
                q.add(n.left);
            }
            if(n.right != null) {
                q.add(n.right);
            }
            
            if(!q.isEmpty()) {
                n = q.remove();
            }
            else {
                n = null;
            }
        }

        System.out.println();
    }

    public void depthFirstSearch() {
        depthFirstSearch(root);
    }

    private void depthFirstSearch(BinarySearchTreeNode n) {
        if(n == null) {
            return;
        }

        depthFirstSearch(n.right);
    }
}