/**
 * Copyright (c) 2011 Aaron Faanes
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.bluespot.logic.actors;

import java.util.HashMap;
import java.util.Map;

/**
 * A funnel creates many actors that aggregate received values into a single
 * object.
 * 
 * @author Aaron Faanes
 * @param <E>
 *            the type of states funnelled into this object
 * @param <T>
 *            the type of value accepted by underlying actors
 * @see Actor
 */
public abstract class Funnel<E, T> {

	private final Map<E, Actor<? super T>> forwarders = new HashMap<E, Actor<? super T>>();

	public Actor<? super T> getActor(final E state) {
		if (!forwarders.containsKey(state)) {
			forwarders.put(state, new Actor<T>() {
				@Override
				public void receive(T value) {
					Funnel.this.receive(state, value);
				}
			});
		}
		return forwarders.get(state);
	}

	/**
	 * Receive a value with an accompanying state. This function is called by
	 * underlying actors, who have states associated with them.
	 * 
	 * @param state
	 *            the accompanying state
	 * @param value
	 *            the received value
	 */
	protected abstract void receive(E state, T value);

}
