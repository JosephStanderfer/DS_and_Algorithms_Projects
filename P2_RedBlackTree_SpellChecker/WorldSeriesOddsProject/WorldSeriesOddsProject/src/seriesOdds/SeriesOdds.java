//Author: Joseph Standerfer
//St_id: josephst
//Date: 2/11/2019

package seriesOdds;

public class SeriesOdds {
    //uses probability method below to calculate athe odds of a team
    //winning a series. Two integers are passed to probability method which
    //represent the "games needed to win" sent 
    public static void main(String[] args){
        SeriesOdds so = new SeriesOdds();
        System.out.println("World Series Odds Calculator");

        System.out.printf("%-10s %-10s %-10s\n", "Series", "Value", "Time (seconds)");
        int i = 0;
        int j = 0;
        
        //ints i and j are the games need by each team to win the series
        i = 1; j = 4;
        //record start time of probability method being called
        long start = System.currentTimeMillis();
        //pass ints to probabilty calculator
        float prob = so.probability(i,j);
        //record end time of probability method being called
        float time = (System.currentTimeMillis() - start) / 1000;
        //print metrics
        System.out.printf("%-10s %-10s %-10s\n", "P("+i+","+j+")", String.format("%.2f", prob), String.format("%.8f",time));

        i = 2; j = 3;
        start = System.currentTimeMillis() ;
        prob = so.probability(i,j);
        time = (System.currentTimeMillis() - start) / 1000;
        System.out.printf("%-10s %-10s %-10s\n", "P("+i+","+j+")", String.format("%.2f", prob), String.format("%.8f",time));

        i = 4; j = 7;
        start = System.currentTimeMillis();
        prob = so.probability(i,j);
        time = (System.currentTimeMillis() - start) / 1000;
        System.out.printf("%-10s %-10s %-10s\n", "P("+i+","+j+")", String.format("%.2f", prob), String.format("%.8f",time));

        i = 7; j = 6;
        start = System.currentTimeMillis();
        prob = so.probability(i,j);
        time = (System.currentTimeMillis() - start) / 1000;
        System.out.printf("%-10s %-10s %-10s\n", "P("+i+","+j+")", String.format("%.2f", prob), String.format("%.8f",time));

        i = 10; j = 12;
        start = System.currentTimeMillis();
        prob = so.probability(i,j);
        time = (System.currentTimeMillis() - start) / 1000;
        System.out.printf("%-10s %-10s %-10s\n", "P("+i+","+j+")", String.format("%.2f", prob), String.format("%.8f",time));

        i = 20; j = 23;
        start = System.currentTimeMillis();
        prob = so.probability(i,j);
        time = (System.currentTimeMillis() - start) / 1000;
        System.out.printf("%-10s %-10s %-10s\n", "P("+i+","+j+")", String.format("%.2f", prob), String.format("%.8f",time));

        i = 30; j = 15;
        start = System.currentTimeMillis();
        prob = so.probability(i,j);
        time = (System.currentTimeMillis() - start) / 1000;
        System.out.printf("%-10s %-10s %-10s\n", "P("+i+","+j+")", String.format("%.2f", prob), String.format("%.8f",time));

        i = 50; j = 40;
        start = System.currentTimeMillis();
        prob = so.probability(i,j);
        time = (System.currentTimeMillis() - start) / 1000;
        System.out.printf("%-10s %-10s %-10s\n", "P("+i+","+j+")", String.format("%.2f", prob), String.format("%.8f",time));

    }
    //uses dynamic programming and recursion to test the probabilty of a series being won by team i
    // Pre-Post conditions:
        // Precondition: i > 0 and j > 0
        // Postcondition: returns and removes object in first position, first one that was added
    public float probability(int i, int j){
        // i is the number of wins the team needs to win the series
        // j is the number of wins the opponents needs to win the series
        if(i == 0 && j > 0) {  // if team i has 0 games needed to win probability = 1 for team i
            return 1;
        }
        else if(i > 0 && j == 0) { // if team j has 0 games needed to win probability = 0 for team i
            return 0;
        }
        else if(i > 0 && j > 0){
            float p = ((probability(i - 1, j) + probability(i, j-1)) / 2);  //compute probability
            return p;
        } else {
            System.out.println("Error! cannot compute P(0,0)");
        }
        return -1;
    }

}
