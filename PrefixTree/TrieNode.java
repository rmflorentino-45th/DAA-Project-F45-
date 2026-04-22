

public class TrieNode {
    TrieNode[] children;
    boolean isEndOfWord;
    String originalLine;

    public TrieNode() {
        children = new TrieNode[26];
        isEndOfWord = false;
        originalLine = null;
    }
}
