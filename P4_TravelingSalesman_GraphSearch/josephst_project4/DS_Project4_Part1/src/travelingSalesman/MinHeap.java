//Author: Joseph Standerfer
//St_id: josephst
//Date: 3/19/2019

package travelingSalesman;

import java.util.Random;

public class MinHeap {
    //CrimeRecord[] crimeRecords
    int[] crimeHeapIndex; //stores location of each crime as organized in the heap. prevents searching all nodes for update
    HeapNode[] minHeap; //stores the heap numbers in heap
    int nodeId = 0; //stores total number of nodes that have been stored in minheap. Used to assign different crimeNum to each
    int nodeCount = 0;  //stores the number of nodes in the minHeap

    public MinHeap(int heapSize){
        this.crimeHeapIndex = new int[heapSize];
        //change initial inputs for array too -1 so that empty spots can be identified
        for(int i = 0; i < crimeHeapIndex.length; i++){
            this.crimeHeapIndex[i] = -1;
        }
        this.minHeap = new HeapNode[heapSize];
    }
    //returns true if minHeap is empty
    public boolean isEmpty(){
        return this.nodeCount == 0;
    }
    public int parentIndex(int heapIdx){
        if(heapIdx == 0){
            return -1;  //the root has no parent
        }
        return (heapIdx-1)/2;  //parent index of each non-root node
    }
    public int leftChildIndex(int heapIdx){
        return (heapIdx * 2) + 1;
    }
    public int rightChildIndex(int heapIdx){
        return (heapIdx * 2) + 2;
    }
    public boolean isLeaf(int heapIdx){
        int LC = leftChildIndex(heapIdx);
        if(LC >= minHeap.length) return true;
        else if (this.minHeap[LC] == null) return true;
        else return false;
    }
    //swaps the position of two numbers in the heap as well as adjusts the crimeHeapIndex to match
    public void swapPositions(int heapIdx1, int heapIdx2){
        //get the index of each node in the crimeHeapIndex array so the heap positions can be updated
        int crimeNum1 = minHeap[heapIdx1].getCrimeNum();
        int crimeNum2 = minHeap[heapIdx2].getCrimeNum();

        //swap node positions in minHeap
        HeapNode holder = minHeap[heapIdx1];
        minHeap[heapIdx1] = minHeap[heapIdx2];
        minHeap[heapIdx2] = holder;

        //update stored node locations in crimeHeapIndex array
        crimeHeapIndex[crimeNum1] = heapIdx2;
        crimeHeapIndex[crimeNum2] = heapIdx1;

    }
    public void reheap(int heapIdx){
        //if node is only one in tree, does not need to be reheaped
        if(nodeCount == 1) return;

        int newNodePosition = -1; //stores the node position that the node gets moved to so method can be called again for that node
        //get positions of parent and child nodes
        int pIdx = parentIndex(heapIdx);
        int rcIdx = rightChildIndex(heapIdx);
        int lcIdx = leftChildIndex(heapIdx);

        //if node is a leaf and not root, only check parents
        if(isLeaf(heapIdx) && heapIdx != 0){
            //only check parents
            //if parent key is lower than this node, swap nodes in minHeap
            if(this.minHeap[heapIdx].getKey() < this.minHeap[pIdx].getKey()){
                swapPositions(heapIdx, pIdx);
                newNodePosition = pIdx;
            }
        }
        //if node being heaped is at root, only check children
        else if (heapIdx == 0){
            //only check children
            //if right child node is empty only check left
            if(this.minHeap[rcIdx] == null){
                if (this.minHeap[heapIdx].getKey() > this.minHeap[lcIdx].getKey()) {
                    swapPositions(heapIdx, lcIdx);
                    newNodePosition = lcIdx;
                }
                //if right node is not null compare both children's keys
            } else {
                //else if a child key is larger than this node, swap with lower key'd child node
                if(this.minHeap[heapIdx].getKey() > this.minHeap[rcIdx].getKey() ||
                        this.minHeap[heapIdx].getKey() > this.minHeap[lcIdx].getKey()) {
                    //then check which node is smaller
                    if(this.minHeap[rcIdx].getKey() < this.minHeap[lcIdx].getKey()){
                        swapPositions(heapIdx, rcIdx);
                        newNodePosition = rcIdx;
                    } else {
                        swapPositions(heapIdx, lcIdx);
                        newNodePosition = lcIdx;
                    }
                }
            }
        } else {
            //if node is not a root or a leaf compare all surrounding nodes for swap
            //if parent key is lower than this node, swap nodes in MinHeap
            if(this.minHeap[heapIdx].getKey() < this.minHeap[pIdx].getKey()){
                swapPositions(heapIdx, pIdx);
                newNodePosition = pIdx;
                //check whether right child node is null before comparing both child node keys
            } else if(this.minHeap[rcIdx] == null){
                //if right child is null only compare left child's key
                if(this.minHeap[heapIdx].getKey() > this.minHeap[lcIdx].getKey()){
                    swapPositions(heapIdx, lcIdx);
                    newNodePosition = lcIdx;
                }
            } else{
                //is right child is not null compare both children's keys
                if (this.minHeap[heapIdx].getKey() > this.minHeap[rcIdx].getKey() ||
                        this.minHeap[heapIdx].getKey() > this.minHeap[lcIdx].getKey()) {
                    //then check which node is smaller
                    if(this.minHeap[rcIdx].getKey() < this.minHeap[lcIdx].getKey()){
                        swapPositions(heapIdx, rcIdx);
                        newNodePosition = rcIdx;
                    } else {
                        swapPositions(heapIdx, lcIdx);
                        newNodePosition = lcIdx;
                    }
                }
            }
        }
        //call reheap method again for this nodes new position to check if it is now in the correct minHeap position
        if(newNodePosition != -1){
            reheap(newNodePosition);
        }
    }
    public void updateKey(int crimeNum, double newKey){
        if(crimeHeapIndex[crimeNum] == -1){
            throw new IllegalArgumentException("The record you are trying to update does not exist");
        }
        //find key in minHeap using crimeHeapIndexArray then update key
        int keyLocation = crimeHeapIndex[crimeNum];
        this.minHeap[keyLocation].setKey(newKey);

        //then call reheap method and send node index in minHeap
        reheap(keyLocation);
    }
    //inserts key at the next leaf position (left to right) in the tree, then moves it up the tree until the key is in
    //the proper position within the min heap
    public void insert(double key, CrimeRecord cr){
        //double size of minHeap if it is full
        if(nodeCount >= minHeap.length - 1){
            //if true, update array size and copy it over
            HeapNode[] newMinHeap = new HeapNode[this.minHeap.length * 2];
            for(int i = 0; i < this.minHeap.length; i++){
                newMinHeap[i] = this.minHeap[i];
            }
            this.minHeap = newMinHeap;
        }
        //check if number of crimes added in total exceeds the storage array size
        //may differ from above check because nodeCount goes down as nodes are removed
        if(nodeId >= crimeHeapIndex.length - 1){
            //if true, double array size and copy it over
            int[] newCrimeHeapIndex = new int[this.crimeHeapIndex.length * 2];
            for(int i = 0; i < newCrimeHeapIndex.length; i++){
                if(i<this.crimeHeapIndex.length){
                    newCrimeHeapIndex[i] = this.crimeHeapIndex[i];
                }else {
                    //instantiate empty positions with -1
                    newCrimeHeapIndex[i] = -1;
                }
            }
            this.crimeHeapIndex = newCrimeHeapIndex;
        }
        //create new heapNode for crimeRecord
        HeapNode n = new HeapNode(key, this.nodeId, cr);
        //place new node at last place in heap
        this.minHeap[nodeCount] = n;
        //store the node's location in the heap in the index finder
        this.crimeHeapIndex[this.nodeId] = this.nodeCount;

        this.nodeCount++;
        this.nodeId++;

        //call reheap method to move node to proper position in heap
        reheap(this.nodeCount-1);
    }

    //remove and returns the crimeNum with a distance at the top of the heap (minimum)
    public HeapNode removeMin(){
        //minimum will be located at the front of the heap
        HeapNode min = this.minHeap[0];
        //swap root and last node in heap
        swapPositions(0,this.nodeCount -1);
        //use mins location to update crimeHeapIndex array with -1 (ie. crimeId not stored in heap)
        this.crimeHeapIndex[min.getCrimeNum()] = -1;
        //delete last position in heap
        this.minHeap[this.nodeCount -1] = null;
        //reduce node count
        this.nodeCount--;
        //reheap first node
        if(nodeCount != 0){
            reheap(0);
        }

        return min;
    }
    //prints array representation of the min heaps keys
    public void printHeap(){
        for(int i = 0; i < this.minHeap.length; i++){
            if(this.minHeap[i] != null) {
                System.out.print(this.minHeap[i].getKey()+", ");
            }
        }
        System.out.println();
    }
    //Returns true if crimeNum is currently stored within heap
    public boolean contains(int crimeNum){
        if (this.crimeHeapIndex.length - 1 < crimeNum) return false;
        else return this.crimeHeapIndex[crimeNum] != -1;
    }

    //test the minHeap
    public static void main(String[] args) {
        int length = 10;
        MinHeap heap = new MinHeap(length);
        Random rand = new Random(0);
        for(int i = 0; i < length; i++){
            CrimeRecord cr = null;
            double r = rand.nextInt(50);
            System.out.println("insert -> " + r);
            heap.insert(r,cr);
        }
        //print minHeap
        System.out.println("\n*** Min Heap ***");
        heap.printHeap();
        System.out.println("****************\n");

        //test removing minimums from heap
        System.out.println("remove min <- "+heap.removeMin().getKey());
        System.out.println("*** Min Heap ***");
        heap.printHeap();
        System.out.println("****************\n");

        System.out.println("remove min <- "+heap.removeMin().getKey());
        System.out.println("*** Min Heap ***");
        heap.printHeap();
        System.out.println("****************\n");

        //test updating nodes
        System.out.println("update 3rd crime: " + heap.minHeap[heap.crimeHeapIndex[3]].getKey() + " -> 7");
        heap.updateKey(3, 7);
        System.out.println("*** Min Heap ***");
        heap.printHeap();
        System.out.println("****************\n");

        System.out.println("update 4th crime: " + heap.minHeap[heap.crimeHeapIndex[4]].getKey() + " -> 12");
        heap.updateKey(4, 12);
        System.out.println("*** Min Heap ***");
        heap.printHeap();
        System.out.println("****************\n");


    }
}
