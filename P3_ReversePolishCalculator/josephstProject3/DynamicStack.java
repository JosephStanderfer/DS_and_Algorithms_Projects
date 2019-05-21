//Author: Joseph Standerfer
//St_id: josephst
//Date: 2/28/2019

package project3;

public class DynamicStack {
    private java.lang.Object[] stack;
    private int top; //stores the array index of the queues top (first added)
    private int count;
    public DynamicStack(){
        this.stack = new java.lang.Object[6];
        this.top = -1;
        this.count = 0;
    }
    //returns true if nothing is stored in the holder array "stack"
    public boolean isEmpty(){
        return (this.top < 0);
    }
    // returns true if objects fill entire contents of the holder array "stack"
    public boolean isFull(){
        return (this.top >= this.stack.length - 1);
    }
    // Best case runtimes for method below:
    // Big-Theta: theta(1) => o(1), omega(1)
    // Worst case runtimes for method below:
    // Big-Theta: Theta(1) => O(1), Omega(1)
    // returns item at the top of the stack then deletes it from stack
    public java.lang.Object pop(){
        //check if stack is empty
        if(isEmpty()){
            throw new IllegalArgumentException("stack underflow exception");
        }
        java.lang.Object o = this.stack[this.top];
        this.stack[this.top] = null; //delete object from stack
        this.top--;  //move top position down
        this.count--;
        return o;
    }
    // Best case runtimes for method below:
    // Big-Theta: theta(1) => o(1), omega(1)
    // Worst case runtimes for method below:
    // Big-Theta: Theta(1) => O(1), Omega(1)
    // adds item passed in to the top of the stack
    public void push(java.lang.Object x){
        //ensure item being added is not null
        if(x == null){
            throw new IllegalArgumentException("Cannot insert null objects into stack");
        }
        //if array holder is full, create new array double the size and copy over items in old array
        if(isFull()) {
            java.lang.Object[] newStack = new java.lang.Object[stack.length * 2];
            for (int i = 0; i < this.stack.length; i++) {
                newStack[i] = this.stack[i];
            }
            this.stack = newStack;
        }
        this.count++;
        this.top++;
        this.stack[this.top] = x;
    }
    //returns items at the top of the stack without deleting it
    public java.lang.Object getTop(){
        if(isEmpty()){
            return null;
        }
        return this.stack[this.top];
    }
    //provides count of items stored in stack
    public int getCount(){
        return this.count;
    }
    //print out stack contents delimited by commas
    public java.lang.String toString(){
        StringBuilder sb = new StringBuilder("");
        for(int i = 0; i <= this.top; i++){
            sb.append(this.stack[i] + ",");
        }
        return sb.toString();
    }
    //main testing method for class
    public static void main(String[] args) {
        DynamicStack stack = new DynamicStack();

        //initial stack method checks
        System.out.println("**** Stack info ****");
        System.out.println("Stack size\t\t: " + stack.stack.length);
        System.out.println("Stack top index\t: " + stack.top);
        System.out.println("Is stack empty?\t: " + stack.isEmpty());
        System.out.println("Is stack full?\t: " + stack.isFull());

        System.out.println("\n**** Push 1000 values onto stack ****");
        System.out.print("push -> ");
        int charCount = 0;
        for(int i = 1; i <= 1000; i++){
            stack.push(i);
            System.out.print(i + ", ");
            // count number of characters printed to line to determine when to move to next line
            charCount += (int)(Math.log10(i) + 3);
            if(charCount > 500){
                System.out.print("\n\t");
                charCount = 0;
            }
        }
        //stack method checks post adds
        System.out.println("\n**** Stack info ****");
        System.out.println("Stack size\t\t: " + stack.stack.length);
        System.out.println("Stack top index\t: " + stack.top);
        System.out.println("Is stack empty?\t: " + stack.isEmpty());
        System.out.println("Is stack full?\t: " + stack.isFull());

        //pop and display all values in stack
        System.out.println("\n**** Pop all values from stack ****");
        System.out.print("pop <- ");
        charCount = 0;
        int counter = 0;
        while(!stack.isEmpty()){
            System.out.print(stack.pop() + ", ");
            counter++;
            charCount += (int)(Math.log10(counter) + 3);
            if(charCount > 500) {
                System.out.print("\n\t");
                charCount = 0;
            }
        }
        //stack method checks post adds
        System.out.println("\n**** Stack info ****");
        System.out.println("Stack size\t\t: " + stack.stack.length);
        System.out.println("Stack top index\t: " + stack.top);
        System.out.println("Is stack empty?\t: " + stack.isEmpty());
        System.out.println("Is stack full?\t: " + stack.isFull());

    }
}
