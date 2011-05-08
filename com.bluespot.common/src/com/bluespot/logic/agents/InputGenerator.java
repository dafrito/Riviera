package com.bluespot.logic.agents;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.bluespot.logic.functions.Function;
import com.bluespot.logic.functions.SafeFunction;

public class InputGenerator<I> implements Iterator<I> {

	private I next;

	private Round<I> round;

	public InputGenerator(Collection<? extends Function<Object, ?>> functions, Class<I> guardType) {
		this(functions, guardType, Collections.<I> emptySet());
	}

	/**
	 * Create a new {@link InputGenerator} object that uses the specified
	 * functions and values in the first round.
	 * 
	 * @param functions
	 *            a collection of {@link Function} objects that will be used to
	 *            generate input values
	 * @param inputType
	 *            the required type of generated input values
	 * @param values
	 *            a collection of values that will be passed to each
	 *            {@link Function} in {@code functions}. {@code null} will
	 *            always be passed along with other values.
	 */
	public InputGenerator(Collection<? extends Function<Object, ?>> functions, Class<I> inputType, Collection<? extends I> values) {
		if (functions == null) {
			throw new NullPointerException("functions must not be null");
		}
		this.round = new Round<I>(inputType, functions, values);
		next = this.round.search();
	}

	@Override
	public I next() {
		I thisValue = next;
		while ((next = this.round.search()) == null) {
			this.round = this.round.nextRound();
			next = this.round.search();
		}
		return thisValue;
	}

	@Override
	public boolean hasNext() {
		return next != null;
	}

	private static class Round<I> {

		private final Class<I> inputType;

		private Round<I> nextRound;

		private Set<I> values;
		private Iterator<I> valueIterator;
		private Set<Function<Object, ?>> functions;

		private Function<Object, ?> currentFunc;
		private Iterator<Function<Object, ?>> functionIterator;

		private Round(Class<I> inputType, Collection<? extends Function<Object, ?>> functions, Collection<? extends I> values) {
			this.inputType = inputType;
			this.functions = new HashSet<Function<Object, ?>>(functions);
			if (this.functions.isEmpty()) {
				throw new IllegalArgumentException("functions must contain at least one function");
			}
			this.values = new HashSet<I>(values);
			this.values.add(null);
		}

		public void addFunction(Function<Object, ?> function) {
			this.functions.add(function);
		}

		public void addValue(I value) {
			this.values.add(value);
		}

		private void begin() {
			if (this.nextRound != null) {
				return;
			}
			// We're a new round, so freeze our state and begin.
			this.functions = Collections.unmodifiableSet(this.functions);
			this.values = Collections.unmodifiableSet(this.values);
			this.nextRound = new Round<I>(this.inputType, this.functions, this.values);

			this.functionIterator = this.functions.iterator();
			this.valueIterator = this.values.iterator();
			this.currentFunc = this.functionIterator.next();
		}

		public I search() {
			this.begin();
			while (this.currentFunc != null) {
				while (this.valueIterator.hasNext()) {
					I input = this.valueIterator.next();
					if (this.checkFunction(this.currentFunc, input) != null) {
						return input;
					}
				}
				if (this.functionIterator.hasNext()) {
					this.currentFunc = this.functionIterator.next();
				} else {
					this.currentFunc = null;
				}
			}
			// At this point, we've exhausted every permutation, so just return null.
			return null;
		}

		private I checkFunction(Function<Object, ?> function, I value) {
			Object o = function.apply(null);
			if (o == null) {
				return null;
			}
			Class<I> guardType = this.inputType;
			if (guardType.isInstance(o)) {
				I candidate = guardType.cast(o);
				if (function.apply(candidate) != null) {
					this.nextRound.addValue(candidate);
					return candidate;
				}
			} else if (SafeFunction.class.isInstance(o)) {
				this.nextRound.addFunction((SafeFunction<?>) o);
			}
			return null;
		}

		public Round<I> nextRound() {
			if (this.nextRound == null) {
				throw new IllegalStateException("Next round is not yet available");
			}
			return this.nextRound;
		}

	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("Removal is not supported");
	}

}