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

/**
 * A clock that uses the system to determine elapsed time. This class does not
 * have a predetermined frequency: you must invoke {@link SystemClock#run()}
 * directly in order to pass time.
 * <p>
 * The following code will run the given clock at approximately 60 ticks a
 * second:
 * 
 * <pre>
 * SystemClock clock = Timing.millis();
 * ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
 * scheduler.scheduleAtFixedRate(clock, 0, 1000 / 60, TimeUnit.MILLISECONDS);
 * </pre>
 * 
 * It may seem unusual to create a clock that doesn't automatically run.
 * However, I think that separating time granularity from listener registration
 * leads to a cleaner design. For example, if we wanted to have our clock (and
 * subsequent listeners) run on the EDT, we could use Swing's timer class:
 * 
 * <pre>
 * SystemClock clock = Timing.millis();
 * new javax.swing.Timer(1000 / 60, Runnables.asActionListener(clock)).start();
 * </pre>
 * 
 * @author Aaron Faanes
 * @see Timing#nanos()
 * @see Timing#millis()
 */
public abstract class SystemClock extends AbstractClock implements Runnable {

	private long lastTime = -1;

	/**
	 * Prime this clock, such that the current time is used for the offset. The
	 * next invocation to {@link #run()} will use the time saved here to
	 * determine elapsed time.
	 * <p>
	 * It is not required to use {@link #prime()}. If it is not used, then the
	 * first invocation to {@link #run()} will prime the clock.
	 */
	public void prime() {
		lastTime = this.fetchTime();
	}

	@Override
	public void run() {
		if (lastTime == -1) {
			this.prime();
			return;
		}
		long current = this.fetchTime();
		long elapsed = current - lastTime;
		if (elapsed > 0) {
			lastTime = current;
			this.elapse(elapsed);
		}
	}

	/**
	 * Fetch the current time, according to this clock.
	 * 
	 * @return the current time
	 * @see System#nanoTime()
	 * @see System#currentTimeMillis()
	 */
	protected abstract long fetchTime();

}
