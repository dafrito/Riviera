package logic.adapters;

/**
 * An {@link HandledAdapter} that safely performs a cast. If the cast is not
 * allowed, then a {@link CastingAdapterException} is dispatched to the
 * associated handler.
 * 
 * @author Aaron Faanes
 * 
 * @param <D>
 *            the destination type.
 */
public class CastingAdapter<D> implements Adapter<Object, D> {

	private final Class<? extends D> castType;

	/**
	 * Constructs a {@link CastingAdapter} that converts to the specified type.
	 * 
	 * @param castType
	 *            the type to which given values are cast. It may not be null.
	 */
	public CastingAdapter(final Class<? extends D> castType) {
		if (castType == null) {
			throw new NullPointerException("castType is null");
		}
		this.castType = castType;
	}

	/**
	 * Returns the destination type of this adapter.
	 * 
	 * @return the destination type of this adapter
	 */
	public Class<? extends D> getCastType() {
		return this.castType;
	}

	@Override
	public D adapt(final Object source) {
		if (source == null) {
			return null;
		}
		if (!this.getCastType().isInstance(source)) {
			return null;
		}
		try {
			return this.getCastType().cast(source);
		} catch (ClassCastException e) {
			// The cast should have been guaranteed to succeed, so this error
			// should never fire.
			throw new AssertionError(e);
		}
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof CastingAdapter<?>)) {
			return false;
		}
		final CastingAdapter<?> other = (CastingAdapter<?>) obj;
		if (!this.getCastType().equals(other.getCastType())) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + this.getCastType().hashCode();
		return result;
	}

	@Override
	public String toString() {
		return String.format("cast to %s", this.getCastType());
	}

}
