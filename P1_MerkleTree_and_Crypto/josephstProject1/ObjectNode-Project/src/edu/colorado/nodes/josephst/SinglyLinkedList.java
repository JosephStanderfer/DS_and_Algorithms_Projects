//name: Joseph Standerfer
//st_id: josephst

package edu.colorado.nodes.josephst;

import edu.colorado.nodes.ObjectNode;

public class SinglyLinkedList {
    ObjectNode head;
    ObjectNode tail;
    ObjectNode iterator;
    int countNodes;


    public SinglyLinkedList(){
        this.countNodes = 0;
    }
    
    public static void main(String[] args) {
        SinglyLinkedList s = new SinglyLinkedList();

        //testing list creation front and back node additions
        //should result in list of ordered integers 0-9
        for(int i = 5; i >= 0; i--) {
            s.addAtFrontNode(i);
        }
        for(int i = 6; i < 10; i++){
            s.addAtEndNode(i);
        }

        // toString test
        System.out.println("List Contents: " + s.toString());

        // countNodes test
        System.out.println("Count of Nodes: " + s.countNodes);

        // getObjectAt test
        System.out.println("Object in node 5 (0 based): " + s.getObjectAt(5));

        // getLast test
        System.out.println("Object in last node: " + s.getLast());

        // reset(),hasNext(), and next() method test
        s.reset();
        System.out.println("** List iteration **");
        while(s.hasNext()) {
            System.out.println(s.next());
        }
        System.out.println("** end **");
    }
    // sets iterator node back to the head of the list
    // Big-Theta:  Theta(1)
    public void reset(){
        this.iterator = this.head;
    }
    // returns data from the current list iterator node and then points the list iterator to the next linked node
    // Big-Theta:  Theta(1)
    public Object next(){
        ObjectNode currentN = this.iterator;
        this.iterator = this.iterator.getLink();
        return currentN.getData();
    }
    // returns a boolean answer to whether the list iterator node is linked to another node 
    // (i.e. are there any more items in the list)
    // Big-Theta:  Theta(1)
    public boolean hasNext(){
        if(iterator != null){
            return true;
        }
        return false;
    }
    // creates a new list head and stores the Object passed in to that head. Then links the previous head of the list
    // to the new head
    // Big-Theta:  Theta(1)
    public void addAtFrontNode(Object c){
        if(c == null){
            throw new NullPointerException("cannot insert null value into linked list");
        }
        else if (this.head == null) {
            this.head = new ObjectNode(c, null);
            this.tail = this.head;
            this.iterator = this.head;
            countNodes++;
        }
        else {
            ObjectNode newHead = new ObjectNode(c, this.head);
            this.head = newHead;
            countNodes++;
        }
    }
    // creates a new list tail and stores the Object passed in to that tail. Then links the previous tail of the list
    // to the new tail
    // Big-Theta:  Theta(1)
    public void addAtEndNode(Object c){
        if(c == null){
            throw new NullPointerException("cannot insert null value into linked list");
        } else if (this.head == null){    // if head is null, the list is currently empty and the Object passed in will be the head and tail
            this.head = new ObjectNode(c, null);
            this.tail = this.head;
            this.iterator = this.head;
            countNodes++;
        }
        else {    // if there's already nodes in the list, create a new tail node and add the passed in Object to that tail
            ObjectNode newTail = new ObjectNode(c, null);
            this.tail.addNodeAfter(newTail.getData());
            this.tail.setLink(newTail);
            this.tail = newTail;
            countNodes++;
        }
    }
    // return the nodeCount of the list
    // Big-Theta:  Theta(1)
    public int countNodes(){
        return countNodes;
    }
    // iterate through the list nodes to find the "i" index and return the data stored in that node
    // Big-Theta:  Theta(n)
    public Object getObjectAt(int i){
        if(i < 0 || i >= countNodes){
            throw new IndexOutOfBoundsException("Index " + i + " does not exist in this list");
        }
        ObjectNode current = this.head;
        for(int x = 0; x < i; x++){
            current = current.getLink();
        }
        return current.getData();
    }
    // if there are nodes in the list, return the data of the list's tail
    // Big-Theta:  Theta(1)
    public Object getLast(){
        if(tail != null){
            return tail.getData();
        }
        return null;
    }
    // iterate through the full list and return a concatenated string representation of the data stored in each.
    // Big-Theta:  Theta(n)
    public String toString(){
        if(this.head != null){
            return this.head.toString();
        }
        return null;
    }
}
