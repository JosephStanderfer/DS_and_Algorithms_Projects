/**
 * Author : Joseph Standerfer
 * st_id  : josephst
 */

package seriesOdds;

/**
 *
 * @author josep
 */
public class DynamicSeriesOdds {

    public Float[][] t = new Float[100][100];  //stores probabilities for dynamic programming

    //uses probability method below to calculate the odds of a team
    //winning a series. Two integers are passed to probability method which
    //represent the "games needed to win" sent 
    public static void main(String[] args){
        DynamicSeriesOdds so = new DynamicSeriesOdds();
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
        if(t[i][j] != null) return t[i][j]; // uses probability stored in table t if it has already been calculated
        else if(i == 0 && j > 0) {  // if team i has 0 games needed to win probability = 1 for team i
            t[i][j] = Float.valueOf(1);  //save value to table t
            return 1;
        }
        else if(i > 0 && j == 0) { // if team j has 0 games needed to win probability = 0 for team i
            t[i][j] = Float.valueOf(0);  //save value to table t
            return 0;
        }
        else if(i > 0 && j > 0){
            float p = ((probability(i - 1, j) + probability(i, j-1)) / 2);  //compute probability
            t[i][j] = Float.valueOf(p);  //save value to table t
            return p;
        } else {
            System.out.println("Error! cannot compute P(0,0)");
        }
        return -1;
    }

}
