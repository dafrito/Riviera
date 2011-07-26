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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.bluespot.logic.actors.Actors;
import com.bluespot.logic.values.MutableValue;

/**
 * Tests for various {@link Clock} implementations.
 * 
 * @author Aaron Faanes
 * @see Clock
 * @see MutableClock
 * @see Timing
 */
public class ClockTests {

	public void oneCanListenToAClock() {
		MutableClock clock = new MutableClock();
		MutableValue<Long> value = new MutableValue<Long>(Long.valueOf(0));
		clock.listen(Actors.wrapLong(Actors.set(value)));
		clock.elapse(10);
		assertEquals(10L, value.get().longValue());
	}

	@Test
	public void coalescingTimersAggregateMissedBeats() {
		MutableClock clock = new MutableClock();
		final MutableValue<Integer> value = new MutableValue<Integer>(Integer.valueOf(0));
		Timing.coalesce(clock, 1, new Runnable() {
			@Override
			public void run() {
				value.set(value.get().intValue() + 1);
			}
		});
		clock.elapse(1);
		assertEquals(Integer.valueOf(1), value.get());
		clock.elapse(4);
		assertEquals(Integer.valueOf(2), value.get());
	}

	/**
	 * Bursting allows timers to catch up from unexpected delays. This example
	 * isn't too obvious from the code, but the benefit is that even though
	 * there was only two ticks from the clock (one of which was very long), the
	 * bursting code allowed the value to catch up.
	 * <p>
	 * For contrast, if this was a coalescing timer, the value would only be
	 * two, rather than four. This is because coalescing timers call their
	 * underlying functions up to as many times as they themselves are invoked.
	 */
	@Test
	public void burstingTimersAllowRecoveryFromUnexpectedDelays() {
		MutableClock clock = new MutableClock();
		final MutableValue<Integer> value = new MutableValue<Integer>(Integer.valueOf(0));
		Timing.burst(clock, 2, new Runnable() {
			@Override
			public void run() {
				value.set(value.get().intValue() + 1);
			}
		});
		clock.elapse(1);
		assertEquals(Integer.valueOf(0), value.get());
		clock.elapse(1);
		assertEquals(Integer.valueOf(1), value.get());
		value.set(0);
		clock.elapse(4);
		assertEquals(Integer.valueOf(2), value.get());
	}

}
