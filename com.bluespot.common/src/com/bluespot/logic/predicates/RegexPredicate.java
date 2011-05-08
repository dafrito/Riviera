package com.bluespot.logic.predicates;

import java.util.regex.Pattern;

import com.bluespot.logic.adapters.Adapters;

/**
 * A {@link Predicate} that evaluates strings against a specified regular
 * expression. It will evaluate to {@code true} for a tested value if, and only
 * if, the regular expression evaluates to {@code true} for that tested value.
 * Null values evaluate to {@code false}.
 * <p>
 * This class does not implicitly convert non-{@code String} objects to strings.
 * This is intentional: It is preferred that clients explicitly convert their
 * values to strings instead of relying on this to perform arbitrary behavior on
 * objects. Using {@link #toString()} would be a viable option, but that would
 * imply that {@code toString()} represents a significant and
 * contractually-binding description of the object. Since this is not the
 * intended spirit of that method, we do not use it. Clients are encouraged to
 * use the {@link Adapters} library for making string conversions easier, and
 * {@link Adapters#stringValue()} specifically if you wish to use
 * {@link #toString()} for conversion.
 * 
 * @author Aaron Faanes
 * 
 */
public final class RegexPredicate implements Predicate<String> {

	private final Pattern pattern;

	/**
	 * Constructs a predicate using the specified pattern
	 * 
	 * @param pattern
	 *            the pattern used in this predicate
	 * @throws NullPointerException
	 *             if {@code pattern} is null
	 */
	public RegexPredicate(final Pattern pattern) {
		if (pattern == null) {
			throw new NullPointerException("pattern is null");
		}
		this.pattern = pattern;
	}

	/**
	 * Returns the regular expression used in this predicate.
	 * 
	 * @return the regular expression used in this predicate
	 */
	public Pattern getPattern() {
		return this.pattern;
	}

	@Override
	public boolean test(final String candidate) {
		if (candidate == null) {
			return false;
		}
		return this.getPattern().matcher(candidate).matches();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof RegexPredicate)) {
			return false;
		}
		final RegexPredicate other = (RegexPredicate) obj;
		return this.getPattern().equals(other.getPattern());
	}

	@Override
	public int hashCode() {
		int result = 13;
		result = 31 * result + this.getPattern().hashCode();
		return result;
	}

	@Override
	public String toString() {
		return String.format("matches '%s'", this.getPattern());
	}

}
