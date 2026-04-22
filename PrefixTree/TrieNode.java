public class TrieNode {
    TrieNode[] children;
    boolean isEndOfWord;
    String originalLine;

    public TrieNode() {
        // Array size 26 for the English alphabet
        children = new TrieNode[26];
        isEndOfWord = false;
        originalLine = null;
    }
}
