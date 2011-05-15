/**
 * 
 */
package com.bluespot.geom.points;

/**
 * A basic {@link Point3} implementation.
 * 
 * @author Aaron Faanes
 * 
 * @param <P>
 *            the type of this point
 */
public abstract class AbstractPoint3<P extends Point3<P>> implements Point3<P> {

	private final boolean mutable;

	protected AbstractPoint3(boolean mutable) {
		this.mutable = mutable;
	}

	@Override
	public boolean isMutable() {
		return this.mutable;
	}

}
