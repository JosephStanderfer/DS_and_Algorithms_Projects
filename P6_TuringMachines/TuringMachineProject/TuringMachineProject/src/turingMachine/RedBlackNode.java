//Author: Joseph Standerfer
//St_id: josephst
//Date: 1/31/2019

package turingMachine;

public class RedBlackNode {
    public final static int RED = 1;
    public final static int BLACK = 2;

    private int color;
    private java.lang.String key;
    private Transition value;
    private RedBlackNode lc;
    private RedBlackNode rc;
    private RedBlackNode parent;

    public RedBlackNode(String key, Transition value, int color, RedBlackNode p, RedBlackNode lc, RedBlackNode rc){
        if(color != RED && color != BLACK){
            throw new IllegalArgumentException("Invalid node color. Needs to be either 1(Red) or 2(Black)");
        } if(key != null){
            this.key = key;
        } else {
            this.key = "-1";
        }
        this.value = value;
        this.color = color;
        this.parent = p;
        this.lc = lc;
        this.rc = rc;
    }
    //returns the node color integer, RED or BLACK
    public int getColor(){
        return this.color;
    }
    //returns the key in the node
    public java.lang.String getKey(){
        return this.key;
    }
    public Transition getValue(){
        return this.value;
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
    //sets nodes key to the string value passed in
    public void setKey(java.lang.String key){
        this.key = key;
    }
    public void setValue(Transition value){
        this.value = value;
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
        return "[key = " + this.key +
                ": value = " + this.value +
                ": Color = " + (this.color == this.RED ? "Red" : "Black") +
                ": Parent = " + this.parent.getKey() +
                ": LC = " + this.lc.getKey() +
                ": RC = " + this.rc.getKey() + ']';
    }
}
