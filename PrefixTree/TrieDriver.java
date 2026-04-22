package prefixTree;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TrieDriver {
    public static void main(String[] args) {
        String filePath = "../data/words_10000.txt";
        File f = new File(filePath);
        if (!f.exists()) {
            filePath = "data/words_10000.txt";
        }

        String[] dictionary = new String[10000];
        int wordCount = loadDictionary(filePath, dictionary);

        if (wordCount == 0) {
            System.out.println("Could not load dictionary. Exiting.");
            return;
        }

        System.out.println("\n--- Interactive Search ---");
        Trie trie = new Trie();
        for (int i = 0; i < wordCount; i++) {
            trie.insert(dictionary[i]);
        }

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter prefix to search: ");
            String prefix = scanner.next();

            long startTime = System.nanoTime();
            List<String> results = trie.autocomplete(prefix);
            long duration = System.nanoTime() - startTime;

            for (String match : results) {
                System.out.println("Match found: " + match.split("\\s+")[0]);
            }

            if (results.isEmpty()) {
                System.out.println("No matches found for prefix: " + prefix);
            } else {
                System.out.println("\nTotal matches: " + results.size());
                System.out.println("Search took: " + (duration / 1000000.0) + " ms");
            }
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

