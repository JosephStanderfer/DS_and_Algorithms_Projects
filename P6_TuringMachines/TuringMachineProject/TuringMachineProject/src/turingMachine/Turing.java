//Author: Joseph Standerfer
//St_id: josephst
//Date: 4/23/2019

package turingMachine;

public class Turing {
    private boolean[] finalStates; //allows users to set final states for turing machine
    private State[] states; // an array of turing machine states indexed by state number
    private int currentStateIdx; //the current state of the turing machine
    private int addedStates; //number of states that have been added, checked against initial declaration
    private char[] tape; //holds all the character on the tape
    private int readPosition; //stores the location of the read head within the tape


    public Turing(int n){
        this.states = new State[n];
        this.finalStates = new boolean[n];
        this.currentStateIdx = 0;
        this.addedStates = 0;
        this.tape = new char[100]; //initialize tape to 100 characters as specified
        this.readPosition = 0;
    }
    public void addState(State state) throws Exception{
        if(state == null){
            throw new NullPointerException("States cannot be null");
        } else if (this.addedStates >= this.states.length){
            throw new Exception("Error! The number of machine states added exceeds size initially specified");
        } else {
            this.states[this.addedStates] = state;
            this.addedStates++;
        }
    }
    public String execute(String tapeString){
        //add string to tape
        for(int i = 0; i < tape.length; i++){
            //store all input character in tape
            if(i < tapeString.length()) this.tape[i] = tapeString.charAt(i);
            else this.tape[i] = 'B'; //fill remaining spaces with Blank character 'B'
        }
        //set read head position and current state back to 0 before starting
        this.readPosition = 0;
        this.currentStateIdx = 0;
        //the end state is always n-1 as specified in prompt
        int endStateIdx = states.length - 1;
        while(currentStateIdx != endStateIdx){
            //read current character in tape
            char nextChar = this.tape[readPosition];
            //consult transitions stored in State object to see how to proceed
            if(this.states[currentStateIdx].hasTransition(nextChar)){
                Transition transition = this.states[currentStateIdx].getTransition(nextChar);
                //uncomment to watch machine steps
                //System.out.println("delta (q" + currentStateIdx + "," + nextChar + ") = (q" + transition.getStateChange() + "," + transition.getOutput() + "," + transition.getTapeMove() + ")");
                //change state to one specified
                this.currentStateIdx = transition.getStateChange();
                //change char under read head to the one specified
                this.tape[readPosition] = transition.getOutput();
                //change read head position by step count specified
                this.readPosition += transition.getTapeMove();
                //print out string
                //System.out.println(new String(tape));
            } else {
                //print error if transition is not found
                System.out.println("Error! The current state '" +currentStateIdx+ "' does not have a transition for character " + nextChar);
                return new String(tape);
            }
        }
        return new String(tape);
    }
    public void addFinalStates(boolean[] finalStates) throws Exception{
        if(finalStates.length == states.length){
            this.finalStates = finalStates;
        } else {
            throw new Exception("Error! The number of final states passed in exceeds the total number of machine states");
        }
    }
    public boolean isFinal(){
        return this.finalStates[this.currentStateIdx];
    }
}
