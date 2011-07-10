/**
 * 
 */
package com.bluespot.geom.algebra;

/**
 * An operation on {@link Algebraic} values.
 * 
 * @author Aaron Faanes
 * @param <V>
 *            the type of {@link Algebraic} value
 */
public interface AlgebraicOperation<V extends Algebraic<V>> {
	public void operate(V a, V b);
}
