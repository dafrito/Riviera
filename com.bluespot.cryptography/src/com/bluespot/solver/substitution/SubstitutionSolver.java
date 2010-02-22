package com.bluespot.solver.substitution;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.bluespot.dictionary.Dictionary;
import com.bluespot.solver.Solver;
import com.bluespot.solver.Solvers;
import com.bluespot.solver.WordCombo;

public class SubstitutionSolver implements Solver<Set<String>, String> {

    private final Dictionary dictionary;

    public SubstitutionSolver() {
        this(Dictionary.getDictionary());
    }

    public SubstitutionSolver(final Dictionary dictionary) {
        this.dictionary = dictionary;
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
        final int[] conversionTable = SubstitutionSolver.getConversionTable(encryptedWord);

        final Set<String> matches = new HashSet<String>();
        for (final String candidate : sameLengthWords) {
            if (!SubstitutionSolver.potentialMatch(encryptedWord, candidate, solvedLetters)) {
                continue;
            }
            final int[] candidateConversionTable = SubstitutionSolver.getConversionTable(candidate);
            if (Arrays.equals(conversionTable, candidateConversionTable)) {
                matches.add(candidate);
            }
        }
        return matches;
    }

    public static boolean testWord(final String encryptedWord, final Map<Character, Character> conversions,
            final String candidate) {
        final String workingWord = Solvers.convert(encryptedWord, conversions);
        for (int i = 0; i < workingWord.length(); i++) {
            final char workingLetter = workingWord.charAt(i);
            if (Character.isLowerCase(workingLetter) && (candidate.charAt(i) != workingLetter)) {
                return false;
            }
        }
        return true;
    }

    public Set<String> churn(final String originalEncrypted, final WordCombo combo,
            final Map<Character, Character> conversions) {
        final Set<String> solutions = new HashSet<String>();
        if ((combo == null) || combo.getCandidates().isEmpty()) {
            return solutions;
        }
        for (final String candidate : combo.getCandidates()) {
            if (!SubstitutionSolver.testWord(combo.getWord(), conversions, candidate)) {
                continue;
            }
            final Map<Character, Character> workingConversions = Solvers.asMap(Solvers.asList(combo.getWord()),
                    Solvers.asList(candidate));
            workingConversions.putAll(conversions);
            final String converted = Solvers.convert(originalEncrypted, workingConversions);
            if (converted.toLowerCase().equals(converted)) {
                solutions.add(converted);
                continue;
            }
            final WordCombo nextCombo = SubstitutionSolver.getCheapestWordCombo(this.dictionary, converted);
            if (nextCombo.isEmpty()) {
                continue;
            }
            solutions.addAll(this.churn(converted, nextCombo, workingConversions));
        }
        return solutions;
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
            final WordCombo combo = new WordCombo(word, SubstitutionSolver.getCombinations(dictionary, word, omitted));
            if ((candidate == null) || (combo.getCandidates().size() < candidate.getCandidates().size())) {
                candidate = combo;
            }
        }
        return candidate;
    }

    public Set<String> solve(String encrypted) {
        encrypted = encrypted.toUpperCase();
        return this.churn(encrypted,
                SubstitutionSolver.getCheapestWordCombo(this.dictionary, encrypted),
                new HashMap<Character, Character>());
    }

}
