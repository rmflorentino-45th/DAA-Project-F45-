import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class AutocompleteGUI {
    JFrame frame;
    JTextArea textArea;
    JPanel suggestionStrip;
    JButton btnSugg1, btnSugg2, btnSugg3;
    Trie myTrie;

    public AutocompleteGUI() {
        myTrie = new Trie();
        loadDictionary("../data/words_10000.txt");
    }

    private void loadDictionary(String filePath) {
        java.io.File f = new java.io.File(filePath);
        if (!f.exists()) filePath = "data/words_10000.txt"; 
        try (java.util.Scanner scanner = new java.util.Scanner(new java.io.File(filePath))) {
            while (scanner.hasNextLine()) {
                myTrie.insert(scanner.nextLine());
            }
            System.out.println("Dictionary loaded into GUI successfully.");
        } catch (Exception e) {
            System.err.println("Error loading dictionary: " + e.getMessage());
        }
    }

    public void setupGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}

        frame = new JFrame("The Search Optimizer");
        frame.setLayout(new BorderLayout());

        suggestionStrip = new JPanel(new GridLayout(1, 3));
        btnSugg1 = new JButton("");
        btnSugg2 = new JButton("");
        btnSugg3 = new JButton("");
        suggestionStrip.add(btnSugg1);
        suggestionStrip.add(btnSugg2);
        suggestionStrip.add(btnSugg3);

        btnSugg1.addActionListener(e -> applySuggestion(btnSugg1.getText()));
        btnSugg2.addActionListener(e -> applySuggestion(btnSugg2.getText()));
        btnSugg3.addActionListener(e -> applySuggestion(btnSugg3.getText()));
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setFont(new Font("Arial", Font.PLAIN, 16));
        textArea.setMargin(new Insets(10, 10, 10, 10));

        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String fullText = textArea.getText();

                if (fullText.endsWith(" ") || fullText.endsWith("\n") || fullText.endsWith("\t")) {
                    clearSuggestions();
                    return;
                }

                String[] words = fullText.split("\\s+");
                if (words.length == 0) return;
                
                String currentPrefix = words[words.length - 1];

                if (!currentPrefix.isEmpty()) {
                    updateSuggestions(currentPrefix);
                } else {
                    clearSuggestions();
                }
            }
        });

        frame.add(suggestionStrip, BorderLayout.NORTH);
        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);

        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); 
        frame.setVisible(true);
    }

    public void updateSuggestions(String prefix) {
        List<String> results = myTrie.autocomplete(prefix); 

        if (results.size() > 0) btnSugg1.setText(results.get(0).split("\\s+")[0]); 
        else btnSugg1.setText("");

        if (results.size() > 1) btnSugg2.setText(results.get(1).split("\\s+")[0]); 
        else btnSugg2.setText("");

        if (results.size() > 2) btnSugg3.setText(results.get(2).split("\\s+")[0]); 
        else btnSugg3.setText("");
    }

    public void clearSuggestions() {
        btnSugg1.setText("");
        btnSugg2.setText("");
        btnSugg3.setText("");
    }

    public void applySuggestion(String chosenWord) {
        if (chosenWord.isEmpty()) return;

        String fullText = textArea.getText();
        
        int lastSpaceIndex = -1;
        for (int i = fullText.length() - 1; i >= 0; i--) {
            if (Character.isWhitespace(fullText.charAt(i))) {
                lastSpaceIndex = i;
                break;
            }
        }

        if (lastSpaceIndex == -1) {
            textArea.setText(chosenWord + " ");
        } else {
            String textBeforeCurrentWord = fullText.substring(0, lastSpaceIndex + 1);
            textArea.setText(textBeforeCurrentWord + chosenWord + " ");
        }
        
        clearSuggestions();
        textArea.requestFocus(); 
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AutocompleteGUI().setupGUI();
        });
    }
}
