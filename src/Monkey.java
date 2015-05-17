import java.util.HashSet;
import java.util.Set;

import generate.Randomness;

/**
 * A monkey attempts to reproduce strings through random generation of
 * characters. The monkey's goal is to solve the target string, where solving in
 * this case, is producing a string that matches the specified target string.
 * <p>
 * Monkey objects employ two separate mechanisms for solving strings:
 * incremental, and mad-scientist. Incremental saves matched letters in a
 * string, and only guesses on letters that are not matching. In this way, the
 * string is guessed relatively quickly. The mad-scientist variation does not
 * save correct letters, and only attempts to guess the entire string in its
 * entirety. Because of this, the median times for the mad-scientist approach
 * are horrendous and, in varied strings longer than ten characters or so,
 * astronomical.
 * <p>
 * Both algorithms will pick random letters from a set of letters that are
 * contained in the target string. This is different than some implementations
 * that merely use the alphabet regardless of the string. The benefit is that
 * this allows arbitrary punctuation to be inserted without dramatically
 * increasing median times for average strings. The drawback is that our monkeys
 * solve simple strings much faster than anticipated since the solution set is
 * reduced.
 * <p>
 * As a design note, this class should be refactored such that the strategy is
 * separate. One could also separate the strategy for letter generation so that
 * a purely random generation could be performed. I personally don't feel like
 * doing it and am surprised this has been documented so well thus far in the
 * first place.
 * <p>
 * This should also end up in cryptography, rather than playground, but thus far
 * there's very little worth saving.
 * 
 * 
 * @author Aaron Faanes
 * 
 */
public class Monkey {

	private int iterations = 0;

	private final String target;

	private final char[] candidates;

	/**
	 * Constructs a new {@link Monkey} that will solve for the specified target
	 * string.
	 * 
	 * @param target
	 *            the target string that will be solved
	 */
	public Monkey(final String target) {
		if (target == null) {
			throw new NullPointerException("target is null");
		}
		this.target = target;
		final Set<Character> characterSet = new HashSet<Character>();
		for (int i = 0; i < this.target.length(); i++) {
			// Fill our characterSet with all characters in the target
			characterSet.add(this.target.charAt(i));
		}
		this.candidates = new char[characterSet.size()];
		int i = 0;
		for (final Character candidate : characterSet.toArray(new Character[0])) {
			this.candidates[i++] = candidate;
		}
	}

	/**
	 * Attempts to solve the target string by randomly guessing strings of the
	 * same length. In practice, this algorithm is only capable of solving the
	 * simplest of combinations and quickly becomes permanently unresponsive.
	 * Therefore, this algorithm exists mostly for demonstrative purposes.
	 * <p>
	 * As a side note, this actually isn't the worst-case algorithm. Since we
	 * use a list of candidate characters instead of the whole alphabet, very
	 * short strings will be solved very quickly since their solution set is
	 * very small.
	 * <p>
	 * This method is not idempotent, so don't reuse monkeys.
	 * 
	 * @return the number of iterations it took to solve for this string
	 */
	public int madScientistSolve() {
		final StringBuilder builder = new StringBuilder();
		while (true) {
			for (int i = 0; i < this.target.length(); i++) {
				builder.append(Randomness.choice(this.candidates));
			}
			if (builder.toString().equals(this.target)) {
				break;
			}
			this.iterations++;
			builder.delete(0, builder.length());
		}
		return this.iterations;
	}

	/**
	 * Attempts to solve the target string by randomly guessing letters. When a
	 * letter is matched with the target letter, that letter is saved. This
	 * allows the algorithm to perform relatively well with "difficult" strings.
	 * <p>
	 * This, like {@link #madScientistSolve()}, uses a set of possible
	 * characters, rather than a pre-constructed set of the alphabet, so simple
	 * strings will be solved much faster than ones with a lot of variation.
	 * 
	 * @return the number of iterations it took to solve for this target string
	 */
	public int incrementalSolve() {
		// Construct the initial working string
		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i < this.target.length(); i++) {
			builder.append(Randomness.choice(this.candidates));
		}
		// Loop until we've generated the string
		while (!builder.toString().equals(this.target)) {
			for (int i = 0; i < this.target.length(); i++) {
				if (this.target.charAt(i) != builder.charAt(i)) {
					builder.setCharAt(i, Randomness.choice(this.candidates));
				}
			}
			this.iterations++;
		}
		return this.iterations;
	}

	/**
	 * Returns the list of candidate characters for the target string. Do not
	 * modify the returned array.
	 * 
	 * @return the list of unique characters that are in the target string
	 */
	public char[] getCandidates() {
		return this.candidates;
	}

	/**
	 * A very easy string to solve. Ironically, this is about the upper limit
	 * for the mad-scientist approach.
	 */
	public static final String UNDERLING = "Foobs";

	/**
	 * A difficult string to solve for the incremental strategy. The
	 * mad-scientist strategy is statistically unable to solve this one.
	 */
	public static final String BOSS = "Whos there Nay answer me stand and unfold yourself Long live";

	private static final int ITERATIONS = 1000;

	/**
	 * Solves for the specified string using an incremental search. See
	 * {@link #incrementalSolve()} for more details.
	 * 
	 * @param target
	 *            the target string that is solved
	 */
	public static void solveIncrementally(final String target) {
		final int[] times = new int[ITERATIONS];
		for (int i = 0; i < ITERATIONS; i++) {
			final Monkey monkey = new Monkey(BOSS);
			times[i] = monkey.incrementalSolve();
		}
		long sum = 0;
		for (final int time : times) {
			sum += time;
		}
		System.out.println(new Monkey(target).getCandidates().length);
		System.out.println(target.length());
		System.out.println("Average: " + (sum / 1000.0d));
	}

	/**
	 * Solves for the specified string using {@link #madScientistSolve()}.
	 * 
	 * @param target
	 *            the target string that is solved
	 */
	public static void solveMadScientist(final String target) {
		System.out.printf("Solved in %d iterations", new Monkey(target).madScientistSolve());
	}

	/**
	 * Launches the monkeys to solve some string.
	 * 
	 * @param args
	 *            unused
	 */
	public static void main(final String[] args) {
		Monkey.solveIncrementally(BOSS);
	}
}
