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

import com.bluespot.logic.actors.LongActor;

/**
 * @author Aaron Faanes
 * 
 */
public interface Clock {

	/**
	 * @return the number of ticks that have elapsed, according to this clock.
	 */
	public long getElapsed();

	/**
	 * Register the specified actor to receive updates regarding this clock's
	 * time. The listener will be called every time the clock updates.
	 * 
	 * @param actor
	 *            the actor that will periodically receive tick counts
	 */
	public void listen(LongActor actor);

	/**
	 * Remove the specified actor, so that it will receive no more updates.
	 * 
	 * @param actor
	 *            the actor to detach
	 */
	public void detach(LongActor actor);

}
