import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;

class WordSearchTest {
    String[] words = {"home", "friend", "lover", "happy"};
    String[] words2 = {"Elephant", "Sunshine", "Bicycle", "Symphony", "Ocean", "Mountain", "Rainbow", "Whisper", "Chocolate", "Universe", "Serendipity", "Moonlight", "Adventure", "Butterfly", "Fireworks", "Serenity", "Harmony", "Waterfall", "Blossom", "Wanderlust", "Enchantment", "Twilight", "Reflection", "Galaxy", "Sunshine", "Laughter", "Tranquility", "Majestic", "Starlight", "Jubilant"};
    // 00 10 20 30
    // 01 11 21 31
    // 02 12 22 32
    // 03 13 23 33
    @Test
    void printTest() {
        try {
            WordSearch.generateSearch(words, 7);
        } catch (CantAddWordException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void printSizeStressTest() {
        try {
            WordSearch.generateSearch(words, 100);
        } catch (CantAddWordException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void printWordsSizeStressTest() {
        try {
            WordSearch.generateSearch(words2, 100);
        } catch (CantAddWordException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void wordTooLongTest() {
        assertThrows(CantAddWordException.class, () -> {
            WordSearch.generateSearch(new String[]{"hello"}, 3);
        });
    }
    @Test
    void nonLetterWordTest() {
        assertThrows(CantAddWordException.class, () -> {
            WordSearch.generateSearch(new String[]{"h2l!o"}, 6);
        });
    }
    @Test
    public void findCoordinatesThrowsExceptionTest() {
        char[][] search = {{'A', 'A'}, {'A', 'A'}};
        int size = 2;
        String word = "hi";
        assertThrows(CantAddWordException.class, () -> {
            WordSearch.findCoordinates(search, size, word);
        });
    }
    @Test
    public void addWordThrowsExceptionTest() {
        char[][] search = {{'A','A'},{'A','A'}};
        int size = 2;
        String word = "hi";
        assertThrows(CantAddWordException.class, () -> {
            WordSearch.addWord(search, size, word);
        });
    }
    @Test
    void validtestLtoR() {
        char[][] search = new char[4][4];
        search[1][1] = 'A';
        try {
            assertTrue(WordSearch.isValid(search, 4, "hey", new int[]{1, 0, 0}));
            assertFalse(WordSearch.isValid(search, 4, "hey", new int[]{0, 1, 0}));
            assertFalse(WordSearch.isValid(search, 4, "hey", new int[]{2, 2, 0}));
        } catch (CantAddWordException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void validtestUtoD() {
        char[][] search = new char[4][4];
        search[1][1] = 'A';
        try {
            assertTrue(WordSearch.isValid(search, 4, "hey", new int[]{2, 1, 1}));
            assertFalse(WordSearch.isValid(search, 4, "hey", new int[]{1, 0, 1}));
            assertFalse(WordSearch.isValid(search, 4, "hey", new int[]{1, 2, 1}));
        } catch (CantAddWordException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void validtestLUtoRD() {
        char[][] search = new char[4][4];
        search[1][1] = 'A';
        try {
            assertTrue(WordSearch.isValid(search, 4, "hey", new int[]{0, 1, 2}));
            assertFalse(WordSearch.isValid(search, 4, "hey", new int[]{0, 0, 2}));
            assertFalse(WordSearch.isValid(search, 4, "hey", new int[]{0, 2, 2}));
        } catch (CantAddWordException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void validtestLDtoRU() {
        char[][] search = new char[4][4];
        search[1][1] = 'A';
        try {
            assertTrue(WordSearch.isValid(search, 4, "hey", new int[]{1, 2, 3}));
            assertFalse(WordSearch.isValid(search, 4, "hey", new int[]{0, 2, 3}));
            assertFalse(WordSearch.isValid(search, 4, "hey", new int[]{1, 0, 3}));
        } catch (CantAddWordException e) {
            throw new RuntimeException(e);
        }
    }
}