//Author: Joseph Standerfer
//St_id: josephst
//Date: 4/23/2019

package turingMachine;

public class TuringFlipper {
    public static void main(String[] args) throws Exception{
        Turing machine1 = new Turing(2); // A two state machine

        State s0 = new State(0); // Only s0 has transitions

        s0.addTransition(new Transition('0','1',Transition.RIGHT,0));
        s0.addTransition(new Transition('1','0',Transition.RIGHT,0));
        s0.addTransition(new Transition('B','B',Transition.RIGHT,1));

        machine1.addState(s0); // Add the state to the machine

        String inTape = "0101010101010"; // Define some input

        System.out.println(inTape);

        String outTape = machine1.execute(inTape); // Execute the machine

        System.out.println(outTape); // Show the machine’s output

    }
}
