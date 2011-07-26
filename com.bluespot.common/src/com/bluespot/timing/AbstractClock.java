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
package com.bluespot.timing;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.bluespot.logic.actors.LongActor;

/**
 * @author Aaron Faanes
 * 
 */
public class AbstractClock implements Clock {

	private final List<LongActor> listeners = new CopyOnWriteArrayList<LongActor>();

	@Override
	public void listen(LongActor actor) {
		if (actor == null) {
			throw new NullPointerException("actor must not be null");
		}
		listeners.add(actor);
	}

	@Override
	public void detach(LongActor actor) {
		if (actor == null) {
			throw new NullPointerException("actor must not be null");
		}
		listeners.remove(actor);
	}

	protected void elapse(long ticks) {
		for (LongActor actor : listeners) {
			actor.receive(ticks);
		}
	}
}
