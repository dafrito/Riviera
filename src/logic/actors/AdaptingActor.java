package logic.actors;

import logic.adapters.Adapter;

/**
 * A {@link Actor} that uses a specified {@link Adapter} to convert accepted
 * values. The converted values are then sent to the specified target actor.
 * Converted values are sent as-is to the target actor; no filtering is
 * performed.
 * 
 * @author Aaron Faanes
 * 
 * @param <S>
 *            the type of value that is initially accepted by this actor
 * @param <D>
 *            the type of value that is sent to the target actor
 * @see Adapter
 */
public final class AdaptingActor<S, D> implements Actor<S> {

	private final Adapter<? super S, ? extends D> adapter;
	private final Actor<? super D> target;

	/**
	 * Constructs a new {@link AdaptingActor} that uses the specified adapter to
	 * convert accepted values. The converted values are then sent to the
	 * specified target actor.
	 * 
	 * @param adapter
	 *            the adapter that performs conversion for this actor
	 * @param target
	 *            the actor that ultimately accepts the converted values
	 * @throws NullPointerException
	 *             if either {@code adapter} or {@code target} is null
	 */
	public AdaptingActor(final Adapter<? super S, ? extends D> adapter, final Actor<? super D> target) {
		if (adapter == null) {
			throw new NullPointerException("adapter is null");
		}
		if (target == null) {
			throw new NullPointerException("target is null");
		}
		this.adapter = adapter;
		this.target = target;
	}

	/**
	 * @return the underlying target of this actor
	 */
	public Actor<? super D> getTarget() {
		return this.target;
	}

	/**
	 * Returns the adapter used to convert values sent to this actor to values
	 * that are acceptable to the underlying target.
	 * 
	 * @return the adapter used to convert accepted values
	 */
	public Adapter<? super S, ? extends D> getAdapter() {
		return this.adapter;
	}

	@Override
	public void receive(final S value) {
		this.getTarget().receive(this.getAdapter().adapt(value));
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof AdaptingActor<?, ?>)) {
			return false;
		}
		final AdaptingActor<?, ?> other = (AdaptingActor<?, ?>) obj;
		if (!this.getAdapter().equals(other.getAdapter())) {
			return false;
		}
		if (!this.getTarget().equals(other.getTarget())) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int result = 7;
		result = 31 * result + this.getAdapter().hashCode();
		result = 31 * result + this.getTarget().hashCode();
		return result;
	}

	@Override
	public String toString() {
		return String.format("AdaptingActor - adapter: %s, target: %s", this.getAdapter(), this.getTarget());
	}

}
