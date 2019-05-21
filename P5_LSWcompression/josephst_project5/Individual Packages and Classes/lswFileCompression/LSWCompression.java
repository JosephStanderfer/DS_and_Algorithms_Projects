//Author: Joseph Standerfer
//St_id: josephst
//Date: 4/7/2019

package lswFileCompression;

import edu.colorado.nodes.josephst.HashMap;

import java.io.*;
import java.util.Scanner;

public class LSWCompression {
    public static HashMap StringValueTable = new HashMap();
    public static String[] ValueStringRA = new String[((int)Math.pow(2,12))]; //set length to max 12-bit int value
    public static int bytesRead = 0;
    public static int bytesWritten = 0;
    
    //2) describe how your program is working on ASCII files and on binary files:
    // This program performs compression by reading in bytes one at a time, and adding the character representations to a string. When new string sequences
    // are found they are assigned a 12-bit code and saved. The strings and their 12-bit code representation (saves as an integer value) are stored in a hashmap
    // using the string as a key. The 12 bit byte code representations for each of the string sequences are written out to the compressed file in 8-bit chunks
    // The 12-bits are divided into 8 bits using substrings of the bit representation. The first of every 2 codewords to be written out has 4 bits remaining
    // (8 bits written and 4 leftover). These 4 bits are added on to the beginning of the next codeword to be written out to make 16 bits (2 8-bit write).
    // when the number of string representations exceeds 2^12, they can no longer be coded with 12-bits. The compressor must clear the hashmap and start assigning
    // new codes to the following strings. Decompression is done in a similar manner. However the 12-bit codes are now being read into the program to be matched
    // with their original string representation. The string sequences found are stored in an array this time under the index of the 12-bit code word. Since
    // the file can only be read in 8 bit chunks, 2 codewords are found every 3 byte reads. They are pieced together  using substrings of the bit representations
    //
    //2) Explain whether your program works for both cases and state the degree of compression obtained on words.html, CrimeLatLonXY1990.csv, and 01_Overview.mp4:
    // My program works for all 3 cases (html, csv, and mp4).
    //			html compression ratio 0.429
    //				bytes read = 2493498, bytes written = 1070000
    //			csv compressed ratio %
    // 			MP4 compressed ratio 1.35 (it grows for binary files)
    //				bytes Read = 25008838, bytes Written = 33771078
    
    public static void main(String[] args) throws IOException{

        System.out.println("\n***** Lempel-Ziv Welch Compression Program *****\n");
        System.out.println("Enter the task you would like to perform in the below format\n or press 'return' to quit");
        System.out.println("java LZWCompression <'-c' or '-d'> <'' or '-v'> <input filename> <output filename>");
        System.out.println();
        boolean endflag = false;
        
        //keep asking user for input until they press "return" and end flag is triggered
        while(!endflag){
            Scanner scanner = new Scanner(System.in);
            boolean validEntry = false;
            String userInput = "";
          //keep asking user for input until valid entry is given or they press "return"
            while(!validEntry) {
                userInput = scanner.nextLine().trim();
                if(userInput.trim().equals("")){
                    System.out.println("Good Bye!");
                    endflag = true;
                    break;
                }
                //split input by spaces to check individual entries
                String inputs[] = userInput.split("\\s+");
                //verbose input length
                //send input to function to determine validity
                validEntry = checkInputs(inputs);
            }
            if(endflag) break;
            String input[] = userInput.split("\\s+");
            if (input.length == 6) {
                //if input length if 6 the user wants to use the verbose option
                if(input[2].equalsIgnoreCase("-c")){
                    lSW_Compress(input[4].trim(), input[5].trim());
                    System.out.println("bytes read = " + bytesRead + ", bytes written = " + bytesWritten);

                } else if (input[2].equalsIgnoreCase("-d")){
                    lSW_Decompress(input[4].trim(), input[5].trim());
                    System.out.println("bytes read = " + bytesRead + ", bytes written = " + bytesWritten);


                }
            } else {
                //if input length is 5, the user does not want to use the verbose option,
                if(input[2].equalsIgnoreCase("-c")){
                    lSW_Compress(input[3].trim(), input[4].trim());


                } else if (input[2].equalsIgnoreCase("-d")){
                    lSW_Decompress(input[3].trim(), input[4].trim());

                }
            }
        }

    }
    //input will be received from the user in the following forms:
    //>>> java LZWCompression -c –v shortwords.txt zippedFile.txt
    //>>> java LZWCompression -d –v zippedFile.txt unzippedFile.txt
    // this function checks that the file names are valid and other entries are expected return true
    // if everything checks out and false otherwise
    public static boolean checkInputs(String[] input){
        if (input.length < 5 || input.length > 6) {
            //invalid input length
            System.out.println("Error! Invalid entry length. Please try again");
            return false;
        } else if (input.length == 6) {
            //check inputs are right length before substrings
            if(input[2].length()<2 || input[3].length()<2 ){
                System.out.println("Error! Invalid entry. Please try again");
                return false;
            }

            //if input length if 6 the user likely wants to use the verbose option,
            //check that the "-v" is in place. Also chck that -d or -c is in position
            if(!input[3].substring(input[3].length()-1).equalsIgnoreCase("v") ||
                    !(input[2].substring(input[2].length()-1).equalsIgnoreCase("c") ||
                            input[2].substring(input[2].length()-1).equalsIgnoreCase("d"))){
                System.out.println("Error! Invalid entry. Please try again");
                return false;
            }
            //check that input filename is valid
            String inputFile = input[4];
            File file = new File(inputFile);
            if (file.exists() && !file.isDirectory()) {
            } else {
                System.out.println("Error! Invalid filename. Please try again");
                return false;
            }
        } else {
            //check inputs are right length before substrings
            if(input[2].length()<2){
                System.out.println("Error! Invalid entry. Please try again");
                return false;
            }

            //if input length is 5, the user does not want to use the verbose option,
            //check that the "-v" is in place. Also chck that -d or -c is in position
            if(!(input[2].substring(input[2].length()-1).equalsIgnoreCase("c")
                    || input[2].substring(input[2].length()-1).equalsIgnoreCase("d"))){
                System.out.println("Error! Invalid entry. Please try again");
                return false;
            }
            //check that input filename is valid
            String inputFile = input[3];
            File file = new File(inputFile);
            if (file.exists() && !file.isDirectory()) {
            } else {
                System.out.println("Error! Invalid filename. Please try again");
                return false;
            }
        }
        return true;
    }
    	
    public static void lSW_Compress(String inputFileName, String outputFileName) throws IOException{
        bytesRead = 0;
        bytesWritten = 0;
        //enter all possible 1 byte combinations into table with char representations as key;
        int cwCounter = 0; //keeps track of the codewords already assigned
        for(int i = 0; i < 256; i++) {
            StringValueTable.put(String.valueOf((char) i), cwCounter);
            cwCounter++;
        }

        //read bytes in file, find or assign 12-bit key, and output to compressed file
        DataInputStream in =
                new DataInputStream(
                        new BufferedInputStream(
                                new FileInputStream(inputFileName)));
        DataOutputStream out =
                new DataOutputStream(
                        new BufferedOutputStream(
                                new FileOutputStream(outputFileName)));
        String s = "";
        int remaining = 0;
        String remStr = ""; //holds carry over bits from odd codeword file writes
        try {
            //read first character from w into string s
            bytesRead++;
            s = String.valueOf((char) (0xFF & in.readByte()));

            int code = 0;
            //while any input left
            while(true) {
                String c = String.valueOf((char) (0xFF & in.readByte()));
                bytesRead++;
                //if "s + c" is in the table
                if(StringValueTable.containsKey(s.concat(c))){
                    s = s.concat(c);
                } else {
                    //if s+c is not in table write 12-bit code for s into compressed file then store s+c in table
                    code = StringValueTable.get(s);

                    String codeStr = Integer.toBinaryString(code);
                    while (codeStr.length() < 12) {
                        codeStr = "0".concat(codeStr);
                    }

                    //the first byte of each 3 byte cycle with have carryOver
                    //write 1 8-bit byte to file then leave 4-bits to carry over to next codeword print
                    if(bytesWritten%3 == 0){
                        out.writeByte((byte) Integer.parseInt(codeStr.substring(0, 8), 2));
                        bytesWritten++;
                        //add four 0 bits to right side so it can be added to next write byte
                        remStr = codeStr.substring(8, 12);
                    } else {
                        //first write-out will be last 4 bits of previous codeword and first 4 of this codeword
                        out.writeByte((byte) Integer.parseInt(remStr.concat(codeStr.substring(0, 4)), 2));
                        remStr = "";
                        bytesWritten++;
                        //second byte write-out will be remaining 8 bits of this codeword
                        out.writeByte((byte) Integer.parseInt(codeStr.substring(4, 12), 2));
                        bytesWritten++;
                    }

                    //if code word counter exceeds 12-bit maximum (2^12-1), then no more 12bit codes can be created
                    // reinitialize the table
                    if(cwCounter >= Math.pow(2,12)-1){
                        StringValueTable = new HashMap();
                        cwCounter = 0;
                        //enter all possible 1 byte combinations into table with char representations as key;
                        for(int i = 0; i < 256; i++) {
                            StringValueTable.put(String.valueOf((char) i), cwCounter);
                            cwCounter++;
                        }
                    }
                    StringValueTable.put(s +c,cwCounter);
                    cwCounter++;
                    s = c;

                }
            }
        }
        catch(EOFException e) {
            in.close();
            //write out last remaining codeword and any carryover bits
            int code = StringValueTable.get(s);
            String codeStr = Integer.toBinaryString(code);
            while (codeStr.length() < 12) {
                codeStr = "0".concat(codeStr);
            }

            //if first of the 3 byte cycle then there is no carryover from last codeword
            //write first 8 bits then remaining 4 with 4 0's added to the end
            if(bytesWritten%3 == 0){
                out.writeByte((byte) Integer.parseInt(codeStr.substring(0, 8), 2));
                out.writeByte((byte) (Integer.parseInt(codeStr.substring(8, 12), 2) * ((int) Math.pow(2,4))));
                bytesWritten += 2;
            } else {
                //first write-out will be last 4 bits of previous codeword and first 4 of this codeword
                out.writeByte((byte) Integer.parseInt(remStr.concat(codeStr.substring(0, 4)), 2));
                remStr = "";
                bytesWritten++;
                //second byte write-out will be remaining 8 bits of this codeword
                out.writeByte((byte) Integer.parseInt(codeStr.substring(4, 12), 2));
                bytesWritten++;
            }
            out.close();
        }

    }
    public static void lSW_Decompress(String inputFileName, String outputFileName) throws IOException{
        //enter all symbols into the table;
        int cwCounter = 0; //keeps track of the codewords already assigned
        bytesRead = 0;
        bytesWritten = 0;

        //enter all possible chars into table with int (12-bit) representations as key;
        for(int i = 0; i < 256; i++) {
            ValueStringRA[cwCounter] = String.valueOf((char) i);
            cwCounter++;
        }
        //read bytes in file, find or assign 12-bit key, and output to compressed file
        DataInputStream in =
                new DataInputStream(
                        new BufferedInputStream(
                                new FileInputStream(inputFileName)));
        DataOutputStream out =
                new DataOutputStream(
                        new BufferedOutputStream(
                                new FileOutputStream(outputFileName)));
        int cwIndex = 0;
        int priorcwIndex = 0;
        String codeword = "";
        String priorcodeword = "";
        //instantiate storage objects to be used
        try {
            //read in first codeword, 12 of 16 bytes
            String strOne = bitString(in.readByte());
            String strTwo = bitString(in.readByte());
            bytesRead += 2;

            //break two bytes into 1 codeword and 4 remaining bits
            priorcwIndex = Integer.parseInt(strOne.substring(0, 8) + strTwo.substring(0, 4), 2);
            String remainingStr = strTwo.substring(4, 8); //holds carry over bits from odd codeword file writes

            //split codeword into chars and write out bytes to file
            for(char ch : ValueStringRA[priorcwIndex].toCharArray()){
                out.writeByte((byte) ch);
                bytesWritten++;
            }

            //while(any input left){
            while(true) {
                //read in 8-bits at a time
                strOne = bitString(in.readByte());
                bytesRead++;

                //find codeword for each 12 bits
                if(bytesRead%3 == 0){
                    //last byte of every 3 byte cycle. 1 codeword translated and leave nothing remaining
                    //cw index is last 4 bits from last byte and all 8 from this one
                    cwIndex = Integer.parseInt(remainingStr.substring(0, 4) + strOne.substring(0, 8), 2);
                    remainingStr = "";
                } else if (bytesRead%3 == 1){
                    //first byte of every 3 byte cycle. nothing gets written, 8 bits remaining
                    cwIndex = -1;
                    //left justify the 8 remaining bits by 4-bits
                    remainingStr = strOne;
                } else {
                    //second byte of every 3 byte cycle. 1 codeword translated, 4 bits remaining
                    cwIndex = Integer.parseInt(remainingStr.substring(0, 8) + strOne.substring(0, 4), 2);
                    remainingStr = strOne.substring(4, 8);
                }
                //if new codeWord index is found
                if(cwIndex >= 0){
                    //check whether codeword index has been filled
                    if(ValueStringRA[cwIndex] == null){
                        priorcodeword = ValueStringRA[priorcwIndex];
                        //if no string entered in the cw position
                        String output = priorcodeword + ((char) priorcodeword.charAt(0));
                        //if codeword table is full, create a new one and fill it with the ascii chars
                        if(cwCounter >= Math.pow(2,12)-1){
                            cwCounter = 0;
                            ValueStringRA = new String[((int)Math.pow(2,12))];
                            //enter all possible chars into table with int (12-bit) representations as key
                            for(int i = 0; i < 256; i++) {
                                ValueStringRA[cwCounter] = String.valueOf((char) i);
                                cwCounter++;
                            }
                        }
                        //save new codeword
                        ValueStringRA[cwCounter] = output;
                        cwCounter++;

                        
                        //output codeword
                        out.writeBytes(output);
                        bytesWritten += output.getBytes().length; //add written bytes to counter
                    } else {
                        // else if codeword index has already been filled
                        priorcodeword = ValueStringRA[priorcwIndex];
                        codeword = ValueStringRA[cwIndex];

                        //if codeword table is full, create a new one and fill it with the ascii chars
                        if(cwCounter >= Math.pow(2,12)-1) {
                            cwCounter = 0;
                            ValueStringRA = new String[((int) Math.pow(2, 12))];
                            //enter all possible chars into table with int (12-bit) representations as key
                            for (int i = 0; i < 256; i++) {
                                ValueStringRA[cwCounter] = String.valueOf((char) i);
                                cwCounter++;
                            }
                        }
                        //save new codeword
                        ValueStringRA[cwCounter] = priorcodeword + ((char) codeword.charAt(0));
                        cwCounter++;

                        out.writeBytes(codeword); //write codeword out to file
                        bytesWritten += codeword.getBytes().length; //add written bytes to counter
                    }
                    priorcwIndex = cwIndex;
                }
            }
        }
        catch(EOFException e) {
            in.close();
            out.close();
        }
    }
    // to transform the byte into string
    public static String bitString(byte b) {
        String str = Integer.toBinaryString(b);
        // when less than 8
        while (str.length() < 8) {
            str = "0".concat(str);
        }
        str = str.substring(str.length() - 8);
        return str;
    }
}
