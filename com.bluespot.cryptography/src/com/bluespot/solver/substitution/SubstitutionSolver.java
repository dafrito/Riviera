package com.bluespot.solver.substitution;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import com.bluespot.dictionary.Dictionary;
import com.bluespot.solver.Solver;
import com.bluespot.solver.SolverListener;
import com.bluespot.solver.Solvers;
import com.bluespot.solver.WordCombo;

public class SubstitutionSolver implements Solver<String, String> {

	private final Dictionary dictionary;

	public SubstitutionSolver() {
		this(Dictionary.getDictionary());
	}

	public SubstitutionSolver(final Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	private int[] getConversionTable(final String word) {
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

	private boolean potentialMatch(final String encryptedWord, final String candidate,
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

	private Set<String> getCombinations(String encryptedWord, Set<Character> solvedLetters) {
		final Dictionary sameLengthWords = this.dictionary.wordsOfLength(encryptedWord);
		final int[] conversionTable = this.getConversionTable(encryptedWord);

		final Set<String> matches = new HashSet<String>();
		for (final String candidate : sameLengthWords) {
			if (!this.potentialMatch(encryptedWord, candidate, solvedLetters)) {
				continue;
			}
			final int[] candidateConversionTable = this.getConversionTable(candidate);
			if (Arrays.equals(conversionTable, candidateConversionTable)) {
				matches.add(candidate);
			}
		}
		return matches;
	}

	private boolean testWord(final String encryptedWord, final Map<Character, Character> conversions,
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

	private Set<String> churn(final String originalEncrypted, final WordCombo combo,
			final Map<Character, Character> conversions) {
		final Set<String> solutions = new HashSet<String>();
		if ((combo == null) || combo.getCandidates().isEmpty()) {
			return solutions;
		}
		for (final String candidate : combo.getCandidates()) {
			if (!this.testWord(combo.getWord(), conversions, candidate)) {
				continue;
			}
			final Map<Character, Character> workingConversions = Solvers.asMap(Solvers.asList(combo.getWord()),
					Solvers.asList(candidate));
			workingConversions.putAll(conversions);
			final String converted = Solvers.convert(originalEncrypted, workingConversions);
			if (converted.toLowerCase().equals(converted)) {
				this.dispatchResult(converted);
				solutions.add(converted);
				continue;
			}
			final WordCombo nextCombo = this.getCheapestWordCombo(converted);
			if (nextCombo.isEmpty()) {
				continue;
			}
			solutions.addAll(this.churn(converted, nextCombo, workingConversions));
		}
		return solutions;
	}

	private WordCombo getCheapestWordCombo(final String encrypted) {
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
			final WordCombo combo = new WordCombo(word, this.getCombinations(word, omitted));
			if ((candidate == null) || (combo.getCandidates().size() < candidate.getCandidates().size())) {
				candidate = combo;
			}
		}
		return candidate;
	}

	@Override
	public Set<String> solve(String encrypted) {
		encrypted = encrypted.toUpperCase();
		Set<String> results = this.churn(encrypted,
				this.getCheapestWordCombo(encrypted),
				new HashMap<Character, Character>());
		this.dispatchFinished();
		return results;
	}

	private final List<SolverListener<? super String>> listeners = new CopyOnWriteArrayList<SolverListener<? super String>>();

	protected void dispatchResult(Iterable<String> results) {
		for (String result : results) {
			this.dispatchResult(result);
		}
	}

	protected void dispatchFinished() {
		for (SolverListener<? super String> listener : this.listeners) {
			listener.finished();
		}
	}

	protected void dispatchResult(String result) {
		for (SolverListener<? super String> listener : this.listeners) {
			listener.onSolution(result);
		}
	}

	@Override
	public void addSolverListener(SolverListener<? super String> listener) {
		this.listeners.add(listener);
	}

}
