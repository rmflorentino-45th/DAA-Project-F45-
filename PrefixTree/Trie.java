package prefixTree;

import java.util.ArrayList;
import java.util.List;

public class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public void insert(String line) {
        if (line == null || line.trim().isEmpty()) return;
        
        String word = line.split("\\s+")[0].toLowerCase();
        
        TrieNode current = root;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);

            if (ch < 'a' || ch > 'z') {
                continue;
            }
            int index = ch - 'a';
            if (current.children[index] == null) {
                current.children[index] = new TrieNode();
            }
            current = current.children[index];
        }
        current.isEndOfWord = true;
        current.originalLine = line;
    }

    public List<String> autocomplete(String prefix) {
        List<String> results = new ArrayList<>();
        if (prefix == null || prefix.trim().isEmpty()) {
            return results;
        }

        prefix = prefix.toLowerCase();
        TrieNode current = root;
        
        for (int i = 0; i < prefix.length(); i++) {
            char ch = prefix.charAt(i);
            if (ch < 'a' || ch > 'z') {
                continue;
            }
            int index = ch - 'a';
            if (current.children[index] == null) {
                return results;
            }
            current = current.children[index];
        }
        gatherWords(current, results);
        return results;
    }

    private void gatherWords(TrieNode node, List<String> results) {
        if (node == null) return;
        
        if (node.isEndOfWord) {
            results.add(node.originalLine);
        }
        
        for (int i = 0; i < 26; i++) {
            if (node.children[i] != null) {
                gatherWords(node.children[i], results);
            }
        }
    }
}

