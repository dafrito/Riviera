package com.bluespot.logic.predicates;

/**
 * A predicate that tests for equality.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the type of value to test
 */
public final class EqualityPredicate<T> implements Predicate<T> {

	private final T referenceValue;

	/**
	 * Constructs a predicate that equates to the specified value
	 * 
	 * @param referenceValue
	 *            the value to test against.
	 * @throws NullPointerException
	 *             if the specified value is {@code null}
	 */
	public EqualityPredicate(final T referenceValue) {
		if (referenceValue == null) {
			throw new NullPointerException("referenceValue is null");
		}
		this.referenceValue = referenceValue;
	}

	/**
	 * @return the value that this predicate uses in its evaluation
	 */
	public T getReferenceValue() {
		return this.referenceValue;
	}

	@Override
	public boolean test(final T value) {
		return this.referenceValue.equals(value);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof EqualityPredicate<?>)) {
			return false;
		}
		final EqualityPredicate<?> predicate = (EqualityPredicate<?>) obj;
		return this.getReferenceValue().equals(predicate.getReferenceValue());
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + this.getReferenceValue().hashCode();
		return result;
	}

	@Override
	public String toString() {
		return String.format("is %s", this.getReferenceValue());
	}
}
