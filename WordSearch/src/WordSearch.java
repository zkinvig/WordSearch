import java.util.*;
import java.util.regex.Pattern;

class CantAddWordException extends Exception {
    public CantAddWordException(String message) {
        super(message);
    }
}
public class WordSearch {
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_RESET = "\u001B[0m";
    static ArrayList<int[]> coordsToHighlight = new ArrayList<>();
    public static boolean arrayListContainsIntArray(ArrayList<int[]> arrayList, int[] target) {
        for (int[] array : arrayList) {
            if (Arrays.equals(array, target)) {
                return true;
            }
        }
        return false;
    }
    public static void printSearch(char[][] search, int size, boolean reveal){
        for(int y=0; y < size; y++){
            for(int x=0; x < size; x++){
                if(arrayListContainsIntArray(coordsToHighlight,new int[]{x,y}) && reveal){
                    System.out.print(ANSI_PURPLE+ search[x][y]+"  ");
                }else {
                    System.out.print(ANSI_RESET+search[x][y] + "  ");
                }
            }
            System.out.println();
        }
    }
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
    public static char[][] addWord(char[][] search, int size, String word) throws CantAddWordException{
        int[] coord;
        try{
            coord = findCoordinates(search, size, word);
        }catch (CantAddWordException e){
            throw new CantAddWordException("Couldn't add word '"+word+"'");
        }
        return addToSearch(search, size, word, coord);
    }
    //Returns {x, y, orientation}
    public static int[] findCoordinates(char[][] search, int size, String word) throws CantAddWordException{
        int[][] coordArray = new int[size*size*4][3];
        int coordArraySize = 0;
        for(int x=0; x < size; x++){
            for(int y=0; y < size; y++){
                for(int z=0; z <=3; z++){
                    int[] coord = {x,y,z};
                    if(isValid(search, size, word, coord)){
                        if(coordArraySize < coordArray.length) {
                            coordArray[coordArraySize] = coord;
                            coordArraySize++;
                        }else{
                            System.out.println("Overflow");
                        }
                    }
                }
            }
        }
        if(coordArraySize == 0){
            throw new CantAddWordException("'"+word+"' cant be added.");
        }
        Random random = new Random();
        int randomNumber = random.nextInt(coordArraySize);
        return coordArray[randomNumber];
    }
    //0 means L to R
    //1 means U to D
    //2 means LU to RD
    //3 means LD to RU
    public static boolean isValid(char[][] search, int size, String word, int[] coord) throws CantAddWordException{
        char[] wordArray = word.toCharArray();
        int length = wordArray.length;
        int x = coord[0];
        int y = coord[1];
        int i = 0;
        while(i < length){
            if(x >= size || y >= size || x < 0 || y < 0) {
                return false;
            }
            if(search[x][y] != '\0' && search[x][y] != wordArray[i]){
                return false;
            }
            switch(coord[2]){
                case 0:
                    x++;
                    break;
                case 1:
                    y++;
                    break;
                case 2:
                    x++;
                    y++;
                    break;
                case 3:
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
    public static char[][] addToSearch(char[][] search, int size, String word, int[] coord) throws CantAddWordException {
        char[] wordArray = word.toCharArray();
        int length = word.length();
        int x = coord[0];
        int y = coord[1];
        int i = 0;
        while(i < length) {
            coordsToHighlight.add(new int[]{x, y});
            search[x][y] = wordArray[i];
            switch (coord[2]) {
                case 0:
                    x++;
                    break;
                case 1:
                    y++;
                    break;
                case 2:
                    x++;
                    y++;
                    break;
                case 3:
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
    public static char generateRandomCapitalLetter() {
        Random random = new Random();
        int randomNumber = random.nextInt(26);
        return (char) ('A' + randomNumber);
    }
    public static int getNumberOfLetters(String[] words){
        int sum= 0;
        for(String word: words){
            sum += word.length();
        }
        return sum;
    }
    public static void generateSearch(String[] words, int size) throws CantAddWordException {
        char[][] search = new char[size][size];
        Arrays.sort(words, Comparator.comparing(String::length).reversed());
        Pattern pattern = Pattern.compile("^[a-zA-Z]+$");
        for(String word: words){
            if (!pattern.matcher(word).matches()) {
                throw new CantAddWordException("'" + word + "' contains non-letter characters.");
            }
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
        boolean reveal = scanForReveal();
        if(reveal){
            printSearch(search, size, true);
        }
    }
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
    public static void searchScanner() throws CantAddWordException {
        Scanner scanner = new Scanner(System.in);
        String[] words = new String[100];
        Pattern pattern = Pattern.compile("^[a-zA-Z]+$");
        int wordCount = 0;
        int longestWord = 0;

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
