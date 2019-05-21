//Author: Joseph Standerfer
//St_id: josephst
//Date: 3/19/2019

package travelingSalesman;

import edu.colorado.nodes.josephst.SinglyLinkedList;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class TSPSolver {

    static CrimeGraph crimeGraph;
    static String fileName = "CrimeLatLonXY1990.csv";
    static SinglyLinkedList[] mstNodeChildren;
    static SinglyLinkedList poTraversal;


    public static void main(String[] args) {
        //convert string dates to int arrays
        int[] startDDMMYY = retrieveDate("start");  //temp
        int[] endDDMMYY = retrieveDate("end");

        String[] recordsFound = constructCrimeGraph(startDDMMYY, endDDMMYY);
        System.out.println();

        //continue with program if records were found within date range
        if(recordsFound == null){
            System.out.println("There were no crime records found between " + startDDMMYY[1] + "/" + startDDMMYY [0] + "/" + startDDMMYY[2] +
                    " and " + endDDMMYY[1] + "/" + endDDMMYY [0] + "/" + endDDMMYY[2]);
        } else {
            System.out.println("Crime records between " + startDDMMYY[1] + "/" + startDDMMYY [0] + "/" + startDDMMYY[2] +
                    " and " + endDDMMYY[1] + "/" + endDDMMYY [0] + "/" + endDDMMYY[2]);
            for(String r : recordsFound){
                System.out.println(r);  //print each record in data range
            }
      
	        System.out.println();
	        //Part 1. Using an approximation algorithm to solve TSP
	        int[] hamiltonianCycle = approxTspTour(crimeGraph);
	
	        //Part 2. Finding an optimal solution to TSP - brute force
	        int[] optimalRoute = findOptimal(crimeGraph);
	
	        //Part 3. Displaying the output to Google Earth
	        printKML(hamiltonianCycle, optimalRoute);
        }
    }

    //asks user to enter a date, checks that the month, day, and year entries are valid, and returns an integer
    // array representation of the date
    public static int[] retrieveDate(String dateName){
        int[] dateInts = new int[3];
        boolean correctEntry = false;  //use to flag incorrect entries
        //continue with prompt until correct entry found
        while(!correctEntry) {
            System.out.println("Enter " + dateName + " Date (MM/DD/YY):");
            Scanner scanner =  new Scanner(System.in);
            String date = scanner.nextLine().trim();
            String[] dateFields = date.split("/");
            if(dateFields.length == 3){
                correctEntry = true;
                for(int i = 0; i < dateFields.length; i++){
                    try {
                        //convert fields to integer. re-prompt user if one of the entries is not an int
                        int field = Integer.parseInt(dateFields[i]);
                        //check that fields are valid date entries
                        if(field < 0 ||  //invalid if field is less than 0
                                ((i == 0 && field > 12)) || //invalid if month entry is greater than 12
                                ((i == 1 && field > 31)) || //invalid if day is greater than 31
                                ((i == 2 && field > 99))){   //invalid if year is greater than 99
                            correctEntry = false;
                        } else {
                            dateInts[i] = field;
                        }
                    } catch (NumberFormatException nfe){
                        correctEntry = false;
                    }
                }
            }
            if(!correctEntry){
                System.out.println("Invalid Entry! try again");
            }
        }
        //switch day and month entries so the date is arranged DDMMYY
        int monthHolder = dateInts[0];
        dateInts[0] = dateInts[1];
        dateInts[1] = monthHolder;

        return dateInts;
    }
    //searches the file for crimes that fall into the data range specified, constructs an array of CrimeRecords, and creates a graph from that array
    public static String[] constructCrimeGraph(int[] startDDMMYY, int[] endDDMMYY){
        CrimeGraph graph = null;
        StringBuilder sb = new StringBuilder();
        try(BufferedReader buffy =  new BufferedReader(new FileReader(new File(fileName)))){
            String record = null;
            buffy.readLine(); //skip header line
            while((record = buffy.readLine()) != null){ //continue reading file until end of lines
                String[] recordFields =  record.trim().split(",");  //separate record into fields
                String[] dateFields = recordFields[5].split("/");
                int[] crimeDate = new int[3];

                //***reorder date from mm/dd/yy to dd/mm/yy
                crimeDate[0] = Integer.parseInt(dateFields[1]);  //day
                crimeDate[1] = Integer.parseInt(dateFields[0]);  //month
                crimeDate[2] = Integer.parseInt(dateFields[2]);  //year

                //check whether the crime falls within the specified window
                boolean inWindow = true;
                //iterate backwards through dates Y>M>D (greatest to least) to see if they fall within start and end dates
                for(int i = crimeDate.length-1; i >= 0; i--){  //check month, day, and year
                    if (i == 2){
                        if (crimeDate[i] < startDDMMYY[i] || crimeDate[i] > endDDMMYY[i]) {
                            inWindow = false;
                            break;
                        }
                    } else {
                        if((crimeDate[i+1] == startDDMMYY[i+1] && crimeDate[i] < startDDMMYY[i]) ||
                                (crimeDate[i+1] == endDDMMYY[i+1] && crimeDate[i] > endDDMMYY[i])){
                            inWindow = false;
                            break;
                        }
                    }
                }
                // if crime date is in window add the record to the string builder
                // array of crimeRecords can only be created once count is found
                if(inWindow){
                    sb.append(record + "\n");
                }
            }
        } catch (IOException io){
            io.printStackTrace();
        }
        //if no records are found return null
        if(sb.toString().trim().isEmpty()){
            return null;
        }
        //split string builder into lines for each record
        String[] matchingRecords = sb.toString().trim().split("\n");
        //initialize CrimeRecords array with length of matchingRecords
        CrimeRecord[] cr = new CrimeRecord[matchingRecords.length];
        for(int i = 0; i < cr.length; i++){
            cr[i] = new CrimeRecord(matchingRecords[i]);
        }
        //initialize graph with array of crimeRecords
        crimeGraph = new CrimeGraph(cr);

        return matchingRecords;
    }
    public static SinglyLinkedList[] mstPrim(CrimeGraph graph, int root){
        //get the list of vertices within the graph
        CrimeRecord[] vertices = graph.getCrimes();
        //create storage arrays to be used for MST prim algorithm
        int[] parent = new int[vertices.length];  //stores parent of each node (parent = closest node found)
        double[] key = new double[vertices.length];  //stores key for each node (key = distance of parent node)

        MinHeap minHeap = new MinHeap(vertices.length);  //create a min Heap for sorting vertices distances

        //add all vertices to minHeap with the initial key set to max value
        for(int i = 0; i < vertices.length; i++){
            minHeap.insert(Double.MAX_VALUE, vertices[i]);
            key[i] = Double.MAX_VALUE;
        }
        //update 0 (root) node to distance (key) = 0
        minHeap.updateKey(0,0);
        key[0] = 0;
        parent[0] = -1; //used to specify nil
        while(!minHeap.isEmpty()){
            //extract vertices with minimum key from minHeap
            HeapNode u = minHeap.removeMin();

            //find light edge for each vertice still contained within minHeap
            for(int i = 0; i < vertices.length;i++){
                if(minHeap.contains(i)){
                    //check distance of each vertices in minHeap. Update key and parent if it is less than previous
                    double distance = u.getCrimeRecord().getDistance(vertices[i]);
                    if(distance < key[i]){
                        key[i] = distance;
                        parent[i] = u.getCrimeNum();
                        minHeap.updateKey(i,distance);//update minHeap with new key
                    }
                }
            }
        }

        //create a array of singlyLinkedLists to be used for the MST's pre-order traversal
        mstNodeChildren = new SinglyLinkedList[vertices.length];

        //loop and fill in child linked list array
        for(int vert  = 0; vert < mstNodeChildren.length; vert++){
            for(int p = 0; p < parent.length; p++) {
                //if linked list has not been initilized
                if (mstNodeChildren[vert] == null) {
                    mstNodeChildren[vert] = new SinglyLinkedList();
                }
                //iterate through vertices parent array and add them as children of the specified parent
                if (vert == parent[p]) {
                    mstNodeChildren[vert].addAtEndNode(p);
                }
            }
        }

        return mstNodeChildren;
    }
    public static void preOrderTraversal(){
        poTraversal = new SinglyLinkedList();
        preOrderTraversal(0);

    }
    public static void preOrderTraversal(int vertices){
        poTraversal.addAtEndNode(vertices) ; //add current vertices to the preOrder traversal list

        if(mstNodeChildren[vertices] == null) {
            return;
        }
        else {
            while(mstNodeChildren[vertices].hasNext()){  //iterate through nodes children
                preOrderTraversal((int) mstNodeChildren[vertices].next());
            }
        }
    }
    public static int[] approxTspTour(CrimeGraph cg){
        //call prim Algorithm to construct mst with crime 0 as the root
        mstPrim(cg, 0);

        //get preOrder traversal list of the mst
        preOrderTraversal();

        //add root to the end node as well to complete the hamiltonian cycle
        poTraversal.addAtEndNode(0);

        //get crimeList to match indexes with crimes, used to compute distances
        CrimeRecord[] crimes = cg.getCrimes();
        double totalDistance = 0;
        CrimeRecord previous = null;

        //create an array to hold the cycle
        int[]  hamiltonianCycle = new int[crimes.length + 1];

        //print cycle and total distance traveled
        System.out.println("Hamiltonian Cycle (not necessarily optimum):");
        int idx = 0;
        //cycle through list, print vertices and save to array
        while(poTraversal.hasNext()){
            int currentCrimeIdx = (int) poTraversal.next();  //get next node in list
            hamiltonianCycle[idx] = currentCrimeIdx;  //save vertices to array
            idx++;
            System.out.print(currentCrimeIdx + " ");
            if(previous != null){
                totalDistance += previous.getDistance(crimes[currentCrimeIdx]);  //add distance ot next node to total
            }
            previous = crimes[currentCrimeIdx]; //update previous crime location
        }
        System.out.println();
        System.out.println("Length of cycle: " + totalDistance + " miles");

        return  hamiltonianCycle;
    }
    public static int[] findOptimal(CrimeGraph cg){

        CrimeRecord[] vertices = cg.getCrimes();

        //create an array of crimeRecord identifiers for the permutation. Leave out root, 0.
        int[] range = new int[vertices.length-1];
        //fill with range 1 - crimes.length
        for(int i = 0; i < range.length; i++) range[i] = i + 1;
        //initialize list to route permutations
        SinglyLinkedList allRoutes = new SinglyLinkedList();
        //fill out list
        allPermutations(allRoutes, range, 0);

        //create placeholders to store shortest route
        double minDist = Double.MAX_VALUE;
        int[] minRoute = new int[cg.getCrimes().length+1];

        //find length of every route and save shortest
        int[] currRoute;
        int[] fullRoute = new int[cg.getCrimes().length+1];
        while(allRoutes.hasNext()){
            currRoute = (int[]) allRoutes.next(); //get next route
            double distance = 0;  //set starting distance back to 0

            //iterate through route and compute total distance between vertices
            CrimeRecord previousVert = vertices[0];
            for(int i = 0; i < currRoute.length; i++){
                fullRoute[i+1] = currRoute[i];
                distance += previousVert.getDistance(vertices[currRoute[i]]);  //add distance from last to current vertices
                previousVert = vertices[currRoute[i]];  //update previous
            }
            //finally add distance back to vertices 0
            distance += previousVert.getDistance(vertices[0]);

            //if distance is less than all others then save it to minRoute
            if(distance < minDist){
                minDist = distance;
                minRoute = Arrays.copyOf(fullRoute, fullRoute.length);
            }
        }
        System.out.println();
        System.out.println("Looking at every permutation to find the optimal solution");
        System.out.println("The best permutation ");
        for(int i = 0; i < minRoute.length; i++){
            System.out.print(minRoute[i] + " ");
        }
        System.out.println("\n");
        System.out.println("Optimal Cycle length = " + minDist + " miles");

        return minRoute;
    }

    public static void allPermutations(SinglyLinkedList sll, int[] route, int currIdx) {
        //for each index not filled iterate through and swap
        for(int i = currIdx; i < route.length; i++){
            int holder = route[currIdx];
            route[currIdx] = route[i];
            route[i] = holder;

            //call permutation again for next index
            allPermutations(sll, route, currIdx + 1);

            //return both indexes in array to original
            route[i] = route[currIdx];
            route[currIdx] = holder;
        }
        //if array is filled add to list
        if (currIdx == route.length-1) {
            sll.addAtEndNode(Arrays.copyOf(route, route.length));
        }
    }
    public static void printKML(int[] hamiltonian, int[] optimalRoute){
        String opener = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<kml xmlns=\"http://earth.google.com/kml/2.2\">\n" +
                "<Document>\n" +
                "<name>Pittsburgh TSP</name><description>TSP on Crime</description><Style id=\"style6\">\n" +
                "<LineStyle>\n" +
                "<color>73FF0000</color>\n" +
                "<width>5</width>\n" +
                "</LineStyle>\n" +
                "</Style>\n" +
                "<Style id=\"style5\">\n" +
                "<LineStyle>\n" +
                "<color>507800F0</color>\n" +
                "<width>5</width>\n" +
                "</LineStyle>\n" +
                "</Style>\n" +
                "<Placemark>\n" +
                "<name>TSP Path</name>\n" +
                "<description>TSP Path</description>\n" +
                "<styleUrl>#style6</styleUrl>\n" +
                "<LineString>\n" +
                "<tessellate>1</tessellate>\n" +
                "<coordinates>\n";
        String firstCoordinates = getCoordinateString(hamiltonian);
        String middle = "</coordinates>\n" +
                "</LineString>\n" +
                "</Placemark>\n" +
                "<Placemark>\n" +
                "<name>Optimal Path</name>\n" +
                "<description>Optimal Path</description>\n" +
                "<styleUrl>#style5</styleUrl>\n" +
                "<LineString>\n" +
                "<tessellate>1</tessellate>\n" +
                "<coordinates>\n";
        String secondCoordinates = getCoordinateString(optimalRoute);
        String closer = "</coordinates>\n" +
                "</LineString>\n" +
                "</Placemark>\n" +
                "</Document>\n" +
                "</kml>\n";

        try(FileWriter fw = new FileWriter("PGHCrimes.kml")){
            fw.write(opener);
            fw.write(firstCoordinates);
            fw.write(middle);
            fw.write(secondCoordinates);
            fw.write(closer);
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getCoordinateString(int[] route){
        StringBuilder sb = new StringBuilder();
        CrimeRecord[] crimes = crimeGraph.getCrimes();

        for(int i= 0; i < route.length; i++){
            sb.append(crimes[route[i]].getLon() + "," + crimes[route[i]].getLat() + "\n");
        }
        return sb.toString();
    }
}

