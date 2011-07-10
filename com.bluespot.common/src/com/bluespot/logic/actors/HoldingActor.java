/**
 * 
 */
package com.bluespot.logic.actors;

import com.bluespot.logic.values.Value;

/**
 * An {@link Actor} that uses received values for a {@link Value}
 * implementation.
 * 
 * @author Aaron Faanes
 * @param <V>
 *            the type of received value
 */
public class HoldingActor<V> implements Actor<V>, Value<V> {

	private V value;

	public HoldingActor(V initialValue) {
		this.value = initialValue;
	}

	@Override
	public void receive(V value) {
		this.value = value;
	}

	@Override
	public V get() {
		return value;
	}

	@Override
	public String toString() {
		return String.format("Holding[%s]", value);
	}

}
