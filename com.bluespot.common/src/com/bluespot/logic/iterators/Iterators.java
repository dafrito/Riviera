/**
 * 
 */
package com.bluespot.logic.iterators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.bluespot.logic.actors.Actor;
import com.bluespot.logic.actors.Actors;
import com.bluespot.logic.adapters.Adapter;
import com.bluespot.logic.predicates.Predicate;
import com.bluespot.logic.values.Value;
import com.bluespot.logic.values.Values;

/**
 * A collection of utility methods that create {@link Iterator} objects.
 * <p>
 * Iterators generally don't override {@link Object#equals(Object)} since
 * they're inherently mutable and relatively unstable. As a result, the
 * implementations returned here don't override it either.
 * 
 * @author Aaron Faanes
 * @see Iterator
 */
public final class Iterators {

	private Iterators() {
		throw new AssertionError("Instantiation is not allowed");
	}

	public static <T> Iterator<T> repeat(Iterator<? extends T> underlying) {
		return new RepeatingIterator<T>(underlying);
	}

	public static <S, D> Iterator<D> adapt(Iterator<? extends S> source, Adapter<? super S, ? extends D> adapter) {
		return new AdaptingIterator<S, D>(source, adapter);
	}

	public static <T> StaggeredIterator<T> stagger(Iterator<? extends T> iter) {
		return new StaggeredIterator<T>(iter);
	}

	public static <T> ChainingIterator<T> chain(Iterator<? extends T> iter) {
		return new ChainingIterator<T>(iter);
	}

	public static <T> Iterator<T> value(Value<? extends T> value) {
		return new ValueIterator<T>(value);
	}

	public static <T> Iterator<T> constant(T constant) {
		return new ValueIterator<T>(Values.constant(constant));
	}

	public static IntegerIterator count(int initial, int increment) {
		return new IntegerIterator(initial, increment);
	}

	public static IntegerIterator count(int initial, int increment, int lastValue) {
		return new IntegerIterator(initial, increment, lastValue);
	}

	public static IntegerIterator range(int first, int last) {
		int increment = first < last ? 1 : -1;
		return new IntegerIterator(first, increment, last);
	}

	public static <T> Iterator<T> sideEffect(Iterator<? extends T> underlying, Actor<? super T> sideEffect) {
		return new SideEffectIterator<T>(underlying, sideEffect);
	}

	public static <T> Iterator<T> lazy(Iterable<? extends T> underlying) {
		return new LazyIterator<T>(underlying);
	}

	public static <T> Iterator<T> saving(Iterator<? extends T> underlying) {
		List<T> values = new ArrayList<T>();
		return Iterators.chain(Iterators.sideEffect(underlying, Actors.addTo(values))).add(
				Iterators.lazy(values)
				);
	}

	public static void exhaust(Iterator<?> iterator) {
		if (iterator == null) {
			throw new NullPointerException("iterator must not be null");
		}
		while (iterator.hasNext()) {
			iterator.next();
		}
	}

	public static void exhaust(Iterator<?> iterator, int limit) {
		if (iterator == null) {
			throw new NullPointerException("iterator must not be null");
		}
		if (limit < 0) {
			throw new IllegalArgumentException("limit must be positive");
		}
		while (iterator.hasNext() && limit-- > 0) {
			iterator.next();
		}
	}

	public static <T> void fill(Iterator<? extends T> iterator, Collection<? super T> collection) {
		if (iterator == null) {
			throw new NullPointerException("iterator must not be null");
		}
		if (collection == null) {
			throw new NullPointerException("collection must not be null");
		}
		while (iterator.hasNext()) {
			collection.add(iterator.next());
		}
	}

	public static <T> void fill(Iterator<? extends T> iterator, Collection<? super T> collection, int limit) {
		if (iterator == null) {
			throw new NullPointerException("iterator must not be null");
		}
		if (collection == null) {
			throw new NullPointerException("collection must not be null");
		}
		if (limit < 0) {
			throw new IllegalArgumentException("limit must be a positive integer");
		}
		while (iterator.hasNext() && limit-- > 0) {
			collection.add(iterator.next());
		}
	}

	public static <T> List<T> fill(Iterator<? extends T> iterator) {
		List<T> elements = new ArrayList<T>();
		Iterators.fill(iterator, elements);
		return elements;
	}

	public static <T> List<T> fill(Iterator<? extends T> iterator, int limit) {
		List<T> elements = new ArrayList<T>();
		Iterators.fill(iterator, elements, limit);
		return elements;
	}

	public static Iterator<Integer> fibonacci() {
		return new Iterator<Integer>() {

			private int a = -1;
			private int b = -1;

			@Override
			public boolean hasNext() {
				return true;
			}

			@Override
			public Integer next() {
				if (a == -1) {
					a = 0;
					return a;
				} else if (b == -1) {
					b = 1;
					return b;
				} else {
					int next = a + b;
					a = b;
					b = next;
					return next;
				}
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("Removal is not supported on this iterator");
			}
		};
	}

	public static <T> Iterator<T> limited(Iterator<? extends T> underlying, int charges) {
		return new LimitedIterator<T>(underlying, charges);
	}

	public static <T> Iterator<T> guard(Iterator<? extends T> underlying, Predicate<? super T> sentinel) {
		return new GuardedIterator<T>(underlying, sentinel);
	}
}
