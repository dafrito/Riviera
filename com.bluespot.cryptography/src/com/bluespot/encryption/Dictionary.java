package com.bluespot.encryption;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class Dictionary implements Iterable<String> {

    private final Set<String> words;

    public Dictionary(final Set<String> words) {
        this.words = Collections.unmodifiableSet(new HashSet<String>(words));
    }

    public Set<String> getWords() {
        return this.words;
    }

    public Dictionary subDictionary(final Pattern pattern) {
        final Set<String> subWords = new HashSet<String>();
        for (final String word : this.words) {
            if (pattern.matcher(word).matches()) {
                subWords.add(word);
            }
        }
        return new Dictionary(subWords);
    }

    public int size() {
        return this.words.size();
    }

    public Dictionary wordsLongerThan(final int length) {
        final Set<String> subWords = new HashSet<String>();
        for (final String word : this.words) {
            if (word.length() > length) {
                subWords.add(word);
            }
        }
        return new Dictionary(subWords);
    }

    public Dictionary wordsShorterThan(final int length) {
        final Set<String> subWords = new HashSet<String>();
        for (final String word : this.words) {
            if (word.length() < length) {
                subWords.add(word);
            }
        }
        return new Dictionary(subWords);
    }

    public boolean contains(final String word) {
        return this.words.contains(word.toLowerCase());
    }

    public Dictionary wordsOfLength(final String string) {
        return this.wordsOfLength(string.length());
    }

    private final Map<Integer, Dictionary> cached = new HashMap<Integer, Dictionary>();

    public Dictionary wordsOfLength(final int length) {
        if (!this.cached.containsKey(length)) {
            final Set<String> subWords = new HashSet<String>();
            for (final String word : this.words) {
                if (word.length() == length) {
                    subWords.add(word);
                }
            }
            this.cached.put(length, new Dictionary(subWords));
        }
        return this.cached.get(length);
    }

    @Override
    public Iterator<String> iterator() {
        return this.getWords().iterator();
    }

    private static Set<String> getWords(final File file) throws IOException {
        final LineNumberReader reader = new LineNumberReader(new BufferedReader(new FileReader(file)));
        final Set<String> words = new HashSet<String>();
        while (true) {
            final String line = reader.readLine();
            if (line == null) {
                break;
            }
            words.add(line.toLowerCase());
        }
        return words;
    }

    private static Dictionary INSTANCE;

    public static Dictionary getDictionary() throws IOException {
        if (INSTANCE == null) {
            final Set<String> words = new HashSet<String>();
            words.addAll(Dictionary.getWords(new File("NAMES.DIC")));
            words.addAll(Dictionary.getWords(new File("2of12.txt")));
            words.addAll(Dictionary.getWords(new File("2of12inf.txt")));
            // words.addAll(Dictionary.getWords(new File("dic-0294.txt")));
            INSTANCE = new Dictionary(words);
        }
        return INSTANCE;
    }

    public static Dictionary fromFile(final String fileName) throws IOException {
        final File file = new File(fileName);
        return new Dictionary(Dictionary.getWords(file));
    }
}
