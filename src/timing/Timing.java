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
package timing;

import logic.actors.LongActor;

/**
 * @author Aaron Faanes
 * 
 */
public final class Timing {

	private Timing() {
		throw new AssertionError("Instantiation not allowed");
	}

	/**
	 * Register the specified runnable to run periodically. The specified clock
	 * will be used to determine elapsed time. After {@code period} ticks, the
	 * specified runnable will be invoked.
	 * <p>
	 * If the underlying clock has a delay longer than {@code period}, then
	 * invocations will be "lost". If you need to ensure an expected number of
	 * invocations will occur over a given timespan, use
	 * {@link #burst(MutableClock, long, Runnable)}.
	 * 
	 * @param clock
	 *            the clock that will provide elapsed tick updates
	 * @param period
	 *            the minimum number of ticks between each invocation
	 * @param runnable
	 *            the runnable that will be invoked
	 * @return a {@link LongActor} that represents this operation. It is
	 *         returned so that the runnable may be detached from the specified
	 *         clock at a later time.
	 */
	public static LongActor coalesce(MutableClock clock, final long period, final Runnable runnable) {
		LongActor actor = new LongActor() {
			private long elapsed = 0;

			@Override
			public void receive(long ticks) {
				elapsed += ticks;
				if (elapsed >= period) {
					runnable.run();
				}
				elapsed = 0;
			}
		};
		clock.listen(actor);
		return actor;
	}

	/**
	 * Register the specified runnable to run periodically. The specified clock
	 * will be used to determine elapsed time. After {@code period} ticks, the
	 * specified runnable will be invoked.
	 * <p>
	 * If the underlying clock has a delay longer than {@code period}, then the
	 * runnable will be invoked repeatedly and immediately to "catch up" to the
	 * desired number of invocations. As a result, the {@code period} between
	 * invocations may be much shorter than is specified.
	 * 
	 * @param clock
	 *            the clock that will provide elapsed tick updates
	 * @param period
	 *            the desired number of ticks between each invocation
	 * @param runnable
	 *            the runnable that will be invoked
	 * @return a {@link LongActor} that represents this operation. It is
	 *         returned so that the runnable may be detached from the specified
	 *         clock at a later time.
	 */
	public static LongActor burst(MutableClock clock, final long period, final Runnable runnable) {
		LongActor actor = new LongActor() {
			private long elapsed = 0;

			@Override
			public void receive(long ticks) {
				elapsed += ticks;
				int invocations = (int) (elapsed / period);
				while (invocations-- > 0) {
					runnable.run();
				}
				elapsed %= period;
			}
		};
		clock.listen(actor);
		return actor;
	}

	/**
	 * Return a new {@link SystemClock} that, when invoked, will return elapsed
	 * time in milliseconds.
	 * <p>
	 * Remember that {@code SystemClock} objects need to be primed using
	 * {@link SystemClock#prime()}.
	 * 
	 * @return a {@link SystemClock} in milliseconds
	 */
	public static SystemClock millis() {
		return new SystemClock() {
			@Override
			protected long fetchTime() {
				return System.currentTimeMillis();
			}
		};
	}

	/**
	 * Return a new {@link SystemClock} that, when invoked, will return elapsed
	 * time in nanoseconds.
	 * <p>
	 * Remember that {@code SystemClock} objects need to be primed using
	 * {@link SystemClock#prime()}.
	 * 
	 * @return a {@link SystemClock} in nanoseconds
	 */
	public static SystemClock nanos() {
		return new SystemClock() {
			@Override
			protected long fetchTime() {
				return System.nanoTime();
			}
		};
	}
}
