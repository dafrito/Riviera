/**
 * 
 */
package logic.actors;

import geom.algebra.Algebraic;
import geom.algebra.AlgebraicOperation;

/**
 * @author Aaron Faanes
 * @param <V>
 *            the type of {@link Algebraic} value
 */
public class AlgebraicActor<V extends Algebraic<V>> implements Actor<V> {

	private final V target;
	private final AlgebraicOperation<? super V> operation;

	public AlgebraicActor(final AlgebraicOperation<? super V> operation, V target) {
		if (operation == null) {
			throw new NullPointerException("operation must not be null");
		}
		this.operation = operation;
		if (target == null) {
			throw new NullPointerException("target must not be null");
		}
		this.target = target;
	}

	public V getTarget() {
		return target;
	}

	public AlgebraicOperation<? super V> getOperation() {
		return operation;
	}

	@Override
	public void receive(V value) {
		if (value != null) {
			this.operation.operate(target, value);
		}
	}

	@Override
	public String toString() {
		return String.format("AlgebraicActor[%s on %s]", this.getOperation(), this.getTarget());
	}
}
