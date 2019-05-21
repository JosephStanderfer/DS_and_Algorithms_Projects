//Author: Joseph Standerfer
//St_id: josephst
//Date: 2/10/2019

package rbTree;

import java.io.*;
import java.util.Scanner;

public class RedBlackTreeSpellChecker {
    public static String scFilename = "shortwords.txt";
    public static RedBlackTree dict = new RedBlackTree();
    
    //implements a spell checking program using a RedBlackTree as dictionary storage
    public static void main(String[] args) {
        System.out.println();
        System.out.println("*** RBTree Spell Checking Program ***");
        fileNamePrompt();

        System.out.println();
        System.out.println("java RedBlackTreeSpellChecker " + scFilename);
        System.out.println("loading a tree of english words from " + scFilename);
        loadDictionary(scFilename);
        int size = dict.getSize();
        System.out.println("Red Black Tree is loaded with " + size);
        System.out.println("Initial height is " + dict.height());
        double worstT = 2 *(Math.log(size + 1) / Math.log(2));
        System.out.println("Never worse than 2 * Lg(n+1) = " + worstT);

        Scanner scanner = new Scanner(System.in);
        //prompt user for input
        mainMenu();
        
        //shuttle program to option picked using the fist character
        boolean quit = false;
        while(!quit){
            String[] input = scanner.nextLine().trim().split(" ");
            switch(input[0].toLowerCase()){
                case "d":
                    System.out.println("Level order traversal");
                    dict.levelOrderTraversal();
                    break;
                case "s":
                    System.out.println("In order traversal");
                    dict.inOrderTraversal();
                    break;
                case "r":
                    System.out.println("Reverse order traversal");
                    dict.reverseOrderTraversal();
                    break;
                case "c":
                    if(input.length != 2){
                        System.out.println("Invalid Entry. Please try again");
                    } else {
                        String searchTerm = input[1].trim();
                        if(dict.contains(searchTerm)){
                            System.out.println("Found " + searchTerm + " after " + dict.getRecentCompares() + " comparisons");
                        } else {
                            System.out.println(searchTerm + " Not in dictionary. Perhaps you mean \n" + dict.closeBy(searchTerm));
                        }
                    }
                    break;
                case "a":
                    if(input.length != 2){
                        System.out.println("Invalid Entry. Please try again");
                    } else {
                        String searchTerm = input[1].trim();
                        if(dict.contains(searchTerm)){
                            System.out.println(searchTerm + " already in dictionary. Was not added");
                        } else {
                            dict.insert(searchTerm);
                            System.out.println(searchTerm + " was added to the dictionary");
                        }
                    }
                    break;
                case "f":
                    if(input.length != 2){
                        System.out.println("Invalid Entry. Please try again");
                    } else {
                        String fileName = input[1].toLowerCase().trim();
                        // check file name to ensure it exists otherwise repeat prompts
                        File file = new File(fileName);
                        if(file.exists() && !file.isDirectory()){
                            boolean error = spellCheckFile(fileName);
                            if(!error){
                                System.out.println("No spelling errors found.");
                            }
                        } else {
                            System.out.println("Error! Filename '" + fileName + "' could not be found");
                        }
                    }
                    break;
                case "i":
                    System.out.println("The dictionary tree's diameter is: " + dict.treeDiameter());
                    break;
                case "m":
                    mainMenu();
                    break;
                case "!":
                    System.out.println("Bye.");
                    quit = true;
                    break;
                default:
                    System.out.println("Invalid Entry. Please try again.");
                    break;
            }
        }
    }
    // Pre-Post conditions:
        // Precondition: scFilename != null and exists
        // Postcondition: loads dictionary rbTree with words in file
    public static void loadDictionary(String scFilename){
        try(BufferedReader bf = new BufferedReader(new FileReader(new File(scFilename)))){
            boolean eof = false;
            //keep reading till end of file
            while(!eof){
                String word = bf.readLine();
                if(word == null){
                    eof = true;  //if line read from file is null we've reached the end of the file. break loop
                } else{
                    word = word.trim();
                    if(!dict.contains(word) && word != ""){ //don't add word to dict if its already included or is empty
                        dict.insert(word);
                    }
                }
            }
        } catch(IOException ioe){
            ioe.printStackTrace();
        }
    }
    // Pre-Post conditions:
        // Precondition: filename passed needs to exist
        // Postcondition: calls loadDictionary method with the fileName that is passed in by user
    public static void fileNamePrompt(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the filename of the dictionary you'd like to use: ");


        //prompt user for filename entry. Reprompt if entry is invalid
        String fileName = "";
        boolean validFileName = false;
        while(!validFileName){
            System.out.println("Enter the filename of the dictionary you'd like to use or select one from the list above: ");
            System.out.println("> Enter '1' for shortwords.txt");
            System.out.println("> Enter '2' for words.txt");

            // if entry is number set filename based on selection
            if(scanner.hasNextInt()){
                switch(scanner.nextInt()){
                    case 1:
                        fileName = "shortwords.txt";
                        break;
                    case 2:
                        fileName = "words.txt";
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
        //save fileName entered to the spell checker filename declared in the class header
        scFilename = fileName;
    }
    // Pre-Post conditions:
        // Postcondition: displays options for user on console
    public static void mainMenu(){
        //prompt user for input
        System.out.println();
        System.out.println("Legal commands are:");
        System.out.println("d    display the entire word tree with a level order traversal.");
        System.out.println("s    print the words of the tree in sorted order (using an inorder traversal).");
        System.out.println("r    print the words of the tree in reverse sorted order (reverse inorder traversal).");
        System.out.println("c   <word> to spell check this word.");
        System.out.println("a   <word> add this word to tree.");
        System.out.println("f   <fileName> to check this text file for spelling errors.");
        System.out.println("i    display the diameter of the tree.");
        System.out.println("m    view this menu.");
        System.out.println("!    to quit.");
    }
    // Pre-Post conditions:
        // Precondition: dict has been loaded && fileName != null && filename exists
        // Postcondition: displays mispelled words in file (not found in dict) to console. 
        //                  returns True is mispellings were found and false if not
    public static boolean spellCheckFile(String fileName){
        boolean spellingError = false;

        try(BufferedReader bf = new BufferedReader(new FileReader(new File(fileName)))){
            boolean eof = false;
            //keep reading till end of file
            while(!eof){
                String text = bf.readLine();
                if(text == null){
                    eof = true;  //if line read from file is null we've reached the end of the file. break loop
                } else if (!text.isEmpty()){
                    String[] words = text.trim().replaceAll("[^a-zA-Z ]", "").split(" "); //replace punctuation and split into words
                    for(String w : words){
                        w = w.replaceAll("\\s+","");
                        if(!dict.contains(w) && !w.equals("")){ //check if file word is found in dictionary. print line if not
                            System.out.println("'" + w + "' was not found in dictionary.");
                            spellingError = true;
                        }
                    }
                }
            }
        } catch(IOException ioe){
            ioe.printStackTrace();
        }
        return spellingError;
    }

}
