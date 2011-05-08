package com.bluespot.logic.predicates;

public class TypePredicate<T> implements Predicate<Object> {

	private Class<T> type;

	public TypePredicate(Class<T> type) {
		this.type = type;
	}

	public Class<T> getType() {
		return this.type;
	}

	@Override
	public boolean test(Object candidate) {
		if (candidate == null) {
			return true;
		}
		return getType().isAssignableFrom(candidate.getClass());
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof TypePredicate<?>)) {
			return false;
		}
		final TypePredicate<?> predicate = (TypePredicate<?>) obj;
		if (!this.getType().equals(predicate.getType())) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int result = 23;
		result = 31 * result + this.getType().hashCode();
		return result;
	}

	@Override
	public String toString() {
		return String.format("TypePredicate[%s]", this.getType());
	}

}
