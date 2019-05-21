//Author: Joseph Standerfer
//St_id: josephst
//Date: 4/23/2019

package turingMachine;

import java.util.Scanner;

public class TuringDecider {
    // This turing machine reads a series of zeroes and ones and decides the language L = {0^n1^n, n >= 1}.
    // The sequence of zeroes and ones will be termintated by a blank "b" or "".
    // Your Turing machine will read the input and decide whether the string fits this convention
    // It will leave the result (a 1 = ‘yes’ or a 0 = ‘no’) on the output tape. Here are some example runs:
    // 00011 -> 0BBBBBB...
    // 0011 -> 1BBBBBB...
    // 011 -> 0BBBBBB...
    // 01 -> 1BBBBBB...
    // 1100 -> 0BBBBBB...
    // 0101 -> 0BBBBBB...
    public static void main(String[] args) throws Exception{
        Turing machine1 = new Turing(9); // A two state machine

        int failedState = 5;  //send to this state if string is found not to fit convention

        //first character in tape gets set to 'F' so it can be located at the end
        State s0 = new State(0);
        //if first letter is 0, good so far. Send to state 2 to look for corresponding 1
        s0.addTransition(new Transition('0','F',Transition.RIGHT,2));
        //if tape starts with 1 or B, it has failed the 01 format. Send to failed state flow
        s0.addTransition(new Transition('1','F',Transition.RIGHT,failedState));
        s0.addTransition(new Transition('B','F',Transition.RIGHT,failedState));
        machine1.addState(s0); // Add the state to the machine

        State s1 = new State(0);
        //searches for 0 to mark as X
        s1.addTransition(new Transition('0','X',Transition.RIGHT,2));
        //if Y is found first, no more 01 pairs. send to ending states
        s1.addTransition(new Transition('Y','Y',Transition.RIGHT,4));
        machine1.addState(s1); // Add the state to the machine

        //searches for corresponding 1 for each 0 (changed to X)
        //changes that 1 to Y
        State s2 = new State(0);
        s2.addTransition(new Transition('0','0',Transition.RIGHT,2));
        s2.addTransition(new Transition('1','Y',Transition.LEFT,3));
        s2.addTransition(new Transition('Y','Y',Transition.RIGHT,2));
        //machine reached end of tape before finding corresponding 1, send to failed state
        s2.addTransition(new Transition('B','B',Transition.LEFT,failedState));
        machine1.addState(s2); // Add the state to the machine

        //after 1 0 pair is found. find latest X to start the process again
        State s3 = new State(0);
        s3.addTransition(new Transition('0','0',Transition.LEFT,3));
        s3.addTransition(new Transition('X','X',Transition.RIGHT,1));
        s3.addTransition(new Transition('Y','Y',Transition.LEFT,3));
        s3.addTransition(new Transition('F','F',Transition.RIGHT,1));
        machine1.addState(s3); // Add the state to the machine

        //no more XY pairs to be made. check whole string to determine whether its valid
        State s4 = new State(0);
        s4.addTransition(new Transition('0','Z',Transition.RIGHT,5));
        s4.addTransition(new Transition('1','Z',Transition.RIGHT,5));
        s4.addTransition(new Transition('Y','Y',Transition.RIGHT,4));
        s4.addTransition(new Transition('B','B',Transition.LEFT,7));
        machine1.addState(s4); // Add the state to the machine

        //failed state. continue to end of tape to transition everything to blank
        //once complete, move to state 6 return to first index and replace F with 0
        State s5 = new State(0);
        s5.addTransition(new Transition('0','Z',Transition.RIGHT,5));
        s5.addTransition(new Transition('1','Z',Transition.RIGHT,5));
        s5.addTransition(new Transition('Y','Z',Transition.RIGHT,5));
        s5.addTransition(new Transition('B','B',Transition.LEFT,6));
        machine1.addState(s5); // Add the state to the machine
        //once complete, move to state 6 return to first index and replace F with 0
        State s6 = new State(0);
        s6.addTransition(new Transition('0','B',Transition.LEFT,6));
        s6.addTransition(new Transition('1','B',Transition.LEFT,6));
        s6.addTransition(new Transition('F','0',Transition.RIGHT,8));
        s6.addTransition(new Transition('X','B',Transition.LEFT,6));
        s6.addTransition(new Transition('Y','B',Transition.LEFT,6));
        s6.addTransition(new Transition('Z','B',Transition.LEFT,6));
        machine1.addState(s6); // Add the state to the machine

        //flow for good sequences.
        //start at last index and delete every character until the first index. Replace F with 1
        State s7 = new State(0);
        s7.addTransition(new Transition('0','B',Transition.LEFT,7));
        s7.addTransition(new Transition('1','B',Transition.LEFT,7));
        s7.addTransition(new Transition('F','1',Transition.RIGHT,8));
        s7.addTransition(new Transition('X','B',Transition.LEFT,7));
        s7.addTransition(new Transition('Y','B',Transition.LEFT,7));
        s7.addTransition(new Transition('Z','B',Transition.LEFT,7));
        machine1.addState(s7); // Add the state to the machine


        //get input from user
        Scanner scanner = new Scanner(System.in); //used to get input from user
        boolean validInput = false;
        String inTape = "";
        //example cases
//        String inTape = "";
//        String inTape = "01"; // Define some input
//        String inTape = "10"; // Define some input
//        String inTape = "00000000001111111111"; // Define some input
//        String inTape = "1100111";
        System.out.println("Enter tape contents below:");
        while(!validInput){
            inTape = scanner.nextLine();
            validInput = true;
            if(inTape.trim() == null){
                validInput = false;
            } else {
                for(char c : inTape.trim().toCharArray()){
                    if(c != '0' && c != '1' && c != 'B'){
                        validInput = false;
                    }
                }
            }
            if(!validInput){
                System.out.println("Invalid entry. Enter new tape below:");
            }
        }


        System.out.println(inTape);

        String outTape = machine1.execute(inTape); // Execute the machine

        System.out.println(outTape); // Show the machine’s output

    }
}
