//name: Joseph Standerfer
//st_id: josephst


// Part 3 answer:
// The file with the merkle root A5A74A770E0C3922362202DAD62A97655F8652064CCCBE7D3EA2B588C7E07B58
// is "CrimeLatLonXY.csv"


package merkleTree;

import edu.colorado.nodes.josephst.SinglyLinkedList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;


public class MerkleTree {

    // the main routine for this class prompts the user for a fileName, checks the path to ensure its valid then calls
    // the other methods to construct the Merkle tree stored as "mt" and print the root
    public static void main(String[] args) {
        SinglyLinkedList mt = new SinglyLinkedList();
        String fileName = "";
        Scanner scanner = new Scanner(System.in);
        boolean validFileName = false;

        System.out.println();
        System.out.println("******** Merkle Tree Root Finder ***********");

        while(!validFileName){
            System.out.println("Enter '1' for smallFile.txt");
            System.out.println("Enter '2' for CrimeLatLonXY.csv");
            System.out.println("Enter '3' for CrimeLatLonXY1990_Size2.csv");
            System.out.println("Enter '4' for CrimeLatLonXY1990_Size3.csv");
            System.out.println("Enter a file name or select one from the list above: ");

            // if entry is number set filename based on selection
            if(scanner.hasNextInt()){
                switch(scanner.nextInt()){
                    case 1:
                        fileName = "smallFile.txt";
                        break;
                    case 2:
                        fileName = "CrimeLatLonXY.csv";
                        break;
                    case 3:
                        fileName = "CrimeLatLonXY1990_Size2.csv";
                        break;
                    case 4:
                        fileName = "CrimeLatLonXY1990_Size3.csv";
                        break;
                    default:
                        //if selection is invalid file set filename to empty string
                        //file path check will catch invalid entry
                        fileName = "";
                }
            } else {
                // if entry is not an integer save input as filename
                fileName = scanner.next().trim();
            }

            // check file name to ensure it exists otherwise repeat prompts
            File file = new File(fileName);
            if(file.exists() && !file.isDirectory()){
                validFileName = true;
            } else {
                System.out.println("Error! Invalid entry, please try again");
                System.out.println();
            }
        }

        //store the Singly Linked list of file lines to a new object and add it the the first row of the Merkle Tree "mt"
        SinglyLinkedList ln = readFile(fileName);
        mt.addAtFrontNode(ln);

        //send the first nodes of the merkle tree to the "findMTRoot" method to construct the rest of the rest of the Tree
        constructMerkleTree(mt);

        System.out.println("The Merkle Root of '" + fileName + "' is: ");
        System.out.println(mt.getLast());

        // provides prompt for viewing the full merkle tree
        System.out.println();
        System.out.println("Would you like to view the full Merkle Tree? (Y/N):");

        if(scanner.next().trim().toUpperCase().equals("Y")){
            System.out.println("************* Full Merkle Tree ************");
            mt.reset();

            //iterates through nodes of merkle tree and internal lists to print full tree
            while(mt.hasNext()){
                Object o = mt.next();
                if(o instanceof SinglyLinkedList){
                    SinglyLinkedList current = (SinglyLinkedList) o;
                    current.reset();
                    while(current.hasNext()){
                        System.out.printf("%10s...", (((String) current.next()).substring(0, 5)));
                    }
                    System.out.println();
                } else {
                    System.out.printf("%10s...",((String) o).substring(0, 5));
                }
            }
        }


    }
    //returns the hash for a String passed to it
    public static String h(String text) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash =
                digest.digest(text.getBytes(StandardCharsets.UTF_8));
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i <= 31; i++) {
            byte b = hash[i];
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
    // reads the file specified the argument and return a Singly
    // linked list of the lines within the file
    public static SinglyLinkedList readFile(String fileName){
        StringBuilder sbInput = new StringBuilder();
        SinglyLinkedList ln = new SinglyLinkedList();

        // read file using buffered reader
        try(BufferedReader br = new BufferedReader(new FileReader(new File(fileName)))){
            String nextLine;
            //read file 1 line at a time
            while((nextLine = br.readLine()) != null){

                //retrieve hash for each line and save it to the Linked List
                try {
                    ln.addAtEndNode(h(nextLine));
                } catch (NoSuchAlgorithmException nsa){
                    nsa.printStackTrace();
                }
            }
        } catch (IOException io) {
            io.printStackTrace();
        }

        // if there is an uneven number of nodes in the list, duplicate the last one
        if(ln.countNodes()%2 != 0){
            ln.addAtEndNode(ln.getObjectAt(ln.countNodes()-1));
        }

        return ln;
    }
    public static String constructMerkleTree(SinglyLinkedList mt){
        //reset merkle tree linked list so that the iterator points to the first node
        mt.reset();
        Object o = mt.next();

        // if the object stored in this node of the merkle tree is a list (ie. not hash)
        // construct the next node as a concatenation of the previous hashed
        while(o instanceof SinglyLinkedList){
            SinglyLinkedList current = (SinglyLinkedList) o;
            SinglyLinkedList nextList = new SinglyLinkedList();

            //reset iterator to first node of mt internal linked list
            current.reset();

            //if there are more that 2 nodes in the current list add new hashes to next list
            //else if there are only 2 nodes left this has will be the Merkle Tree root;
            if(current.countNodes() > 2){
                while(current.hasNext()){
                    String hash1 = (String) current.next();
                    String hash2;

                    // set hash2 to next node if it exists otherwise duplicate last
                    if(current.hasNext()){
                        hash2 = (String) current.next();
                    } else {
                        hash2 = hash1;
                    }

                    //find hash for concatenated hash codes 1 & 2.
                    try{
                        String concatHash = h(hash1 + hash2);

                        //add concatenated hashes to next linked list. will be saved to next node in Merkle Tree
                        nextList.addAtEndNode(concatHash);
                    } catch (NoSuchAlgorithmException nsa){
                        nsa.printStackTrace();
                    }
                }
                //add list of concatenated hashes to next node in the merkle tree
                mt.addAtEndNode(nextList);
            } else {
                // if current node of merkle tree contains just 2 hashes concatenate them and add the String directly
                // to the last node
                String hash1 = (String) current.next();
                String hash2;

                // get hash2 from next node or copy hash1 if no next node
                if(current.hasNext()){
                    hash2 = (String) current.next();
                } else {
                    hash2 = hash1;
                }

                try{
                    //concatenate hashes and add to last node in Merkle tree linked list
                    String concatHash = h(hash1 + hash2);
                    mt.addAtEndNode(concatHash);
                } catch (NoSuchAlgorithmException nsa){
                    nsa.printStackTrace();
                }
                // break while loop if its the last iteration
                break;
            }
            //set o equal to the latest list in the merkle tree
            // the while loop will check if it is a single linked list on next iteration
            o = mt.getLast();
        }

        return mt.getLast().toString();
    }
}
