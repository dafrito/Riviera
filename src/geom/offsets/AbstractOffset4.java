/**
 * 
 */
package geom.offsets;

import geom.Side;


/**
 * An {@link Offset4i} that implements some basic functionality. Most
 * implemented functions deal with copy-and-mutate methods, since these don't
 * depend on internals.
 * 
 * @author Aaron Faanes
 * @param <T>
 *            the type of offset
 * 
 */
public abstract class AbstractOffset4<T extends Offset4<T>> implements Offset4<T> {

	private final boolean mutable;

	protected AbstractOffset4(boolean mutable) {
		this.mutable = mutable;
	}

	@Override
	public boolean isMutable() {
		return this.mutable;
	}

	@Override
	public T with(Side side, T offset) {
		if (side == null) {
			throw new NullPointerException("side must not be null");
		}
		if (offset == null) {
			throw new NullPointerException("offset must not be null");
		}
		T result = this.toMutable();
		result.set(side, offset);
		return result;
	}

	@Override
	public T added(T offset) {
		T result = this.toMutable();
		result.add(offset);
		return result;
	}

	@Override
	public T added(Side side, T offset) {
		if (side == null) {
			throw new NullPointerException("side must not be null");
		}
		if (offset == null) {
			throw new NullPointerException("offset must not be null");
		}
		T result = this.toMutable();
		result.add(side, offset);
		return result;
	}

	@Override
	public T cleared(Side side) {
		T result = this.toMutable();
		result.clear(side);
		return result;
	}

}
