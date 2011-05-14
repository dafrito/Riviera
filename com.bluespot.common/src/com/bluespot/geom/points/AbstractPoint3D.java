/**
 * 
 */
package com.bluespot.geom.points;

/**
 * A basic {@link Point3D} implementation.
 * 
 * @author Aaron Faanes
 * 
 * @param <P>
 *            the type of this point
 */
public abstract class AbstractPoint3D<P extends Point3D<?>> implements Point3D<P> {

	private final boolean mutable;

	protected AbstractPoint3D(boolean mutable) {
		this.mutable = mutable;
	}

	@Override
	public boolean isMutable() {
		return this.mutable;
	}

}
