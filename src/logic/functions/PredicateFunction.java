package logic.functions;

import logic.predicates.Predicate;

/**
 * A {@link Function} that returns a value retrieved from a {@link Predicate}.
 * 
 * @author Aaron Faanes
 * 
 * @param <V>
 *            the type of the tested value
 */
public class PredicateFunction<V> implements Function<V, Boolean> {

	private final Predicate<? super V> predicate;

	public PredicateFunction(Predicate<? super V> predicate) {
		if (predicate == null) {
			throw new NullPointerException("Predicate must not be null");
		}
		this.predicate = predicate;
	}

	public Predicate<? super V> getPredicate() {
		return this.predicate;
	}

	@Override
	public Boolean apply(V input) {
		return predicate.test(input);
	}

	@Override
	public String toString() {
		return String.format("PredicateFunction[%s]", predicate);
	}

	@Override
	public int hashCode() {
		int result = 51;
		result *= 31 * result + getPredicate().hashCode();
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof PredicateFunction<?>)) {
			return false;
		}
		final PredicateFunction<?> other = (PredicateFunction<?>) obj;
		return this.getPredicate().equals(other.getPredicate());
	}

}
