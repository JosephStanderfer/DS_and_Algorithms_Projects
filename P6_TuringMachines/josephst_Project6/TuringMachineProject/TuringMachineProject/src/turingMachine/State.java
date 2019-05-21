//Author: Joseph Standerfer
//St_id: josephst
//Date: 4/23/2019

package turingMachine;

public class State {
    private RedBlackTree transitions = new RedBlackTree();

    public State(int transitions){
        //instantiate balanced tree to hold state transitions organized by activation letter
        this.transitions = new RedBlackTree();
    }
    //add transition to tree with letter activation as key
    public void addTransition(Transition t){
        if(t == null){
            throw new NullPointerException("Error. Transitions cannot be null");
        } else {
            transitions.insert(""+t.getInput(), t);
        }
    }
    public boolean hasTransition(char c){
        return transitions.contains(String.valueOf(c));
    }
    //retrieves Transition stored under the character key and returns it
    public Transition getTransition(char c){
        return transitions.lookup(String.valueOf(c));
    }
}
