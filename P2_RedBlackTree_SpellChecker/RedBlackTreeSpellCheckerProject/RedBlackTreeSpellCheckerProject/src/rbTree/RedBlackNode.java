//Author: Joseph Standerfer
//St_id: josephst
//Date: 1/31/2019

package rbTree;

public class RedBlackNode {
    public final static int RED = 1;
    public final static int BLACK = 2;

    private int color;
    private java.lang.String data;
    private RedBlackNode lc;
    private RedBlackNode rc;
    private RedBlackNode parent;

    public RedBlackNode(String data, int color, RedBlackNode p, RedBlackNode lc, RedBlackNode rc){
        if(color != RED && color != BLACK){
            throw new IllegalArgumentException("Invalid node color. Needs to be either 1(Red) or 2(Black)");
        } if(data != null){
            this.data = data;
        } else {
            this.data = "-1";
        }

        this.color = color;
        this.parent = p;
        this.lc = lc;
        this.rc = rc;
    }
    //returns the node color integer, RED or BLACK
    public int getColor(){
        return this.color;
    }
    //returns the data in the node
    public java.lang.String getData(){
        return this.data;
    }
    //returns the left child node of this RedBlackNode
    public RedBlackNode getLc(){
        return this.lc;
    }
    //returns the parent node of this RedBlackNode
    public RedBlackNode getP(){
        return this.parent;
    }
    //returns the right child of this RedBlackNode
    public RedBlackNode getRc(){
        return this.rc;
    }
    //sets node color by integer representing RED or BLACK
    public void setColor(int color){
        if(color == RED || color == BLACK){
            this.color = color;
        } else {
           throw new IllegalArgumentException("Invalid node color. Needs to be either 1(Red) or 2(Black)");
        }
    }
    //sets nodes data to the string value passed in
    public void setData(java.lang.String data){
        this.data = data;
    }
    //sets the left child node
    public void setLc(RedBlackNode lc){
        this.lc = lc;
    }
    //sets the parent node
    public void setP(RedBlackNode p){
        this.parent = p;
    }
    //set the right child node
    public void setRc(RedBlackNode rc){
        this.rc = rc;
    }
    //returns String value representing this nodes contents
    @Override
    public String toString() {
        return "[data = " + this.data +
                ":Color = " + (this.color == this.RED ? "Red" : "Black") +
                ":Parent = " + this.parent.getData() +
                ": LC = " + this.lc.getData() +
                ": RC = " + this.rc.getData() + ']';
    }
}
