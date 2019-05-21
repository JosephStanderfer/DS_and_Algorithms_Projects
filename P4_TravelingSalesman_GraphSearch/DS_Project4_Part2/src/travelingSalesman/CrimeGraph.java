//Author: Joseph Standerfer
//St_id: josephst
//Date: 3/19/2019

package travelingSalesman;

public class CrimeGraph {

    private CrimeRecord[] crimes; //stores all crimeRecords passed in to constructor, used as the graph vertices
    private double[][] distances; //distance between crimes in miles

    //construct graph using array of crimeRecords
    public CrimeGraph(CrimeRecord[] cr){
        this.crimes = cr;
        this.distances = new double[this.crimes.length][this.crimes.length];
        //create distance matrix between crimeRecords
        findDistances();
    }
    //computes distance matrix using pythagorean theorem
    public void findDistances(){
        //compare each crimes location vs all others
        for(int i = 0; i < crimes.length; i++){
            for(int j = 0; j < crimes.length; j++){
                if(i == j){
                    //the distance between an entry and itself is 0
                    this.distances[i][j] = 0;
                } else{
                    //use pythagorean theorem to find distances between points in miles
                    this.distances[i][j] = crimes[i].getDistance(crimes[j]);
                }
            }
        }
    }
    public CrimeRecord[] getCrimes() {
        return crimes;
    }
    public double[][] getDistances() {
        return distances;
    }
}
