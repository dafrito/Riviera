package com.bluespot.util;

import java.util.Collections;
import java.util.Iterator;

public final class Ranges {

	private static abstract class Range<T extends Comparable<T>> implements Iterable<T> {

		protected final T endValue;
		protected final T initialValue;
		protected final T step;

		public Range(final T initialValue, final T endValue, final T step) {
			this.initialValue = initialValue;
			this.endValue = endValue;
			this.step = step;
		}

		public abstract T add(T value, T increment);

		public boolean isIncrementing() {
			return this.initialValue.compareTo(this.endValue) < 0;
		}

		public Iterator<T> iterator() {
			return new Iterator<T>() {

				private T currentValue = Range.this.initialValue;

				public boolean hasNext() {
					final int comparison = this.currentValue.compareTo(Range.this.endValue);
					if (Range.this.isIncrementing()) {
						return comparison < 0;
					}
					return comparison > 0;
				}

				public T next() {
					final T oldValue = this.currentValue;
					this.currentValue = Range.this.add(this.currentValue, Range.this.step);
					return oldValue;
				}

				public void remove() {
					throw new UnsupportedOperationException();
				}
			};
		}
	}

	private Ranges() {
		// Disallowed.
	}

	public static Iterable<Integer> range(final int endValue) {
		return Ranges.range(0, endValue, endValue > 0 ? 1 : -1);
	}

	public static Iterable<Integer> range(final int initialValue, final int endValue) {
		return Ranges.range(initialValue, endValue, endValue > initialValue ? 1 : -1);
	}

	public static Iterable<Integer> range(final int initialValue, final int endValue, final int step) {
		if ((initialValue < endValue && step < 0) || (initialValue > endValue && step > 0) || step == 0) {
			throw new IllegalArgumentException("Range will never complete.");
		} else if (initialValue == endValue) {
			return Collections.emptyList();
		}

		return new Range<Integer>(initialValue, endValue, step) {

			@Override
			public Integer add(final Integer value, final Integer increment) {
				return value + increment;
			}
		};

	}

}
