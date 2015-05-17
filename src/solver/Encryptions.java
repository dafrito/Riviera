package solver;

import java.util.Collections;
import java.util.List;

public class Encryptions {
	private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public String substitution(final String unencrypted) {
		final List<Character> letters = Solvers.asList(ALPHABET);
		Collections.shuffle(letters);
		return Solvers.convert(unencrypted.toUpperCase(), Solvers.asMap(ALPHABET, letters));
	}

}
