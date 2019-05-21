//Author: Joseph Standerfer
//St_id: josephst
//Date: 1/31/2019

package rbTree;

public class RedBlackTree {

    // the nil node is used to represent all of the external nulls. Its color will always be black.
    // The parent pointer (p) in the root will point to this node and so will all the internal nodes
    // that would normally contain a left or right value of null.
    private RedBlackNode nil;  //node to pointed to for null node values
    private RedBlackNode root;  //tree root
    private int nodeCount = 0;  //number of nodes currently in tree
    private int recentCompCount = -1; // stores the number of nodes traversed on the most recent contains(v) call

    public RedBlackTree(){
        this.nil = new RedBlackNode(null, RedBlackNode.BLACK,null,null,null);
        this.root = this.nil;
    }
    // Pre-Post conditions:
        // Postcondition: returns and removes object in first position, first one that was added
    // Best and worst case runtimes for method below:
        // Big-Theta: Theta(1) => O(1), Omega(1)
    public int getSize(){
        return nodeCount;
    }
    // Performs an in-order traversal of the tree. The inOrderTraversal(RedBlackNode) method is recursive and
    // displays the content of the tree. It makes calls on System.out.println().
    // Pre-Post conditions:
        // Precondition: t is the root of the tree on the first call of this method
        // Postcondition: The value printed out is the ordered traversal of 
        //                  objects stored in the tree which root was passed. 
        //                  The tree will not be modified
    // Best and worst case runtimes for method below:
        // Big-Theta: Theta(n) => O(n), Omega(n)
    public void inOrderTraversal(RedBlackNode t){
        if(t == this.nil){
            return;
        }
        //use recursion to most to left node
        inOrderTraversal(t.getLc());
        //print node. Effectively prints the left-most nodes first
        System.out.println(t.toString());
        //use recursion on right node
        inOrderTraversal(t.getRc());
    }
    // calls the recursive inOrderTraversal(RedBlackNode) - passing the root.
    // Pre-Post conditions:
        // Postcondition: The value printed out is the ordered traversal of 
        //                  objects stored in the tree of the classes root
    // Best and worst case runtimes for method below:
        // Big-Theta: Theta(n) => O(n), Omega(n)
    public void inOrderTraversal(){
        inOrderTraversal(this.root);
    }
    // Performs a reverseOrder traversal of the tree. The reverseOrderTraversal(RedBlackNode) method is recursive and
    // displays the content of the tree in reverse order. This method would normally be private.
    // Pre-Post conditions:
        // Precondition: t is the root of the tree on the first call of this method
        // Postcondition: The value printed out is the reversed ordered traversal of 
        //                  objects stored in the tree. The tree will not be modified
    // Best and worst case runtimes for method below:
        // Big-Theta: Theta(n) => O(n), Omega(n)
    public void reverseOrderTraversal(RedBlackNode t){
        if(t == this.nil){
            return;
        }
        //use recursion to most to right node
        reverseOrderTraversal(t.getRc());
        //print node. Effectively prints the right-most nodes first
        System.out.println(t.toString());
        //use recursion on left node
        reverseOrderTraversal(t.getLc());
    }
    // calls the recursive reverseOrderTraversal(RedBlackNode) - passing the root.
    // Pre-Post conditions:
        // Postcondition: The value printed out is the reverse ordered traversal of 
        //                  objects stored in the tree of the classes root
    // Best and worst case runtimes for method below:
        // Big-Theta: Theta(n) => O(n), Omega(n)
    public void reverseOrderTraversal(){
        reverseOrderTraversal(this.root);
    }
    //places a data item into the tree.
    // Pre-Post conditions:
        // Precondition: value != null && value is an integer to be sorted
        // Postcondition: The value passed in will be stored in the appropriate
        //                  node given its order in the strings already stored
    // Best case runtimes for method below:
        // Big-Theta: theta(1) => o(1), omega(1)
    // Worst case runtimes for method below:
        // Big-Theta: Theta(log(n)) => O(log(n)), Omega(log(n))
    public void insert(java.lang.String value){
        RedBlackNode y = this.nil;
        RedBlackNode x = this.root;
        while (x != this.nil){
            y = x;
            if(value.compareTo(x.getData()) < 0){
                x = x.getLc();
            } else {
                x = x.getRc();
            }
        }
        RedBlackNode z = new RedBlackNode(value, RedBlackNode.RED, y, this.nil, this.nil);
        if(y == this.nil){  //if this is first item added to tree put it as the root
            this.root = z;
        } else {
            if(z.getData().compareTo(y.getData()) < 0){
                y.setLc(z);
            } else {
                y.setRc(z);
            }
        }
        this.nodeCount++;
        RBInsertFixup(z);
    }
    //rotate the tree left in order to balance it, in accordance with rbTree rules
    // Pre-Post conditions:
        // Precondition: x.getRc() == nil  . The left child of the root node passed cannot be nil
        //               root.getP() != nil  . The node passed to this method must be the root of the whole tree
        // Postcondition: The root node value and tree will be rotated to the left
    // Best and worst case runtimes for method below:
        // Big-Theta: Theta(1) => O(1), Omega(1)
    public void leftRotate(RedBlackNode x){
        if(x.getRc() == this.nil || this.root.getP() != this.nil){
            System.out.println("Error! Invalid tree geometry passed to left rotate method");
        }
        RedBlackNode y = x.getRc();     // y now points to node to right of x
        x.setRc(y.getLc());     // y's left subtree becomes x's right subtree
        y.getLc().setP(x);      // left subtree of y gets a new parent
        y.setP(x.getP());       // y's parent is now x's parent

        // if x is at root then y becomes new root
        if(x.getP() == this.nil){
            this.root = y;
        } else {  // if x is a left child then adjust x's parent's right child or...
            if(x == x.getP().getLc()){
                x.getP().setLc(y);
            } else {  // adjust x's parent's left child
                x.getP().setRc(y);
            }
        }
        // the right child of y is now x
        y.setLc(x);
        // the parent of x is now y
        x.setP(y);
    }
    //rotate the tree right in order to balance it, in accordance with rbTree rules
    // Pre-Post conditions:
        // Precondition: x.getLc() == nil  . The left child of the root node passed cannot be nil
        //               root.getP() != nil  . The node passed to this method must be the root of the whole tree
        // Postcondition: The root node value and tree will be rotated to the right
    // Best and worst case runtimes for method below:
        // Big-Theta: Theta(1) => O(1), Omega(1)
    public void rightRotate(RedBlackNode x){
        //preconditions check
        if(x.getLc() == this.nil || this.root.getP() != this.nil){
            System.out.println("Error! Invalid tree geometry passed to left rotate method");
        }
        RedBlackNode y = x.getLc();     // y now points to node to left of x
        x.setLc(y.getRc());         // y's right subtree becomes x's left subtree
        y.getRc().setP(x);      // right subtree of y gets a new parent
        y.setP(x.getP());       // y's parent is now x's parent

        // if x is at root then y becomes new root
        if(x.getP() == this.nil){
            this.root = y;
        } else {   // if x is a left child then adjust x's parent's left child or...
            if(x == x.getP().getLc()){
                x.getP().setLc(y);
            } else { // adjust x's parent's right child
                x.getP().setRc(y);
            }
        }
        // the right child of y is now x
        y.setRc(x);
        // the parent of x is now y
        x.setP(y);
    }
    //restructures the tree so that Red Black Properties are preserved.
    // Pre-Post conditions:
        // Precondition: Z != null
        // Postcondition: The colors and node arrangement will be rearranged in accordance
        //                 with rbTree rules.
    // Best case runtimes for method below: (nothing to fix)
        // Big-Theta: theta(1) => o(1), omega(1)
    // Worst case runtimes for method below:
        // Big-Theta: Theta(log(n)) => O(log(n)), Omega(log(n))
    public void RBInsertFixup(RedBlackNode z){
        while(z.getP().getColor() == RedBlackNode.RED){
            if(z.getP() == z.getP().getP().getLc()) {
                RedBlackNode y = z.getP().getP().getRc();
                if( y.getColor() == RedBlackNode.RED){
                    z.getP().setColor(RedBlackNode.BLACK);
                    y.setColor(RedBlackNode.BLACK);
                    z.getP().getP().setColor(RedBlackNode.RED);
                    z = z.getP().getP();
                } else {
                    if(z == z.getP().getRc()){
                        z = z.getP();
                        leftRotate(z);
                    }
                    z.getP().setColor(RedBlackNode.BLACK);
                    z.getP().getP().setColor(RedBlackNode.RED);
                    rightRotate(z.getP().getP());
                }
            } else {
                RedBlackNode y = z.getP().getP().getLc();
                if(y.getColor() == RedBlackNode.RED){
                    z.getP().setColor(RedBlackNode.BLACK);
                    y.setColor(RedBlackNode.BLACK);
                    z.getP().getP().setColor(RedBlackNode.RED);
                    z = z.getP().getP();
                } else {
                    if(z == z.getP().getLc()){
                        z = z.getP();
                        rightRotate(z);
                    }
                    z.getP().setColor(RedBlackNode.BLACK);
                    z.getP().getP().setColor(RedBlackNode.RED);
                    leftRotate(z.getP().getP());
                }
            }
        }
        this.root.setColor(RedBlackNode.BLACK);
    }
    //Returns true if v is in the tree, false otherwise;
    // Pre-Post conditions:
        // Postcondition: Returns boolean answer to whether the tree contains value v
    // Best case runtimes for method below:
        // Big-Theta: theta(1) => o(1), omega(1)
    // Worst case runtimes for method below:
        // Big-Theta: Theta(log(n)) => O(log(n)), Omega(log(n))
    public boolean contains(java.lang.String v){
        this.recentCompCount = 0;  //reset the search counter
        RedBlackNode x = this.root;
        //iterate through tree until we hit a leaf
        while(x != this.nil){
            this.recentCompCount++;
            int comp = v.compareTo(x.getData());
            if(comp == 0){
                return true;  // if node value is equal to v return true
            } else if(comp < 0){
                x = x.getLc(); // if value is less than node value move to left child node
            } else{
                x = x.getRc(); // if value is greater than node value move to right child node
            }
        }
        return false;  //return false if value not found
    }
    //Returns number of comparisons made in last call on the contains method.
    // Pre-Post conditions:
        // Postcondition: The int returned is the number of comparisons made in 
        //                  last call on the contains method. Returns -1 if contains 
        //                  method has not been called before
    public int getRecentCompares(){
        return recentCompCount;
    }
    //The method closeBy(v) returns a value close to v in the tree (most consecutive characters in common)
    // Pre-Post conditions:
        // Postcondition: Returns the value with the most number of characters in common with string v
    // Best case runtimes for method below: (will always run through entire tree because value isn't stored)
        // Big-Theta: Theta(log(n)) => O(log(n)), Omega(log(n))
    // Worst case runtimes for method below:
        // Big-Theta: Theta(log(n)) => O(log(n)), Omega(log(n))
    public java.lang.String closeBy(java.lang.String v){
        char[] vChars = v.toCharArray();   //store characters of serach term
        char[] closestValChars = this.root.getData().toCharArray();  //saves chars of closest value found
        int matchingChars = 0;
        RedBlackNode x = this.root;  //creates node referance to be iterated through
        while(x != this.nil){
            char[] xChars = x.getData().toCharArray();
            //compare the number of initial matching characters between strings.
            //if number is greater than previous closest value then replace
            int shortestLength = Math.min(vChars.length, xChars.length);
            for(int i = 0; i < shortestLength; i++){
                //check for closest value replacement at last matching character or at the last iteration of the loop
                if(vChars[i] != xChars[i] || ((vChars[i] == xChars[i] && i == shortestLength-1))){
                    //if matching chars is greater or equal (closer in compare below) then swap closest value char array
                    if(i >= matchingChars){
                        matchingChars = i;
                        closestValChars = xChars;
                    }
                    break;
                }
            }
            // compare strings to find which node to move to next
            int comp = v.compareTo(x.getData());
            if(comp == 0){
                return x.getData();  // if node value is equal to v return v
            } else if(comp < 0){
                x = x.getLc(); // if value is less than node value move to left child node
            } else{
                x = x.getRc(); // if value is greater than node value move to right child node
            }
        }
        return String.valueOf(closestValChars);
    }
    // This a recursive routine that is used to compute the height of the red black tree.
    // The height() method passes the root of the tree to this method.
    // Pre-Post conditions:
        // Precondition: t != nil && t is root of tree on first iteration
        // Postcondition: returns height of tree, or -1 if nil node is passed
    // Worst and best case runtimes for method below: (goes through every node regardless)
        // Big-Theta: Theta(n) => O(n), Omega(n)
    public int height(RedBlackNode t){
        if(t == this.nil){
            return -1;
        }
        int leftHeight = height(t.getLc());  // find height of the left child tree
        int rightHeight = height(t.getRc()); // find height of the right child tree

        if(leftHeight > rightHeight){ // keep the larger of the two child trees
            return leftHeight + 1;  //add one for this node to the height returned
        } else {
            return rightHeight + 1;
        }
    }
    // Passes root of tree to recursive method above to find height of tree
    // Pre-Post conditions:
        // Precondition: root != nil
        // Postcondition: returns height of tree, or -1 if root is nil
    // Worst and best case runtimes for method below: (goes through every node regardless)
        // Big-Theta: Theta(n) => O(n), Omega(n)
    public int height(){
        return height(this.root);
    }
    // This method displays the RedBlackTree in level order. It first displays the root.
    // Then it displays all children of the root. Then it displays all nodes on level 3 and so on.
    // It is not recursive. It uses a queue.
    // Pre-Post conditions:
        // Postcondition: displays the contents of the RedBlackTree in level order
    // Worst and best case runtimes for method below: (goes through every node regardless)
        // Big-Theta: Theta(n) => O(n), Omega(n)
    public void levelOrderTraversal(){
        Queue q =  new Queue();
        // add the root node to the queue
        q.enQueue(this.root);

        //for each node in the queue add the right and left nodes to the que unless they are null
        while(!q.isEmpty()) {
            RedBlackNode rbn = (RedBlackNode) q.deQueue();
            System.out.println(rbn);
            //add left node to queue if not null
            if (rbn.getLc() != this.nil) {
                q.enQueue(rbn.getLc());
            }
            //add right node to queue if not null
            if (rbn.getRc() != this.nil) {
                q.enQueue(rbn.getRc());
            }
        }
    }
    //uses iterative method below to find diameter of the classes tree by passing the classes root node
    // Pre-Post conditions:
        // Postcondition: returns the diameter of the redBlackTree
    // Worst and best case runtimes for method below: (goes through every node regardless)
        // Big-Theta: Theta(n) => O(n), Omega(n)
    public int treeDiameter(){
        return treeDiameter(this.root);
    }
    //iterates through every branch of the tree to find the longe path of nodes, the diameter
    // Pre-Post conditions:
        // Precondition: t != nil && t is root of tree on first iteration
        // Postcondition: returns the diameter of the redBlackTree
    // Worst and best case runtimes for method below: (goes through every node regardless)
        // Big-Theta: Theta(n) => O(n), Omega(n)
    public int treeDiameter(RedBlackNode t){
        if(t == this.nil){
            return 0;
        }
        int ld = treeDiameter(t.getLc());  //diameter of the left nodes tree
        int rd = treeDiameter(t.getLc());  //diameter of the right nodes tree
        int maxSideDiameter = Math.max(ld, rd);  //keep the length of the larger side tree

        //finds current nodes tree diameter and uses it to determine whether it has a larger 
        //diameter or a child node does
        int lheight = height(t.getLc());   //finds left height of current node
        int rheight = height(t.getRc());   //finds right height of current node
        int currDiameter = lheight + rheight + 2;
            
        // keeps the largest of the three diameters found
        return Math.max(currDiameter, maxSideDiameter);
    }
    //tests the rbtree implementation to see if it works as expected
    // Worst and best case runtimes for method below: (goes through every node regardless)
        // Big-Theta: Theta(1) => O(1), Omega(1)
    public static void main(String[] args){
        RedBlackTree rbt = new RedBlackTree();

        for(int j = 1; j <= 5; j++) rbt.insert(""+j);

        // after 1..5 are inserted
        System.out.println("RBT in order");
        rbt.inOrderTraversal();
        System.out.println("RBT level order");
        rbt.levelOrderTraversal();

        // is 3 in the tree
        if(rbt.contains(""+3)) System.out.println("Found 3");
        else System.out.println("No 3 found");

        // display the height
        System.out.println("The height is " + rbt.height());
    }
}
