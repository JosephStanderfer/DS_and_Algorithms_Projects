//Author: Joseph Standerfer
//St_id: josephst
//Date: 4/23/2019

package turingMachine;

public class TuringSubtractor {
    public static void main(String[] args) throws Exception{
        Turing machine1 = new Turing(7); // A two state machine

        State s0 = new State(0); // Only s0 has transitions
        //Begin. Replace the leading 0 by B.
        s0.addTransition(new Transition('0','B',Transition.RIGHT,1));  //d(q0,0) = (q1,B,R)
        //If in state q0 a 1 is encountered instead of a 0, the first block of 0’s has been exhausted, as in situation (ii) above. M enters
        //state q5 to erase the rest of the tape, then enters q6 and halts.
        s0.addTransition(new Transition('1','B',Transition.RIGHT,5));  //d(q0,1) = (q5,B,R)
        machine1.addState(s0); // Add the state to the machine

        State s1 = new State(0);
        //Search right looking for the first 1.
        s1.addTransition(new Transition('0','0',Transition.RIGHT,1));  //d(q1,0) = (q1,0,R)
        s1.addTransition(new Transition('1','1',Transition.RIGHT,2));  //d(q1,1) = (q2,1,R)
        machine1.addState(s1); // Add the state to the machine

        State s2 = new State(0);
        //Search right past 1’s until encountering a 0. Change that 0 to 1.
        s2.addTransition(new Transition('1','1',Transition.RIGHT,2)); //d(q2,1) = (q2,1,R)
        s2.addTransition(new Transition('0','1',Transition.LEFT,3));  //d(q2,0) = (q3,1,L)
        //If in state q2 a B is encountered before a 0, we have situation i described above. Enter state q4 and move left, changing all 1’s
        //to B’s until encountering a B. This B is changed back to a 0, state q6 is entered and M halts.
        s2.addTransition(new Transition('B','B',Transition.LEFT,4));  //d(q2,B) = (q4,B,L)
        machine1.addState(s2); // Add the state to the machine

        State s3 = new State(0);
        //Move left to a blank. Enter state q0 to repeat the cycle.
        s3.addTransition(new Transition('0','0',Transition.LEFT,3));  //d(q3,0) = (q3,0,L)
        s3.addTransition(new Transition('1','1',Transition.LEFT,3));  //d(q3,1) = (q3,1,L)
        s3.addTransition(new Transition('B','B',Transition.RIGHT,0)); //d(q3,B) = (q0,B,R)
        machine1.addState(s3); // Add the state to the machine

        State s4 = new State(0);
        s4.addTransition(new Transition('1','B',Transition.LEFT,4));  //(q4,1) = (q4,B,L)
        s4.addTransition(new Transition('0','0',Transition.LEFT,4));  //(q4,0) = (q4,0,L)
        s4.addTransition(new Transition('B','0',Transition.RIGHT,6)); //(q4,B) = (q6,0,R)
        machine1.addState(s4); // Add the state to the machine

        State s5 = new State(0);
        s5.addTransition(new Transition('0','B',Transition.RIGHT,5));  //(q5,0) = (q5,B,R)
        s5.addTransition(new Transition('1','B',Transition.RIGHT,5));  //(q5,1) = (q5,B,R)
        s5.addTransition(new Transition('B','B',Transition.RIGHT,6)); //(q5,B) = (q6,B,R)
        machine1.addState(s5); // Add the state to the machine


//        String inTape = "010000"; // Define some input
        String inTape = "00001000";
//        String inTape = "0000100";

        System.out.println(inTape);

        String outTape = machine1.execute(inTape); // Execute the machine

        System.out.println(outTape); // Show the machine’s output

    }

}
