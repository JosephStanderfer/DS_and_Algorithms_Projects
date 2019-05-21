//Author: Joseph Standerfer
//St_id: josephst
//Date: 4/23/2019

package turingMachine;

public class Transition {
    private char input;
    private char output;
    private int tapeSteps;
    private int stateChange;
    public final static int RIGHT = 1;
    public final static int LEFT = -1;

    public Transition(char input, char output, int tapeSteps, int stateChange){
        this.input = input;
        this.output = output;
        this.tapeSteps = tapeSteps;
        this.stateChange = stateChange;
    }
    public char getInput() {
        return input;
    }

    public char getOutput() {
        return output;
    }

    public int getTapeMove() {
        return tapeSteps;
    }

    public int getStateChange() {
        return stateChange;
    }
    //used for balanced tree testing
    @Override
    public String toString() {
        return "Transition for input character "+ input;
    }
}
