package test;

import prefixTree.Trie;
import bruteForce.BruteForceTry;
import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class ScalabilityDriver {
    public static void main(String[] args) {
        String filePath = "data/words_10000.txt";
        if (!new File(filePath).exists()) {
            filePath = "../data/words_10000.txt";
        }

        String[] dictionary = new String[10000];
        int totalWords = loadDictionary(filePath, dictionary);

        int[] inputSizes = {10, 100, 1000, 10000};

        System.out.println("=== Scalability Testing: Prefix Tree (Trie) ===");
        for (int n : inputSizes) {
            if (n > totalWords) continue;
            String[] testData = Arrays.copyOfRange(dictionary, 0, n);
            
            // Setup: Build the Trie outside the timing logic (Test the search, not the build)
            Trie trie = new Trie();
            for (String s : testData) {
                trie.insert(s);
            }

            // 1. Record the start time in nanoseconds
            long startTime = System.nanoTime();

            // 2. Execute your specific string algorithm
            myTrieAlgorithm(trie);

            // 3. Record the end time
            long endTime = System.nanoTime();

            // 4. Calculate the total duration (delta)
            long duration = (endTime - startTime);

            // 5. Output results for your "Chart of Truth"
            System.out.println("Execution time for n=" + testData.length + ": " + duration + " ns");
        }

        System.out.println("\n=== Scalability Testing: Brute Force ===");
        for (int n : inputSizes) {
            if (n > totalWords) continue;
            String[] testData = Arrays.copyOfRange(dictionary, 0, n);

            // 1. Record the start time in nanoseconds
            long startTime = System.nanoTime();

            // 2. Execute your specific string algorithm
            myBruteForceAlgorithm(testData);

            // 3. Record the end time
            long endTime = System.nanoTime();

            // 4. Calculate the total duration (delta)
            long duration = (endTime - startTime);

            // 5. Output results for your "Chart of Truth"
            System.out.println("Execution time for n=" + testData.length + ": " + duration + " ns");
        }
    }

    /**
     * Specific string algorithm for Prefix Tree search.
     */
    public static void myTrieAlgorithm(Trie trie) {
        trie.autocomplete("th");
    }

    /**
     * Specific string algorithm for Brute Force search.
     */
    public static void myBruteForceAlgorithm(String[] testData) {
        BruteForceTry.countMatches(testData, testData.length, "th");
    }

    private static int loadDictionary(String filePath, String[] dictionary) {
        int count = 0;
        try (Scanner fileScanner = new Scanner(new File(filePath))) {
            while (fileScanner.hasNextLine() && count < dictionary.length) {
                dictionary[count++] = fileScanner.nextLine();
            }
        } catch (Exception e) {
            System.err.println("Error loading dictionary: " + e.getMessage());
        }
        return count;
    }
}
