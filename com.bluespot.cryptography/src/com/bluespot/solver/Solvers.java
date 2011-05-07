package com.bluespot.solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Solvers {

	private Solvers() {
		// Suppress default constructor to ensure non-instantiability.
		throw new AssertionError("Instantiation not allowed");
	}

	public static String convert(final String source, final Map<Character, Character> conversions) {
		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i < source.length(); i++) {
			final char letter = source.charAt(i);
			if (conversions.containsKey(letter)) {
				builder.append(conversions.get(letter));
				continue;
			}
			builder.append(letter);
		}
		return builder.toString();
	}

	public static List<Character> asList(final String word) {
		final List<Character> letters = new ArrayList<Character>(word.length());
		for (final char letter : word.toCharArray()) {
			letters.add(letter);
		}
		return letters;
	}

	public static Map<Character, Character> asMap(final String source, final String target) {
		return Solvers.asMap(Solvers.asList(source), Solvers.asList(target));
	}

	public static Map<Character, Character> asMap(final List<Character> source, final String target) {
		return Solvers.asMap(source, Solvers.asList(target));
	}

	public static Map<Character, Character> asMap(final String source, final List<Character> target) {
		return Solvers.asMap(Solvers.asList(source), target);
	}

	public static <T, U> Map<T, U> asMap(final List<T> source, final List<U> target) {
		final Map<T, U> conversions = new HashMap<T, U>();
		for (int i = 0; i < source.size(); i++) {
			conversions.put(source.get(i), target.get(i));
		}
		return conversions;
	}
}
