```markdown
# GUI Implementation Guide: The "Gboard" Auto-Complete UI

To replicate the feel of a Google Keyboard (Gboard) suggestion strip, you need a user interface that dynamically updates as the user types. Assuming you are building this in Java (to match the required driver class logic), **Java Swing** is the standard toolkit for this.

Here is the step-by-step guide to building a text area with a live prediction strip above it.

---

## Step 1: Design the UI Layout

You need three main components stacked vertically:
1.  **Main Container (`JFrame`):** The window holding everything.
2.  **The Text Input (`JTextArea`):** Where the user types their sentences.
3.  **The Suggestion Strip (`JPanel`):** A horizontal bar placed immediately above or below the text area holding 3 clickable buttons (`JButton`).

### Basic Swing Setup Outline:
```java
import javax.swing.*;
import java.awt.*;

public class AutocompleteGUI {
    JFrame frame;
    JTextArea textArea;
    JPanel suggestionStrip;
    JButton btnSugg1, btnSugg2, btnSugg3;

    public void setupGUI() {
        frame = new JFrame("The Search Optimizer");
        frame.setLayout(new BorderLayout());

        // 1. Setup the Suggestion Strip (The "Gboard" bar)
        suggestionStrip = new JPanel(new GridLayout(1, 3)); // 3 buttons side-by-side
        btnSugg1 = new JButton("");
        btnSugg2 = new JButton("");
        btnSugg3 = new JButton("");
        suggestionStrip.add(btnSugg1);
        suggestionStrip.add(btnSugg2);
        suggestionStrip.add(btnSugg3);

        // 2. Setup the Text Area
        textArea = new JTextArea();
        textArea.setLineWrap(true);

        // 3. Add to Frame
        frame.add(suggestionStrip, BorderLayout.NORTH);
        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);

        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
```

---

## Step 2: Implement the Keystroke Listener (The "Live" Element)

To make it act like Gboard, the GUI must listen to every single keystroke. When a key is pressed, it needs to grab the *current word being typed* (the prefix) and send it to your Trie.

In Swing, you attach a `DocumentListener` or `KeyListener` to the `JTextArea`. 

```java
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

// Inside your setupGUI method:
textArea.addKeyListener(new KeyAdapter() {
    @Override
    public void keyReleased(KeyEvent e) {
        // 1. Get all the text currently in the box
        String fullText = textArea.getText();
        
        // 2. Extract just the last word the user is currently typing
        String[] words = fullText.split("\\s+"); // Split by spaces
        if (words.length == 0) return;
        
        String currentPrefix = words[words.length - 1];

        // 3. Send to your Trie ONLY if it's not empty
        if (!currentPrefix.isEmpty()) {
            updateSuggestions(currentPrefix);
        } else {
            clearSuggestions();
        }
    }
});
```

---

## Step 3: Fetch Data from the Trie & Update Buttons

Create a method that passes the `currentPrefix` to your Trie algorithm, retrieves the top 3 matching words, and updates the text on your buttons.

```java
public void updateSuggestions(String prefix) {
    // This calls the Trie method you built in Step 1 of the project instructions
    // Assume myTrie.search(prefix) returns a List<String> or String[]
    String[] results = myTrie.search(prefix); 

    // Update Button 1
    if (results.length > 0) btnSugg1.setText(results[0]); 
    else btnSugg1.setText("");

    // Update Button 2
    if (results.length > 1) btnSugg2.setText(results[1]); 
    else btnSugg2.setText("");

    // Update Button 3
    if (results.length > 2) btnSugg3.setText(results[2]); 
    else btnSugg3.setText("");
}

public void clearSuggestions() {
    btnSugg1.setText("");
    btnSugg2.setText("");
    btnSugg3.setText("");
}
```

---

## Step 4: Handle the "Auto-Complete" Click

When the user clicks one of the suggestion buttons, the GUI must replace the incomplete word they were typing with the full word they clicked.

Add an `ActionListener` to your buttons:

```java
btnSugg1.addActionListener(e -> applySuggestion(btnSugg1.getText()));
btnSugg2.addActionListener(e -> applySuggestion(btnSugg2.getText()));
btnSugg3.addActionListener(e -> applySuggestion(btnSugg3.getText()));

public void applySuggestion(String chosenWord) {
    if (chosenWord.isEmpty()) return;

    String fullText = textArea.getText();
    int lastSpaceIndex = fullText.lastIndexOf(" ");

    // If there are no spaces, they are typing the first word
    if (lastSpaceIndex == -1) {
        textArea.setText(chosenWord + " ");
    } else {
        // Keep everything up to the last space, and append the chosen word
        String textBeforeCurrentWord = fullText.substring(0, lastSpaceIndex + 1);
        textArea.setText(textBeforeCurrentWord + chosenWord + " ");
    }
    
    // Clear the suggestion strip since the word is now complete
    clearSuggestions();
    
    // Return focus to the text area so they can keep typing
    textArea.requestFocus(); 
}
```

### Pro-Tips for the Final Polish:
* **UI Styling:** Java Swing looks a bit dated by default. Use `UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());` at the very start of your `main` method to make it look like a native Windows/Mac application.
* **Trie Performance:** Ensure your Trie search limits its output to exactly 3 words to prevent the GUI from hanging if the prefix matches thousands of dictionary entries.
```