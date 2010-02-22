package com.bluespot.solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.bluespot.dictionary.Dictionary;

public class Frequencies {

    public Map<Character, Integer> getFrequency(final String string) {
        final Map<Character, Integer> letterCount = new HashMap<Character, Integer>();
        for (int i = 0; i < string.length(); i++) {
            final char letter = string.charAt(i);
            if (!Character.isLetter(letter)) {
                continue;
            }
            if (!letterCount.containsKey(letter)) {
                letterCount.put(letter, 0);
            }
            letterCount.put(letter, letterCount.get(letter) + 1);
        }
        return letterCount;
    }

    public Set<String> rotate(final String string) {
        final Set<String> strings = new HashSet<String>();
        for (int rotation = 0; rotation < 26; rotation++) {
            final StringBuilder builder = new StringBuilder();
            for (int i = 0; i < string.length(); i++) {
                final char letter = string.charAt(i);
                if (Character.isLetter(letter)) {
                    final int offset = 'A' + ((letter - 'A' + rotation) % 26);
                    builder.append((char) offset);
                } else {
                    builder.append(letter);
                }
            }
            strings.add(builder.toString());
        }
        return strings;
    }

    public Set<String> getMiddleLetters(Dictionary dictionary) {
        final Set<String> middleLetters = new HashSet<String>();
        for (final String word : dictionary.subDictionary(Pattern.compile("^([a-z])([a-z])\\2([a-z])$"))) {
            middleLetters.add(word.substring(1, 2));
        }
        return middleLetters;
    }

    public List<Character> getEnglishFrequencies() {
        final List<Character> frequencies = new ArrayList<Character>();
        for (final char letter : "ETAOINSHRDLCUMWFGYPBVKJXQZ".toCharArray()) {
            frequencies.add(letter);
        }
        return frequencies;
    }

    public List<Character> getLetterFrequencies(final String string) {
        final Map<Character, Integer> encryptedFrequencies = this.getFrequency(string);
        final List<Character> orderedByFrequency = new ArrayList<Character>(encryptedFrequencies.keySet());
        Collections.sort(orderedByFrequency, new Comparator<Character>() {
            public int compare(final Character o1, final Character o2) {
                return encryptedFrequencies.get(o2) - encryptedFrequencies.get(o1);
            }
        });
        return orderedByFrequency;
    }

}
