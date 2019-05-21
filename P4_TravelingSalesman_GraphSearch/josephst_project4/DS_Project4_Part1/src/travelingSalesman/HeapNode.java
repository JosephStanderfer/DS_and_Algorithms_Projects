//Author: Joseph Standerfer
//St_id: josephst
//Date: 3/20/2019
package travelingSalesman;

public class HeapNode {
    private double key;  //the crime distance, use to organize the minHeap
    private int crimeNum;  //the index of the crime record in the crimeHeapIndex array (the order in which it was added to the heap)
    private CrimeRecord value;

    public HeapNode(double key, int crimeNum, CrimeRecord value) {
        this.key = key;
        this.crimeNum = crimeNum;
        this.value = value;
    }
    public double getKey() {
        return key;
    }
    public void setKey(double key) {
        this.key = key;
    }
    public int getCrimeNum() {
        return crimeNum;
    }
    public void setCrimeNum(int crimeNum) {
        this.crimeNum = crimeNum;
    }
    public CrimeRecord getCrimeRecord() {
        return value;
    }
    public void setCrimeRecord(CrimeRecord value) {
        this.value = value;
    }
}