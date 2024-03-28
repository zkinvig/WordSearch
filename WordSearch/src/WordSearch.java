import java.util.*;
import java.util.regex.Pattern;

class CantAddWordException extends Exception {
    public CantAddWordException(String message) {
        super(message);
    }
}

public class WordSearch {
    /*
    TO DO
    1. Need to overhaul exception handling to make it neater
    2. Consider adding more global variables
    3. Might add a Utils file to hold more general methods
    4. Improve upon algorithm to be faster and generate better word searches
    5. Write more tests
    6. Write a reverse method that finds words when given a word search
    */
    
    //Colour codes for changing output text colours
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_RESET = "\u001B[0m";

    //List of coordinates that contain the words to find
    static ArrayList<int[]> coordsToHighlight = new ArrayList<>();

    //boolean arrayListContainsIntArray(arrayList, target)
    //Returns true if target is found in arrayList, false otherwise
    public static boolean arrayListContainsIntArray(ArrayList<int[]> arrayList, int[] target) {
        for (int[] array : arrayList) {
            if (Arrays.equals(array, target)) {
                return true;
            }
        }
        return false;
    }

    //void printSearch(search, size, reveal)
    //Prints the given word search
    //If reveal is true, will colour the tiles containing the words in purple
    //Otherwise, all tiles will be the default colour
    public static void printSearch(char[][] search, int size, boolean reveal){
        for(int y=0; y < size; y++){
            for(int x=0; x < size; x++){
                if(arrayListContainsIntArray(coordsToHighlight,new int[]{x,y}) && reveal){
                    //Prints purple for the answer key
                    System.out.print(ANSI_PURPLE+ search[x][y]+"  ");
                }else {
                    System.out.print(ANSI_RESET+search[x][y] + "  ");
                }
            }
            System.out.println();
        }
    }

    //char[][] fillRandom(search, size)
    //Fills all empty tiles in the word search with a random capital letter and returns the resulting word search
    public static char[][] fillRandom(char[][] search, int size){
        for(int i=0; i < size; i++){
            for(int y=0; y < size; y++){
                if(search[i][y] == '\0'){
                    search[i][y] = generateRandomCapitalLetter();
                }
            }
        }
        return search;
    }

     //char generateRandomCapitalLetter()
    //Returns any random capital letter
    public static char generateRandomCapitalLetter() {
        Random random = new Random();
        int randomNumber = random.nextInt(26);
        return (char) ('A' + randomNumber);
    }

    //int getNumberOfLetters(words)
    //Returns the number of letters total in an array of strings
    public static int getNumberOfLetters(String[] words){
        int sum= 0;
        for(String word: words){
            sum += word.length();
        }
        return sum;
    }

    //char[][] addWord(search, size, word)
    //Given a word, adds the word somewhere in the word search and returns the resulting word search
    //Throws CantAddWordException if the word can't be added
    public static char[][] addWord(char[][] search, int size, String word) throws CantAddWordException{
        int[] coord;
        try{
            coord = findCoordinates(search, size, word);
        }catch (CantAddWordException e){
            throw new CantAddWordException("Couldn't add word '"+word+"'");
        }
        return addToSearch(search, size, word, coord);
    }

    //int[] findCoordinates(search, size, word)
    //Given a word, returns a random valid coordinate to add the word to the word search
    //Throws a CantAddWordException if there are no possible tiles to place the word
    //Returns coordinate in form of {x, y, orientation}
    //Orientations:
    //0 means L to R
    //1 means U to D
    //2 means LU to RD
    //3 means LD to RU
    public static int[] findCoordinates(char[][] search, int size, String word) throws CantAddWordException{
        int[][] coordArray = new int[size*size*4][3];
        int coordArraySize = 0;
        
        //Iterates through every tile and orientation
        for(int x=0; x < size; x++){
            for(int y=0; y < size; y++){
                for(int z=0; z <=3; z++){
                    int[] coord = {x,y,z};
                    //If the coordinate is valid, adds it to a list of valid coordinates
                    if(isValid(search, size, word, coord)){
                        if(coordArraySize < coordArray.length) {
                            coordArray[coordArraySize] = coord;
                            coordArraySize++;
                        }else{
                            throw new CantAddWordException("Error: Internal issue with coordinates");
                        }
                    }
                }
            }
        }
        //If there are no possible tiles to add the word, throw error
        if(coordArraySize == 0){
            throw new CantAddWordException("Error: '"+word+"' cant be added.");
        }
        //Returns a random coordinate from the list of valid coorindates
        Random random = new Random();
        int randomNumber = random.nextInt(coordArraySize);
        return coordArray[randomNumber];
    }

    //boolean isValid(search, size, word, coord)
    //Determines if the given word can be added to the word search at the specified coordinate
    //A valid coordinate is one where the word does not overwrite any other existing letters in the word search, and does not exceed the dimensions of the word search
    //Returns true if the word and coordinate is valid, false otherwise
    //Throws a CantAddWordException if there is an invalid orientation specified in the coordinate
    public static boolean isValid(char[][] search, int size, String word, int[] coord) throws CantAddWordException{
        char[] wordArray = word.toCharArray();
        int length = wordArray.length;
        int x = coord[0];
        int y = coord[1];
        int i = 0;
        while(i < length){
            //If the word exceeds dimensions of the word search, the coordinate is invalid
            if(x >= size || y >= size || x < 0 || y < 0) {
                return false;
            }
            //If tile is non-empty and does not match the letter we are trying to place, the coordinate is invalid
            if(search[x][y] != '\0' && search[x][y] != wordArray[i]){
                return false;
            }
            switch(coord[2]){
                case 0:
                    //Left to right orientation
                    x++;
                    break;
                case 1:
                    //Up to down orientation
                    y++;
                    break;
                case 2:
                    //Left top to right bottom diagonal orientation
                    x++;
                    y++;
                    break;
                case 3:
                    //Left bottom to right top diagonal orientation
                    x++;
                    y--;
                    break;
                default:
                    throw new CantAddWordException("Something went wrong");
            }
            i++;
        }
        return true;
    }

    //char[][] addToSearch(search, size, word, coord)
    //Adds a given word to the word search at the specified coordinate
    //Returns the resulting word search
    //Throws a CantAddWordException if there is an invalid orientation specified in the coordinate
    public static char[][] addToSearch(char[][] search, int size, String word, int[] coord) throws CantAddWordException {
        char[] wordArray = word.toCharArray();
        int length = word.length();
        int x = coord[0];
        int y = coord[1];
        int i = 0;
        while(i < length) {
            //Adds coordinate to list of coordinates to be highlighted
            coordsToHighlight.add(new int[]{x, y});

            //Updates word search with letter
            search[x][y] = wordArray[i];
            switch (coord[2]) {
               case 0:
                    //Left to right orientation
                    x++;
                    break;
                case 1:
                    //Up to down orientation
                    y++;
                    break;
                case 2:
                    //Left top to right bottom diagonal orientation
                    x++;
                    y++;
                    break;
                case 3:
                    //Left bottom to right top diagonal orientation
                    x++;
                    y--;
                    break;
                default:
                    throw new CantAddWordException("Something went wrong");
            }
            i++;
        }
        return search;
    }

    //void generateSearch(words, size)
    //Given a list of words and dimensions, generates a word search containing all words and of the given size
    //Throws a CantAddWordException if there are words that don't fit in the word search or if a string is entered that contains non-letters
    public static void generateSearch(String[] words, int size) throws CantAddWordException {
        char[][] search = new char[size][size];
        
        //Sorts list of words by descending length so longer words are added first since they have fewer valid coordinates
        Arrays.sort(words, Comparator.comparing(String::length).reversed());
        
        Pattern pattern = Pattern.compile("^[a-zA-Z]+$");
        for(String word: words){
            
            //Check for non-letter characters
            if (!pattern.matcher(word).matches()) {
                throw new CantAddWordException("'" + word + "' contains non-letter characters.");
            }
            
            //Check for words longer than size of word search
            if(word.length() > size){
                throw new CantAddWordException("'"+word+"' cannot fit in "+size+"x"+size+" array. Must be at least "+word.length()+"x"+word.length());
            }
            word = word.toUpperCase();
            try{
                search = addWord(search, size, word);
            }catch (CantAddWordException e){
                throw new CantAddWordException("Error");
            }
        }
        search = fillRandom(search, size);
        printSearch(search, size, false);

        //Asks user if they want to reveal answers
        boolean reveal = scanForReveal();
        if(reveal){
            printSearch(search, size, true);
        }
    }

    //boolean scanForReveal()
    //Scans user input for either 'y' or 'n'
    //Returns true for 'y' and false for 'n'
    public static boolean scanForReveal(){
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println();
            System.out.println("Enter 'y' to reveal answers or 'n' to end program");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y")) {
                return true;
            } else if (input.equals("n")) {
                return false;
            } else {
                System.out.println("Invalid input. Please enter 'y' or 'n'.");
            }
        }
    }

    //void searchScanner()
    //Scans words and size from user, then inititates creating a new word search with given requirements
    //Throws a CantAddWordException if any word can't be added to the word search
    public static void searchScanner() throws CantAddWordException {
        Scanner scanner = new Scanner(System.in);
        String[] words = new String[100];
        Pattern pattern = Pattern.compile("^[a-zA-Z]+$");
        int wordCount = 0;
        int longestWord = 0;

        //Find list of words
        System.out.println("Enter the words you'd like in your word search, hit enter once to submit a word. Press enter twice once done");
        while (true) {
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                if (wordCount > 0) {
                    break;
                }
            } else if (!pattern.matcher(input).matches()) {
                System.out.println("Error: Non-letter characters found. Please enter letters only.");
            } else {
                words[wordCount] = input;
                wordCount++;
                if(input.length() > longestWord){
                    longestWord = input.length();
                }
            }
        }

        //Find size of word search
        int num = 0;
        while (num <= 0) {
            System.out.println("Enter the size of the word search");
            if(scanner.hasNextInt()) {
                num = scanner.nextInt();
                if(num <= 0) {
                    System.out.println("Error: Must be a positive integer.");
                    num = 0;
                }
                if(num < longestWord){
                    System.out.println("Error: Too small to fit the longest word, which is size "+longestWord);
                    num = 0;
                }
            } else {
                System.out.println("Error: Invalid input. Please enter an integer.");
                scanner.next(); // consume invalid input
                num = 0;
            }
        }
        //Trim word list to be exact size
        String[] trimmedWords = new String[wordCount];
        System.arraycopy(words, 0, trimmedWords, 0, wordCount);
        
        try{
            generateSearch(trimmedWords, num);
        }catch (CantAddWordException e){
            throw new CantAddWordException("Search failed");
        }
    }
    
    public static void main(String args[]){
        try {
            searchScanner();
        } catch (CantAddWordException e) {
            throw new RuntimeException(e);
        }
    }
}
