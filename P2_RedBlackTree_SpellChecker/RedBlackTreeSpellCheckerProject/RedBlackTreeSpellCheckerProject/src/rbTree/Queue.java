//Author: Joseph Standerfer
//St_id: josephst
//Date: 1/31/2019

package rbTree;

import java.util.EmptyStackException;

public class Queue {
    private java.lang.Object[] queue;
    private int front; //stores the array index of the queues front (first added)
    private int rear; //stores the array index of the queues rear (latest added)
    private int count = 0;  //records number of Objects contained in Queue

    public Queue(){
        this.queue = new java.lang.Object[5];
    }
    //Postcondition: return T/F for whether Queue is empty. No Objects stored
    public boolean isEmpty(){
        return (this.count <= 0);
    }
    //Postcondition: returns T/F for whether Queue's current storage array has any empty positions
    public boolean isFull(){
        return (count >= this.queue.length);
    }
    
    // Pre-Post conditions:
        // Precondition: Queue is not empty
        // Postcondition: returns and removes object in first position, first one that was added
    public java.lang.Object deQueue(){
        if(this.isEmpty()){  // if nothing is in queue
            throw new EmptyStackException();
        }
        //save front position to object
        Object o = queue[this.front];
        queue[this.front] = null;
        //move next front postition to next position in array. loop around 
        //array if reached the end of the array length
        this.front = (this.front + 1) % this.queue.length;
        this.count--;
        return o;
    }
    //adds Object to the queue in last position
    // Pre-Post conditions:
        // Precondition: x != null
        // Postcondition: stores x in last position of queue.
    public void enQueue(java.lang.Object x){
        //check if x is null
        if(x == null){
            System.out.println("Error. Cannot insert null value into queue");   
            return;
        }
        if(this.isEmpty()){  // if nothing is in queue
            this.front = 0;
            this.rear = 0;
            this.queue[this.rear] = x;
            count++;
        } else if(this.isFull()) {
            //if there are more objects than index in the array then double the array size
            java.lang.Object[] nextQ = new java.lang.Object[this.queue.length * 2];

            //cycle through old array and input them in the new one with the front at index 0
            for(int i = 0; i < this.queue.length; i++){
                nextQ[i] = this.queue[(this.front + i) % this.queue.length];
            }

            // add object to new array
            nextQ[this.count] = x;
            this.queue = nextQ; //save larger array to queue
            this.front = 0;
            this.rear = this.count;
            this.count++;
        } else {
            //find next rear index and save Object, circle around array if necessary
            this.rear = (this.rear + 1) % this.queue.length;
            this.queue[this.rear] = x;
            this.count++;
        }
    }
    //adds Object to the queue in last position
    // Pre-Post conditions:
        // Precondition: Queue is not empty
        // Postcondition: returns object in front position of the array without removal
    public java.lang.Object getFront(){
        return this.queue[this.front];
    }
    //Postcondition: converts array to string
    public java.lang.String toString(){
        StringBuilder sb = new StringBuilder("");
        for(int i = 0; i < count; i++){
            sb.append(this.queue[(this.front + i) % this.queue.length].toString() + ",");
        }
        return sb.toString();
    }
    // Pre-Post conditions:
        // Precondition: a - Command line parameters are not used.
        // Postcondition: checks queue methods to ensure they are all working
    public static void main(java.lang.String[] a){
        Queue q = new Queue();
        System.out.println();
        System.out.println("Queue Test");
        System.out.println("**************************************");
        System.out.println("Queue isEmpty: " + q.isEmpty());
        //add 5 nodes from q using enqueue
        for(int i = 0; i < 5; i++){
            System.out.println("enQueue -> " + i);
            q.enQueue(i);
        }
        //remove 3 nodes from q using dequeue
        for(int i = 0; i < 3; i++){
            System.out.println(q.deQueue() + " <- deQueue");
        }
        //add 3 nodes from q using enqueue
        for(int i = 5; i < 8; i++){
            System.out.println("enQueue -> " + i);
            q.enQueue(i);
        }
        //print out current contents of queue and test methods
        System.out.println("**************************************");
        System.out.println("Queue string\t\t: " + q.toString());
        System.out.println("Expected output\t\t: 3,4,5,6,7,");
        System.out.println("Queue isEmpty\t\t: " + q.isEmpty());
        System.out.println("Queue isFull\t\t: " + q.isFull());
        System.out.println("Queue count \t\t: " + q.count);
        System.out.println("Queue front index\t: " + q.front);
        System.out.println("Queue front \t\t: " + q.getFront());
        System.out.println("Queue rear index\t: " + q.rear);
        System.out.println("Queue array size\t: " + q.queue.length);
        System.out.println("**************************************");
        //remove 3 nodes from q using dequeue
        for(int i = 0; i < 3; i++){
            System.out.println(q.deQueue() + " <- deQueue");
        }
        //add 3 nodes from q using enqueue
        for(int i = 8; i < 15; i++){
            System.out.println("enQueue -> " + i);
            q.enQueue(i);
        }
        //print out current contents of queue and test methods
        System.out.println("**************************************");
        System.out.println("Queue string\t\t: " + q.toString());
        System.out.println("Expected output\t\t: 6,7,8,9,10,11,12,13,14,");
        System.out.println("Queue isFull\t\t: " + q.isFull());
        System.out.println("Queue count \t\t: " + q.count);
        System.out.println("Queue front index\t: " + q.front);
        System.out.println("Queue front \t\t: " + q.getFront());
        System.out.println("Queue rear index\t: " + q.rear);
        System.out.println("Queue array size\t: " + q.queue.length);
        System.out.println("**************************************");
        System.out.println();
    }
}
