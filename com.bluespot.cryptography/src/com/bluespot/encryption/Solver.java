package com.bluespot.encryption;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class Solver {

    private final Dictionary dictionary;

    public Solver(final Dictionary dictionary) {
        this.dictionary = dictionary;
    }

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

    public Set<String> getMiddleLetters() {
        final Set<String> middleLetters = new HashSet<String>();
        for (final String word : this.dictionary.subDictionary(Pattern.compile("^([a-z])([a-z])\\2([a-z])$"))) {
            middleLetters.add(word.substring(1, 2));
        }
        return middleLetters;
    }

    public static int[] getConversionTable(final String word) {
        final LinkedHashSet<Character> chars = new LinkedHashSet<Character>();
        for (final char letter : word.toCharArray()) {
            chars.add(letter);
        }
        final Map<Character, Integer> conversions = new HashMap<Character, Integer>();
        for (final char letter : chars) {
            conversions.put(letter, conversions.size());
        }
        final int[] conversion = new int[word.length()];
        for (int i = 0; i < conversion.length; i++) {
            conversion[i] = conversions.get(word.charAt(i));
        }
        return conversion;
    }

    public static boolean potentialMatch(final String encryptedWord, final String candidate,
            final Set<Character> solvedLetters) {
        for (int i = 0; i < encryptedWord.length(); i++) {
            final char letter = encryptedWord.charAt(i);
            if (Character.isLowerCase(letter)) {
                // It's a "solved" letter
                if (candidate.charAt(i) != letter) {
                    return false;
                }
            } else {
                // It's not solved, so it can't be a solvedLetter
                if (solvedLetters.contains(candidate.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Set<String> getCombinations(final Dictionary dictionary, final String encryptedWord,
            final Set<Character> solvedLetters) {
        final Dictionary sameLengthWords = dictionary.wordsOfLength(encryptedWord);
        final int[] conversionTable = Solver.getConversionTable(encryptedWord);

        final Set<String> matches = new HashSet<String>();
        for (final String candidate : sameLengthWords) {
            if (!Solver.potentialMatch(encryptedWord, candidate, solvedLetters)) {
                continue;
            }
            final int[] candidateConversionTable = Solver.getConversionTable(candidate);
            if (Arrays.equals(conversionTable, candidateConversionTable)) {
                matches.add(candidate);
            }
        }
        return matches;
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

    private static class WordCombo {

        private final String word;
        private final Set<String> candidates;

        public WordCombo(final String word, final Set<String> candidates) {
            if (candidates == null) {
                throw new NullPointerException("candidates is null");
            }
            if (word == null) {
                throw new NullPointerException("word is null");
            }
            this.word = word;
            this.candidates = candidates;
        }

        public boolean isEmpty() {
            return this.getCandidates().isEmpty();
        }

        public String getWord() {
            return this.word;
        }

        public Set<String> getCandidates() {
            return this.candidates;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof WordCombo)) {
                return false;
            }
            final WordCombo other = (WordCombo) obj;
            if (!this.getWord().equals(other.getWord())) {
                return false;
            }
            if (!this.getCandidates().equals(other.getCandidates())) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int result = 17;
            result = 31 * result + this.getWord().hashCode();
            result = 31 * result + this.getCandidates().hashCode();
            return result;
        }

        @Override
        public String toString() {
            return String.format("Combo[word: %s, candidates: %d]", this.getWord(), this.getCandidates().size());
        }

    }

    public static boolean testWord(final String encryptedWord, final Map<Character, Character> conversions,
            final String candidate) {
        final String workingWord = Solvers.convert(encryptedWord, conversions);
        for (int i = 0; i < workingWord.length(); i++) {
            final char workingLetter = workingWord.charAt(i);
            if (Character.isLowerCase(workingLetter) && candidate.charAt(i) != workingLetter) {
                return false;
            }
        }
        return true;
    }

    private final Set<String> matches = new HashSet<String>();

    public static void dump(final Map<Character, Character> conversions) {
        for (final Entry<Character, Character> entry : conversions.entrySet()) {
            if (Character.isLowerCase(entry.getKey())) {
                continue;
            }
            System.out.printf("(%s)%d=(%s)%d%n", entry.getKey(), (entry.getKey().charValue()) - 'A', entry.getValue(),
                    (entry.getValue().charValue()) - 'a');
        }
    }

    public String churn(final String originalEncrypted, final WordCombo combo,
            final Map<Character, Character> conversions) {
        if (combo == null || combo.getCandidates().isEmpty()) {
            return null;
        }
        // System.out.println(originalEncrypted);
        // System.out.println(combo);
        for (final String candidate : combo.getCandidates()) {
            if (!Solver.testWord(combo.getWord(), conversions, candidate)) {
                continue;
            }
            final Map<Character, Character> workingConversions = Solvers.asMap(Solvers.asList(combo.getWord()),
                    Solvers.asList(candidate));
            workingConversions.putAll(conversions);
            final String converted = Solvers.convert(originalEncrypted, workingConversions);
            if (converted.toLowerCase().equals(converted)) {
                if (this.matches.add(converted)) {
                    System.out.println(converted);
                }
                continue;
            }
            final WordCombo nextCombo = Solver.getCheapestWordCombo(this.dictionary, converted);
            if (nextCombo.isEmpty()) {
                continue;
            }
            final String match = this.churn(converted, nextCombo, workingConversions);
            if (match != null) {
                if (this.matches.add(match)) {
                    System.out.println(match);
                }
            }
        }
        // Couldn't solve :(
        return null;
    }

    public static Set<WordCombo> getAllWordCombos(final Dictionary dictionary, final String encrypted) {
        final Set<WordCombo> combos = new HashSet<WordCombo>();
        final Set<Character> omitted = new HashSet<Character>();
        for (final char letter : encrypted.toCharArray()) {
            if (Character.isLowerCase(letter)) {
                omitted.add(letter);
            }
        }
        for (final String word : encrypted.split("[ .,]+")) {
            if (word.toLowerCase().equals(word)) {
                continue;
            }
            combos.add(new WordCombo(word, Solver.getCombinations(dictionary, word, omitted)));

        }
        return combos;
    }

    public static WordCombo getCheapestWordCombo(final Dictionary dictionary, final String encrypted) {
        WordCombo candidate = null;
        final Set<Character> omitted = new HashSet<Character>();
        for (final char letter : encrypted.toCharArray()) {
            if (Character.isLowerCase(letter)) {
                omitted.add(letter);
            }
        }
        for (final String word : encrypted.split("[ .,]+")) {
            if (word.toLowerCase().equals(word)) {
                continue;
            }
            final WordCombo combo = new WordCombo(word, Solver.getCombinations(dictionary, word, omitted));
            if (candidate == null || combo.getCandidates().size() < candidate.getCandidates().size()) {
                candidate = combo;
            }
        }
        return candidate;
    }

    public void crack(final String encrypted) {
        System.out.println(encrypted);
        this.churn(encrypted, Solver.getCheapestWordCombo(this.dictionary, encrypted),
                new HashMap<Character, Character>());
    }

    public static final String LOLZ = "BRETT IS A FLAMING HOMOSEXUAL AND HIS FACE IS ALSO GAY BUT HE IS MOSTLY JUST GAY";

    public static final String UNDERLING = "BICYCLES EAT LARGE COWS EVERY FUCKING DAY";

    public static final String BOSS = "ISS NVVK DIPXYWA PIT AVSUY QIAOP PWZEHVNWIEDZ. CDYT ZVM LOTK HDY AVSMHOVT HV HDOA HYFH, ZVM COSS QY IQSY HV NYH HDY ITACYW, CDOPD OA IKMGQWIHY.";

    public static String encrypt(final String unencrypted) {
        final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final List<Character> letters = Solvers.asList(ALPHABET);
        Collections.shuffle(letters);
        return Solvers.convert(unencrypted.toUpperCase(), Solvers.asMap(ALPHABET, letters));
    }

    public static void main(final String[] args) throws IOException {
        final Solver solver = new Solver(Dictionary.getDictionary());
        solver.crack(UNDERLING);
    }
}
