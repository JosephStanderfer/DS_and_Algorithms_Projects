//name: Joseph Standerfer
//st_id: josephst

package mkEncryption;

import edu.colorado.nodes.josephst.SinglyLinkedList;

import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

public class MessageEncryptor {
    private BigInteger q;  //private key q used for encryption. > sum of w
    private BigInteger r;  //private key r used for encryption. between 0 and q. also coprime with q
    private SinglyLinkedList w; //private key w. super incrementing list
    public static SinglyLinkedList b; //public key. produced from w r and q
    public static boolean keysGenerated = false; //states whether the encr. keys have been generated yet

    public static void main(String[] args) {
        //create instance of the Message Encyptor class
        //the encryptions private keys will only be able to be accessed through this instance
        MessageEncryptor scrambler = new MessageEncryptor();

        //generate public and private keys.
        scrambler.keyGeneration();
        
        System.out.println();
        System.out.println("******  Merkle-Hellman Knapsack Encryptor ******");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a string and I will encrypt it as single large integer.");
        String input = scanner.nextLine().trim();
        
        //ensure input length is ess than 80 characters, otherwise reprompt
        while(input.length() > 80){
            System.out.println("Error! Input needs to be less than or equal to 80 characters.");
            System.out.println("Enter a string and I will encrypt it as single large integer.");
            input = scanner.nextLine().trim();
        }
        System.out.println("Clear text: \n\"" + input + "\"");

        System.out.println("Number of text bytes: " + input.getBytes().length );
           
        // send input to encrypt method to be converted to a BigInteger encryption
        BigInteger encrypted = scrambler.encrypt(input);
        
        // split encryted message into multiple lines so it is easier to view
        System.out.println("\"" + input + "\" is encrypted as: ");
        for(int i = 0; i <= encrypted.toString().length() / 75 ; i++){
            System.out.printf("%-75s", encrypted.toString().
                    substring(i*75, ((i*75 + 75) < encrypted.toString().length() ? (i*75 + 75) : encrypted.toString().length())) + "\n");
        }
        System.out.println();
        
        //send encrypted message to the decrypt method to be converted back into original message
        System.out.println("Result of Decryption: \"" + scrambler.decrypt(encrypted) + "\"");

    }
    public void keyGeneration(){
        // clear previous keys
        w = new SinglyLinkedList();
        b = new SinglyLinkedList();

        // create a super-increasing sequence of BigIntegers and save to private key "w"
        Random rand = new Random();
        BigInteger sum = BigInteger.valueOf(0);

        // adds 640 super increasing values to "w"
        for(int i = 0; i < 640; i++){
            // generates the next super increasing value
            // by adding a random integer between 1 and 21 to the previous sum
            BigInteger wi = BigInteger.valueOf(1  + rand.nextInt(20)).add(sum);
            w.addAtEndNode(wi);

            //updates the linkedList sum
            sum = sum.add(wi);
        }

        // q needs to be greater that the total sum of w
        this.q = BigInteger.valueOf(1  + rand.nextInt(1000)).add(sum);

        // r needs to be between 0 and q and coprime with q
        this.r = this.q.subtract(BigInteger.valueOf(1 + rand.nextInt(1000)));
        boolean coprime = false;

        // generate possible values for r within the 0,q range and check whether they are coprime with q
        while(!coprime){
            // check to make sure r is greater than 0
            if(this.r.compareTo(BigInteger.ZERO) < 0){
                //if not, reset r
                this.r = this.q.subtract(BigInteger.valueOf(1 + rand.nextInt(1000)));
            }

            //check gcd for r and q
            if(this.q.gcd(this.r).equals(BigInteger.ONE)){
                coprime = true;
            } else {
                this.r = this.r.subtract(BigInteger.valueOf(1 + rand.nextInt(1000)));
            }
        }

        //create public key b using w, r and q
        w.reset();
        while(w.hasNext()){
            b.addAtEndNode(((BigInteger) w.next()).multiply(this.r).mod(this.q));
        }

        keysGenerated = true;
    }
    // used the public key "b" to decrypt the message by taking the sum of 1 indexes in the message byte code
    // multiplied by the the corresponding index in the public key list, "b"
    public BigInteger encrypt(String message){
        if(!keysGenerated){
            System.out.println("Error. Encryption keys have not been generated");
            return null;
        }

        // save message binary to string
        String strBinary = new BigInteger(message.getBytes()).toString(2);

        // pad left of string with 0's to match w's size (640)
        strBinary = String.format("%0" + (640 - strBinary.length()) + "d", 0) + strBinary;

        //find sum of binary indexes multiplied by the indexes in public key b
        BigInteger c = BigInteger.ZERO;
        b.reset(); //set b's iterator to the head
        for(char ai : strBinary.toCharArray()){
            //find the individual products of the binary and matching indexes in b and add them to the total sum
            c = c.add(((BigInteger) b.next()).multiply(BigInteger.valueOf(ai-((int)'0'))));
        }

        return c;
    }
    public String decrypt(BigInteger c){
        //used to store decrypted bytes of message
        SinglyLinkedList decrypted = new SinglyLinkedList();

        //multiply the encryption by the modular inverse of r and q
        BigInteger wTotal = c.multiply(this.r.modInverse(this.q)).mod(this.q);

        //cycle through w from greatest to least and subtract values less than wTotal to find the corresponding "1" bytes
        for(int i = w.countNodes()-1; i >= 0; i--){
            BigInteger wi = (BigInteger) w.getObjectAt(i);  //need to use "getObjectAt" method because cycling through list bacward
            if(wTotal.compareTo(wi) >= 0){     
                decrypted.addAtFrontNode((byte) 1);  //save the decrypted 1 byte to message bytecode list
                wTotal = wTotal.subtract(wi);  // subtract wi from encrypted bigInteger
            } else {
                decrypted.addAtFrontNode((byte) 0); //save the decrypted 0 byte to message bytecode list
            }
            if(wTotal.compareTo(BigInteger.ZERO) <= 0){  //if nothing is remaining in encrypted sum, end loop
                break;
            }
        }
        //convert byte list back to string
        String text = new String(new BigInteger(decrypted.toString(), 2).toByteArray());

        return text;
    }
}
