class BinarySearchTree {
    class Node {
        Node left;
        Node right;
        int key;
        int value;
        int subtreeCount;

        public Node(int key, int value, int size) {
            this.key = key;
            this.value = value;
            this.subtreeCount = size;
        }
    }

    Node root;

    public int get(int key) {
        return get(root, key);
    }

    private int get(Node n, int key) {
        if(n == null) {
            // should return null
            return -1;
        }
        
        if(key < n.key) {
            return get(n.left, key);
        }
        else if(key > n.key) {
            return get(n.right, key);
        }
        else {
            return n.value;
        }
    }

    public Node put(int key, int value) {
        return put(root, key, value);
    }

    private Node put(Node n, int key, int value) {
        if(n == null) {
            return new Node(key, value, 1);
        }

        if(key < n.key) {
            n.left = put(n.left, key, value);
        }
        else if(key > n.key) {
            n.right = put(n.right, key, value);
        }
        else {
            n.value = value;
        }

        n.subtreeCount = size(n.left) + size(n.right) + 1;

        return n;
    }

    public int size() {
        return size(root);
    }

    private int size(Node n) {
        if(n == null) {
            return 0;
        }
        else {
            return n.subtreeCount;
        }
    }

    public int min() {
        return min(root).key;
    }

    private Node min(Node n) {
        if(n.left == null) {
            return n;
        }
            
        return min(n.left);
    }

    public int max() {
        return max(root).key;
    }

    private Node max(Node n) {
        if(n.right == null) {
            return n;
        }
            
        return max(n.right);
    }

    public Node deleteMin() {
        deleteMin(root);
    } 

    private Node deleteMin(Node n) {
        if(n.left == null) {
            return n.right;
        }
        
        n.left = deleteMin(n.left);

        n.subtreeCount = size(n.left) + size(n.right) + 1;

        return n;
    }

    public static void main(String[] args) {
        BinarySearchTree bst = new BinarySearchTree();

        Node root = bst.put(5,6);
        bst.root = root;

        bst.put(2,3);
        bst.put(9,10);
        bst.put(1,2);
        bst.put(7,8);
        bst.put(12,13);
        bst.put(4,5);
        bst.put(11,12);
        bst.put(22,23);
        bst.put(0,1);
        bst.put(6,7);
        bst.put(15,16);
        bst.put(42,43);
        bst.put(32,33);
        bst.put(21,22);
        bst.put(10,11);

        System.out.println(bst.get(12));
        System.out.println(bst.get(1));
        System.out.println(bst.get(7));
        System.out.println(bst.get(5));
        System.out.println(bst.get(-5)); //doesnt exist

        System.out.println(bst.min());
        System.out.println(bst.max());
    } 
}