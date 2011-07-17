/**
 * 
 */
package com.bluespot.logic.predicates;

import com.bluespot.logic.adapters.Adapters;

/**
 * Test whether given objects are of a specified type.
 * 
 * @author Aaron Faanes
 * @see Adapters#type()
 */
public class ClassPredicate implements Predicate<Object> {

	private final Class<?> type;

	public ClassPredicate(final Class<?> type) {
		this.type = type;
	}

	public Class<?> getType() {
		return type;
	}

	@Override
	public boolean test(Object candidate) {
		if (candidate == null) {
			return false;
		}
		return this.type.isInstance(candidate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ClassPredicate)) {
			return false;
		}
		ClassPredicate other = (ClassPredicate) obj;
		return this.getType().equals(other.getType());
	}

	@Override
	public int hashCode() {
		return 31 * 17 + this.getType().hashCode();
	}

	@Override
	public String toString() {
		return String.format("ClassPredicate[%s]", this.getType());
	}

}
