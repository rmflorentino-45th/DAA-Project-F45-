package test;
import prefixTree.Trie;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class TestAlgorithms {
    public static void main(String[] args) {
        String filePath = "data/words_10000.txt";
        File f = new File(filePath);
        if (!f.exists()) {
            filePath = "../data/words_10000.txt";
        }

        String[] dictionary = new String[10000];
        int wordCount = loadDictionary(filePath, dictionary);

        if (wordCount == 0) {
            System.out.println("Could not load dictionary. Exiting.");
            return;
        }

        System.out.println("=== Scalability Testing (Prefix Tree vs Brute Force) ===");
        int[] inputSizes = { 10, 100, 1000, 10000 };

        for (int n : inputSizes) {
            if (n > wordCount) continue;
            String[] testData = Arrays.copyOfRange(dictionary, 0, n);
            
            System.out.println("\nTesting for n=" + testData.length);
            
            // 1. Prefix Tree (Trie) Testing
            long trieStartTime = System.nanoTime();
            testTrieAlgorithm(testData);
            long trieDuration = System.nanoTime() - trieStartTime;
            System.out.println("  [Trie] Execution time : " + trieDuration + "ns");

            // 2. Brute Force Testing
            long bruteStartTime = System.nanoTime();
            testBruteForceAlgorithm(testData);
            long bruteDuration = System.nanoTime() - bruteStartTime;
            System.out.println("  [Brute Force] Execution time : " + bruteDuration + "ns");
        }
    }

    private static void testTrieAlgorithm(String[] testData) {
        Trie trie = new Trie();
        for (String data : testData) {
            trie.insert(data);
        }
        trie.autocomplete("th");
    }

    private static void testBruteForceAlgorithm(String[] testData) {
        int matches = bruteForce.BruteForceTry.countMatches(testData, testData.length, "th");
        if (matches < 0) {
            System.out.println("Never happens");
        }
    }

    private static int loadDictionary(String filePath, String[] dictionary) {
        int count = 0;
        try (Scanner fileScanner = new Scanner(new File(filePath))) {
            while (fileScanner.hasNextLine() && count < dictionary.length) {
                dictionary[count++] = fileScanner.nextLine();
            }
            System.out.println("Loaded " + count + " words from " + filePath);
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found at " + filePath);
        }
        return count;
    }
}

