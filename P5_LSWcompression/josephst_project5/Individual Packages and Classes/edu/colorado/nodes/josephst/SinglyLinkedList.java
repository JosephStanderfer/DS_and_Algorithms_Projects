package edu.colorado.nodes.josephst;

import edu.colorado.nodes.ObjectNode;

public class SinglyLinkedList<T> {
    ObjectNode head;
    ObjectNode tail;
    ObjectNode iterator;
    int countNodes;


    public SinglyLinkedList(){
        this.countNodes = 0;
    }

    public static void main(String[] args) {
        SinglyLinkedList<Integer> s = new SinglyLinkedList();

        //testing list creation front and back node additions
        //should result in list of ordered integers 0-9
        for(int i = 5; i >= 0; i--) {
            s.addAtFrontNode(i);
        }
        for(int i = 6; i < 10; i++){
            s.addAtEndNode(i);
            System.out.println(s.next());
            System.out.println(s.iterator.getData());
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

    public void reset(){
        this.iterator = this.head;
    }
    public Object next(){
        ObjectNode currentN = this.iterator;
        this.iterator = this.iterator.getLink();
        return currentN.getData();
    }
    public boolean hasNext(){
        if(iterator != null){
            return true;
        }
        return false;
    }
    public void addAtFrontNode(T c){
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
    public void addAtEndNode(T c){
        if(c == null){
            throw new NullPointerException("cannot insert null value into linked list");
        } else if (this.head == null){
            this.head = new ObjectNode(c, null);
            this.tail = this.head;
            this.iterator = this.head;
            countNodes++;
        }
        else {
            ObjectNode newTail = new ObjectNode(c, null);
            this.tail.addNodeAfter(newTail.getData());
            this.tail.setLink(newTail);
            this.tail = newTail;
            countNodes++;
        }
    }
    public int countNodes(){
        return countNodes;
    }
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
    public Object getLast(){
        if(tail != null){
            return tail.getData();
        }
        return null;
    }
    public String toString(){
        if(this.head != null){
            return this.head.toString();
        }
        return null;
    }
}
