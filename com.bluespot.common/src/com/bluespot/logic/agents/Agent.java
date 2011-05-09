package com.bluespot.logic.agents;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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

	private final Class<I> inputType;

	private final Set<? extends Function<Object, ?>> functions;

	public Agent(Class<I> inputType, Collection<? extends Function<Object, ?>> functions) {
		if (inputType == null) {
			throw new NullPointerException("inputType must not be null");
		}
		this.inputType = inputType;
		if (functions == null) {
			throw new NullPointerException("functions must not be null");
		}
		this.functions = new HashSet<Function<Object, ?>>(functions);
		if (this.functions.isEmpty()) {
			throw new IllegalArgumentException("functions must contain at least one function");
		}
	}

	public Class<I> getInputType() {
		return this.inputType;
	}

	public Collection<? extends Function<Object, ?>> getFunctions() {
		return this.functions;
	}

	/**
	 * Searching the provided function for possible input values. This iterator
	 * will likely iterate forever, so code that uses it must rely on some other
	 * condition to break out of loops.
	 * 
	 * @return an iterator that generates values of the required input type
	 */
	protected Iterator<? extends I> searchInputs() {
		return new InputGenerator<I>(this.getFunctions(), this.getInputType());
	}

	protected Collection<? extends Function<? super I, ? extends V>> computeFunctions(I input, V output) {
		return null;
	}

	@Override
	public Function<? super I, ? extends V> apply(Function<? super I, ? extends V> function) {
		Collection<? extends Function<? super I, ? extends V>> candidates = null;
		Iterator<? extends I> iter = this.searchInputs();
		while (iter.hasNext()) {
			I input = iter.next();
			V output = function.apply(input);
			if (output == null) {
				continue;
			}
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
