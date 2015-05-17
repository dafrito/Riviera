/**
 * 
 */
package geom.offsets;

import geom.Side;
import geom.vectors.Vector3;

/**
 * Represents rectangular offsets. The precision is implemented by subclasses,
 * so these methods only deal with precision-independent functionality. While
 * this interface alone is workable, most users should use a subclass directly
 * as these subclasses offer much more natural precision-dependent methods.
 * <p>
 * This class is implemented in the style of {@link Vector3} and its subclasses.
 * Specifically, we avoid the need to box primitive types, and we also allow a
 * very flexible form of mutability.
 * 
 * @author Aaron Faanes
 * @param <T>
 *            the type of offset object
 * @see Vector3
 * @see Offset4i
 */
public interface Offset4<T extends Offset4<T>> {

	/**
	 * Return whether this offset is mutable. This value is constant for a given
	 * {@link Offset4} instance.
	 * 
	 * @return whether this offset is mutable
	 */
	public boolean isMutable();

	/**
	 * Sets all sides to the values of the specified offset.
	 * 
	 * @param offset
	 *            the offset that will be copied
	 * @throws UnsupportedOperationException
	 *             if this offset is immutable
	 */
	public void set(T offset);

	/**
	 * Sets the specified sides to the values from the specified offset.
	 * 
	 * @param side
	 *            the sides that will be changed
	 * @param offset
	 *            the offset that will be copied
	 * @throws UnsupportedOperationException
	 *             if this offset is immutable
	 */
	public void set(Side side, T offset);

	/**
	 * Return a {@link Offset4} that uses the specified offset's values for the
	 * specified sides.
	 * 
	 * @param side
	 *            the sides to replace
	 * @param offset
	 *            the offset that is copied. It is not modified by this
	 *            operation.
	 * @return a mutable {@link Offset4} with replaced sides
	 */
	public T with(Side side, T offset);

	/**
	 * Add the specified offset's values to this instance.
	 * 
	 * @param offset
	 *            the offset that will be added to this one. {@code offset} is
	 *            not modified by this operation.
	 * 
	 * @throws UnsupportedOperationException
	 *             if this offset is immutable
	 */
	public void add(T offset);

	/**
	 * Add the specified offset's values to the specified sides of this
	 * instance.
	 * 
	 * @param side
	 *            the sides to add
	 * @param offset
	 *            the offset that is added to this instance. It is not modified
	 *            by this operation
	 * @throws UnsupportedOperationException
	 *             if this offset is immutable
	 */
	public void add(Side side, T offset);

	/**
	 * Return a mutable offset with the specified offset added to it.
	 * 
	 * @param offset
	 *            the offset to add. It will not be modified by this operation.
	 * @return a mutable offset with added sides.
	 */
	public T added(T offset);

	/**
	 * Return a mutable offset with added values at the each specified side.
	 * 
	 * @param side
	 *            the sides to add
	 * @param offset
	 *            the offset that is used to add. It will not be modified by
	 *            this operation
	 * @return a mutable offset with added sides
	 */
	public T added(Side side, T offset);

	/**
	 * Clear all values for this offset.
	 * 
	 * @throws UnsupportedOperationException
	 *             if this offset is immutable
	 */
	public void clear();

	/**
	 * Clear this instance's values at the specified sides.
	 * 
	 * @param side
	 *            the sides that will be cleared
	 * @throws UnsupportedOperationException
	 *             if this offset is immutable
	 */
	public void clear(Side side);

	/**
	 * Return a mutable offset with cleared values at the specified sides.
	 * 
	 * @param side
	 *            the sides to clear
	 * @return a mutable offset with cleared sides
	 */
	public T cleared(Side side);

	/**
	 * Return a mutable copy of this offset.
	 * 
	 * @return a mutable copy of this offset.
	 */
	public T toMutable();

	/**
	 * Return an immutable offset with this instance's values. If this offset is
	 * already immutable, it is returned directly.
	 * 
	 * @return an immutable offset with this instance's values
	 */
	public T toFrozen();

	/**
	 * Return whether the specified offset's values are identical to this
	 * offset. Mutability is not considered during comparison.
	 * 
	 * @param offset
	 *            the offset that is compared against
	 * @return {@code true} if each side's value is equal to the specified
	 *         offset's values
	 */
	public boolean same(T offset);
}
