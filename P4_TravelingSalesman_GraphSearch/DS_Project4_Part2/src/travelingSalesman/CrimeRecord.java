//Author: Joseph Standerfer
//St_id: josephst
//Date: 3/19/2019

package travelingSalesman;

public class CrimeRecord {
    private double x;
    private double y;
    private int time;
    private String street;
    private String offense;
    private String date;
    private int tract;
    private double lat;
    private double lon;

    public CrimeRecord(String record) {
        String[] crimeFields = record.split(",");
        this.x = Double.parseDouble(crimeFields[0]);
        this.y = Double.parseDouble(crimeFields[1]);
        this.time = Integer.parseInt(crimeFields[2]);
        this.street = crimeFields[3];
        this.offense = crimeFields[4];
        this.date = crimeFields[5];
        this.tract = Integer.parseInt(crimeFields[6]);
        this.lat = Double.parseDouble(crimeFields[7]);
        this.lon = Double.parseDouble(crimeFields[8]);
    }
    public double getDistance(CrimeRecord crime2){
        if(this == crime2){
            return 0; //return 0 if it is being compared with the same record. Avoids rounding errors
        }
        //convert feet to miles and return
        return 0.00018939 * Math.sqrt( Math.pow((this.x - crime2.getX()),2) + Math.pow((this.y - crime2.getY()),2) );
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public int getTime() {
        return time;
    }
    public String getStreet() {
        return street;
    }
    public String getOffense() {
        return offense;
    }
    public String getDate() {
        return date;
    }
    public int getTract() {
        return tract;
    }
    public double getLat() {
        return lat;
    }
    public double getLon() {
        return lon;
    }
}
