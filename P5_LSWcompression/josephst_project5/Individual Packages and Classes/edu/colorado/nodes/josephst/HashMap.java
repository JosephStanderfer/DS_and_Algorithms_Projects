//Author: Joseph Standerfer
//St_id: josephst
//Date: 4/8/2019

package edu.colorado.nodes.josephst;

public class HashMap {
    private SinglyLinkedList<KeyValuePair<String>>[] table = new SinglyLinkedList[127];
    private int itemCount;

    public HashMap(){
        //initialize table to length 127
        this.table = new SinglyLinkedList[127];
        //initialize each of the internal linkedLists
        for(int i = 0; i < table.length; i++){
            this.table[i] = new SinglyLinkedList<>();
        }
    }
    //retrieves and returns the value from teh corresponding keyValue pair
    //if key is not contained in table, this method returns 0
    public int get(String key){
        int value = -1;
        //retrieve storage list that matches the key hash
        int kHash = this.getHash(key);
        SinglyLinkedList sll = this.table[kHash];
        //reset list iterator before cycling through to find key
        sll.reset();
        while(sll.hasNext()){
            KeyValuePair pair = (KeyValuePair) sll.next();
            //if matching key is found return corresponding value
            if(pair.getKey().equals(key)){
                return pair.getValue();
            }
        }
        return value;
    }
    //returns boolean for whether the key passed in is contained within the table
    public boolean containsKey(String key){
        //retrieve storage list that matches the key hash
        int kHash = this.getHash(key);
        SinglyLinkedList sll = this.table[kHash];
        //reset list iterator before cycling through to find key
        sll.reset();
        while(sll.hasNext()){
            KeyValuePair pair = (KeyValuePair) sll.next();
            //if matching key is found return corresponding value
            if(pair.getKey().equals(key)){
                return true;
            }
        }
        return false;
    }
    //Hashes the String key and places the key value pair in the appropriate position in the map
    //returns true if key value pair meets all conditions to be stored
    public boolean put(String key, int value){
        //catch null key inputs, int values less than 0, and repeat keys
        if(key == null || value < 0 || this.containsKey(key)){
            return false;
        }
        //retrieve strings hash value using internal method
        int kHash = this.getHash(key);
        //create key value pair object to be stored
        KeyValuePair<String> newPair = new KeyValuePair(key, value);
        //store at end of linkedList in hashed table position
        table[kHash].addAtEndNode(newPair);
        //up count of items being stored
        itemCount++;
        return true;
    }
    //returns true if table does not contain any values
    public boolean isEmpty(){
        if(itemCount == 0) return true;
        else return false;
    }
    //sums the String's bytes to form a distinct number to hash
    private int getHash(String key){
        int bSum = 0;
        for(byte b : key.getBytes()){
            //do not allow byte sum to exceed int max
            if(bSum >= Integer.MAX_VALUE - b){
                break;
            } else {
                bSum += b;
            }
        }
        //use String sum and table length to find storage position
        return bSum%table.length;
    }
    //tests the hash maps methods
    public static void main(String[] args) {
        HashMap map = new HashMap();
        System.out.println("Empty Table?: " + map.isEmpty());
        System.out.println("\nAdd alphabet to table");
        for(char c = 'A'; c <= 'Z'; c++){
            System.out.print(c);
            map.put(""+c, (int)c);
        }
        System.out.println("\n\nGet values for select keys");
        System.out.println("Get 'B': " + map.get("B"));
        System.out.println("Get 'D': " + map.get("D"));
        System.out.println("Get '~': " + map.get("~")); //returns -1 (not contained in table)
    }
}
