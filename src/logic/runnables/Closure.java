/**
 * 
 */
package logic.runnables;

import logic.actors.Actor;

/**
 * Send the specified value to an underlying actor.
 * 
 * @author Aaron Faanes
 * @param <T>
 *            the type of closed value
 */
public class Closure<T> implements Runnable {

	private final Actor<? super T> receiver;
	private final T value;

	public Closure(final Actor<? super T> receiver, final T value) {
		if (receiver == null) {
			throw new NullPointerException("receiver must not be null");
		}
		this.receiver = receiver;
		if (value == null) {
			throw new NullPointerException("value must not be null");
		}
		this.value = value;
	}

	@Override
	public void run() {
		this.receiver.receive(value);
	}

	public Actor<? super T> getActor() {
		return receiver;
	}

	public T getValue() {
		return value;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Closure)) {
			return false;
		}
		Closure<?> other = (Closure<?>) obj;
		return this.getActor().equals(other.getActor()) &&
				this.getValue().equals(other.getValue());
	}

	@Override
	public int hashCode() {
		int result = 51;
		result = 31 * result + this.getActor().hashCode();
		result = 31 * result + this.getValue().hashCode();
		return result;
	}

	@Override
	public String toString() {
		return String.format("Closure[%s on %s]", this.getValue(), this.getActor());
	}
}
