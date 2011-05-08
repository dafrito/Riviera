package com.bluespot.logic;

import java.util.Collection;
import java.util.HashSet;

import com.bluespot.logic.functions.Function;
import com.bluespot.logic.functions.UnanimousFunction;

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
public class Agent<I, V> implements Function<Function<? super I, ? extends V>, Function<? super I, ? extends V>> {

	private final Class<I> guardType;

	public Agent(Class<I> guardType) {
		if (guardType == null) {
			throw new NullPointerException("guardType must not be null");
		}
		this.guardType = guardType;
	}

	public Class<I> getGuardType() {
		return this.guardType;
	}

	protected Iterable<? extends I> searchInputs(Function<? super I, ? extends V> input) {
		return null;
	}

	protected Collection<? extends Function<? super I, ? extends V>> computeFunctions(I input, V output) {
		return null;
	}

	@Override
	public Function<? super I, ? extends V> apply(Function<? super I, ? extends V> function) {
		Collection<? extends Function<? super I, ? extends V>> candidates = null;
		for (I input : this.searchInputs(function)) {
			V output = function.apply(input);
			Collection<? extends Function<? super I, ? extends V>> myCandidates = this.computeFunctions(input, output);
			if (candidates != null) {
				// We run into the problem of equality here. Consider these two functions:
				// y = x + 2
				// y = x + 2 + 1 - 1
				// These are logically equivalent, so at least one of them should be
				// retained. However, they're not equal according to Object#equals, so
				// we'd lose this result. 
				// 
				// To resolve this, we either need some simplifying process, or we need to
				// ensure that functions are always produced in their simplest form.
				//
				// Also consider the possibility that the result might be composed of two
				// separate functions. In this case, there may not be a single function that
				// fits both results. In this scenario, we should attempt to create a
				// piece-wise function from the discarded candidates.
				candidates.retainAll(myCandidates);
			} else {
				candidates = new HashSet<Function<? super I, ? extends V>>(myCandidates);
			}
			if (candidates.isEmpty()) {
				return null;
			} else if (candidates.size() == 1) {
				return candidates.iterator().next();
			}
		}
		if (candidates == null || candidates.isEmpty()) {
			return null;
		}
		return new UnanimousFunction<I, V>(candidates);
	}

}
