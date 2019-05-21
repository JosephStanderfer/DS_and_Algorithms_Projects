//Author: Joseph Standerfer
//St_id: josephst
//Date: 2/28/2019

package project3;

import java.math.BigInteger;
import java.util.Scanner;

public class ReversePolishNotation {
    static RedBlackTree variableStorage = new RedBlackTree();
    //static DynamicStack operatorStack = new DynamicStack();
    static DynamicStack numAndVarStack = new DynamicStack();
    static String[] operators = new String[]{"+", "-", "*", "/" , "%", "=", "~", "#"};  //used to identify operators
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("\njava Reverse Polish Notation");
        //continue operation until console input is empty. Then loop will break
        while(true){
            String input = scanner.nextLine().trim();
            if(input.equals(null) || input.isEmpty()){
                System.out.println("terminating");
                break;
            }
            //sends input to solver method to be deciphered and computed
            BigInteger answer = solver(input);
            //output answer
            System.out.println(answer);
        }
    }
    public static BigInteger solver(java.lang.String input){
        Scanner entries = new Scanner(input).useDelimiter(" ");
        //iterate through values in string input and add them to number stack
        //call solver when operator found
        while(entries.hasNext()){
            //look at first character in string to decipher with type of value it is
            if( entries.hasNextBigInteger()) {
                numAndVarStack.push(entries.nextBigInteger());
            } else {   // else decide if its a variable or operator using the first character
                String varOrOper = entries.next();
                char s = varOrOper.charAt(0);
                //if first char is alphabetic we can assume the string represents a variable
                if(Character.isAlphabetic(s)){
                    //push value to stack
                    numAndVarStack.push(varOrOper);
                } else {  //otherwise its either an operator or a typo
                    boolean isOperator = false;
                    //cycle through possible operators to determine if it is one
                    for (String op : operators) {
                        if (op.equals(varOrOper)) {
                            isOperator = true;
                            break;
                        }
                    }
                    if (isOperator) {
                        //whenever an operator is found, go to solver to compute result
                        performOperation(varOrOper);
                    } else {
                        throw new IllegalArgumentException("error: invalid argument '" + varOrOper + "'");
                    }
                }
            }
        }
        //retrieve remaining value in stack
        Object top = numAndVarStack.pop();
        if(numAndVarStack.getCount() > 1){ //ensure no other values remain in stack. if so, entry was invalid
            System.out.println(numAndVarStack.toString());
            throw new IllegalArgumentException("error. missing operation");
        } else if (!(top instanceof BigInteger)) {
            //check whether the remaining value in the stack is a bigInteger. if not replace variable with value.
            BigInteger value = variableStorage.lookup((String) top);
            if (value != null) {  //if variable value is found
                top = value; //insert value into array
            } else { // otherwise this is not a valid entry
                throw new IllegalArgumentException("error. variable " + top + " has not been assigned a value");
            }
        }
        return (BigInteger)top;
    }
    public static BigInteger performOperation(String operator){
        Object var1, var2;
        Object[] varArray;  //storage for values used in method
        switch (operator) {   //use operator to call appropriate functions
            case "~":
                var1 = numAndVarStack.pop();
                if (var1 instanceof BigInteger) {  //check if item popped from stack is a bigInteger
                    numAndVarStack.push(((BigInteger) var1).negate());  // push computed result back onto stack
                } else {
                    throw new IllegalArgumentException("error. Invalid entry: '~' cannot be assigned to undeclared variable");
                }
                break;
            case "#":
                varArray = getBigIntegers(3); //calls method to retrieve and verify bigIntegers for computation
                BigInteger powModResult = ((BigInteger) varArray[2]).modPow((BigInteger) varArray[1], (BigInteger) varArray[0]);
                numAndVarStack.push(powModResult); //insert computed result back into stack
                break;
            case "=":
                var1 = numAndVarStack.pop();
                var2 = numAndVarStack.pop();
                //var1 needs to be a value for = assignment. convert var2 to BigInteger
                if (!(var1 instanceof BigInteger)) {
                    BigInteger value = variableStorage.lookup((String) var1);
                    if (value != null) {  //if variable value is found
                        var1 = value; //replace string with value
                    } else { // otherwise this is not a valid entry
                        throw new IllegalArgumentException("error. " + var1 + " not a value");
                    }
                }
                //var1 needs to be a variable. check for alphabetic first char
                if (var2 instanceof java.lang.String) {
                    variableStorage.insert((String) var2, (BigInteger) var1);
                    numAndVarStack.push((BigInteger) var1); //add save value back to the number stack to be printed when operations complete
                } else {
                    throw new IllegalArgumentException("error. " + var2 + " not an lvalue");
                }
                break;
            case "+":
                varArray = getBigIntegers(2); //calls method to retrieve and verify bigIntegers for computation
                numAndVarStack.push(((BigInteger) varArray[1]).add((BigInteger) varArray[0])); //compute operation and restore value
                break;
            case "-":
                varArray = getBigIntegers(2); //calls method to retrieve and verify bigIntegers for computation
                numAndVarStack.push(((BigInteger) varArray[1]).subtract((BigInteger) varArray[0]));  //compute operation and restore value
                break;
            case "*":
                varArray = getBigIntegers(2); //calls method to retrieve and verify bigIntegers for computation
                //compute operation and push number back onto stack
                numAndVarStack.push(((BigInteger) varArray[1]).multiply((BigInteger) varArray[0]));
                break;
            case "/":
                varArray = getBigIntegers(2); //calls method to retrieve and verify bigIntegers for computation
                //compute operation and push number back onto stack
                numAndVarStack.push(((BigInteger) varArray[1]).divide((BigInteger) varArray[0]));
                break;
            case "%":
                varArray = getBigIntegers(2); //calls method to retrieve and verify bigIntegers for computation
                //compute operation and push number back onto stack
                numAndVarStack.push(((BigInteger) varArray[1]).mod((BigInteger) varArray[0]));
                break;
            default:
                throw new IllegalArgumentException("error. operator" + operator + " not found");
        }
        //return number found
        return (BigInteger)numAndVarStack.getTop();
    }
    // retrieve q number of values from stack, finds their bigInteger representations if they are variables then returns
    // an array of those values.
    public static Object[] getBigIntegers(int q){
        Object[] varArray = new Object[q];
        for(int i = 0; i < q; i++){
            varArray[i] = numAndVarStack.pop();
        }
        for (int i = 0; i < q; i++) {
            if (!(varArray[i] instanceof BigInteger)) {  //if the object is not a bigInteger, it must be a variable. so search for value
                BigInteger value = variableStorage.lookup((String) varArray[i]);
                if (value != null) {  //if variable value is found
                    varArray[i] = value; //insert value into array
                } else { // otherwise this is not a valid entry
                    throw new IllegalArgumentException("error. variable " + varArray[i] + " has not been assigned a value");
                }
            }
        }
        return varArray;
    }
}
