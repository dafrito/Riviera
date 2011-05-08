package com.bluespot.logic.functions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A composite of functions. The behavior for this function is defined by
 * subclasses.
 * 
 * @author Aaron Faanes
 * 
 * @param <I>
 *            the input type acceptable to all child functions
 * @param <V>
 *            the common output type of all child functions
 * 
 */
public abstract class AbstractCompositeFunction<I, V> implements Function<I, V> {

	private final List<? extends Function<? super I, ? extends V>> functions;

	/**
	 * Creates a composite predicate from the specified predicates.
	 * 
	 * @param functions
	 *            the functions used by this object
	 * @throws IllegalArgumentException
	 *             if {@code functions} contains no elements. Degenerate
	 *             composites do not have a predictable return type, so they are
	 *             not allowed.
	 * @throws NullPointerException
	 *             if {@code functions} or any function in {@code functions} is
	 *             null
	 */
	public AbstractCompositeFunction(final Collection<? extends Function<? super I, ? extends V>> functions) {
		if (functions == null) {
			throw new NullPointerException("functions must not be null");
		}
		if (functions.isEmpty()) {
			throw new IllegalArgumentException("functions must contain at least one function");
		}
		this.functions = Collections.unmodifiableList(new ArrayList<Function<? super I, ? extends V>>(functions));
		for (final Function<? super I, ? extends V> function : this.functions) {
			if (function == null) {
				throw new NullPointerException("functions must not contain any null values");
			}
		}
	}

	/**
	 * @return a unmodifiable list of functions used by this object
	 */
	public List<? extends Function<? super I, ? extends V>> getFunctions() {
		return this.functions;
	}

}