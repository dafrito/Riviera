package logic.agents;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import logic.functions.Function;
import logic.functions.Functions;

/**
 * A solver of {@link Function} objects.
 * <p>
 * Functions are solved in the following process:
 * <ol>
 * <li>Determine at least one input value accepted by the function.
 * <li>Evaluate the provided function at those inputs.
 * <li>For each pair, generate at least one function that, when applied the
 * input value, generates an equal output value.
 * </ol>
 * <p>
 * 
 * @author Aaron Faanes
 * 
 * @param <I>
 *            the type of input received by functions
 * @param <V>
 *            the returned value from functions
 */
public class BruteForcingAgent<I, V> implements Function<Function<? super I, ? extends V>, Function<? super I, ? extends V>> {

	private static final int DESIRED_CONFIDENCE = 10;

	private static final int PATIENCE = 100;

	private final Class<? extends I> inputType;

	private final Set<? extends Object> pool;

	public BruteForcingAgent(Class<? extends I> inputType, Collection<? extends Object> pool) {
		if (inputType == null) {
			throw new NullPointerException("inputType must not be null");
		}
		this.inputType = inputType;
		if (pool == null) {
			throw new NullPointerException("pool must not be null");
		}
		this.pool = new HashSet<Object>(pool);
		if (this.pool.isEmpty()) {
			throw new IllegalArgumentException("pool must not be empty");
		}
	}

	public Class<? extends I> getInputType() {
		return this.inputType;
	}

	public Collection<? extends Object> getPool() {
		return this.pool;
	}

	/**
	 * Searching the provided function for possible input values. This iterator
	 * will likely iterate forever, so code that uses it must rely on some other
	 * condition to break out of loops.
	 * 
	 * @return an iterator that generates values of the required input type
	 */
	protected Iterator<? extends I> searchInputs() {
		return new InputGenerator<I>(this.getInputType(), this.getPool());
	}

	protected Iterator<Function<Object, ?>> computeFunctions(I input, V output) {
		InputGenerator<Function<Object, ?>> generator = new InputGenerator<Function<Object, ?>>(
				Functions.safeFunctionType(),
				this.getPool());
		generator.add(input);
		generator.add(output);
		return generator;
	}

	private boolean testCandidate(Function<Object, ?> candidate, I input, V output) {
		Object candidateResult = candidate.apply(input);
		return candidateResult != null && candidateResult.equals(output);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Function<? super I, ? extends V> apply(Function<? super I, ? extends V> function) {
		Function<Object, ?> candidate = null;
		int patience = PATIENCE;
		int confidence = 0;
		Iterator<? extends I> iter = this.searchInputs();
		while (iter.hasNext() && patience-- > 0) {
			I input = iter.next();
			V output = function.apply(input);
			if (output == null) {
				continue;
			}
			if (candidate != null && this.testCandidate(candidate, input, output)) {
				confidence++;
				if (confidence >= DESIRED_CONFIDENCE) {
					// This cast is safe because we've verified these results manually.
					return (Function<? super I, ? extends V>) candidate;
				}
				continue;
			}
			confidence = 0;
			Iterator<Function<Object, ?>> myCandidates = this.computeFunctions(input, output);
			do {
				candidate = null;
				if (!myCandidates.hasNext()) {
					// We can't even generate a function that accepts these inputs, so just die.
					return null;
				}
				candidate = myCandidates.next();
			} while (!this.testCandidate(candidate, input, output));
		}
		// Nothing was good enough, so die. We could do something more sophisticated here - like
		// return the best function we came up with - but I'm not ready to tackle complicated schemes
		// like that.
		return null;
	}
}
