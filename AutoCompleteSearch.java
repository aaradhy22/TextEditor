package autocompleteSearch;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

class TrieNode {
    TrieNode[] children = new TrieNode[26];
    boolean isEndOfWord;
}

class Trie {
    private final TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            if (node.children[c - 'a'] == null) {
                node.children[c - 'a'] = new TrieNode();
            }
            node = node.children[c - 'a'];
        }
        node.isEndOfWord = true;
    }

    public List<String> search(String prefix) {
        List<String> results = new ArrayList<>();
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            if (node.children[c - 'a'] == null) {
                return results;
            }
            node = node.children[c - 'a'];
        }
        findWords(node, prefix, results);
        return results;
    }

    private void findWords(TrieNode node, String prefix, List<String> results) {
        if (node.isEndOfWord) {
            results.add(prefix);
        }
        for (char c = 'a'; c <= 'z'; c++) {
            if (node.children[c - 'a'] != null) {
                findWords(node.children[c - 'a'], prefix + c, results);
            }
        }
    }
}

public class AutoCompleteSearch {
    private JFrame frame;
    private JTextField searchField;
    private DefaultListModel<String> listModel;
    private JList<String> suggestionList;
    private Trie trie;

    public AutoCompleteSearch() {
        trie = new Trie();
        insertSampleWords();

        frame = new JFrame("AutoComplete Search");
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        searchField = new JTextField();
        listModel = new DefaultListModel<>();
        suggestionList = new JList<>(listModel);

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateSuggestions(searchField.getText().toLowerCase());
            }
        });

        frame.setLayout(new BorderLayout());
        frame.add(searchField, BorderLayout.NORTH);
        frame.add(new JScrollPane(suggestionList), BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private void updateSuggestions(String text) {
        listModel.clear();
        if (!text.isEmpty()) {
            List<String> suggestions = trie.search(text);
            for (String suggestion : suggestions) {
                listModel.addElement(suggestion);
            }
        }
    }

    private void insertSampleWords() {
        String[] words = {"apple", "application", "apply", "banana", "band", "bat", "cat", "cater", "dog", "dove"};
        for (String word : words) {
            trie.insert(word);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AutoCompleteSearch::new);
    }
}
