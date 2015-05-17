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
package physics;

import logic.actors.Actor;
import logic.actors.LongActor;
import timing.Clock;

/**
 * @author Aaron Faanes
 * 
 */
public class Motor {

	private final double maximum;
	private double power;
	private final Clock clock;

	public Motor(final Clock clock) {
		this(clock, Double.POSITIVE_INFINITY);
	}

	public Motor(final Clock clock, final double maxPower) {
		if (clock == null) {
			throw new NullPointerException("clock must not be null");
		}
		this.clock = clock;
		if (Double.isNaN(maxPower)) {
			throw new IllegalArgumentException("maximum output must not be NaN");
		}
		this.maximum = maxPower;
	}

	public double getMaximumPower() {
		return maximum;
	}

	public Clock getClock() {
		return clock;
	}

	public double getPower() {
		return power;
	}

	public void setPower(final double power) {
		if (Double.isNaN(power)) {
			throw new IllegalArgumentException("output must not be NaN");
		}
		this.power = Math.max(0, Math.min(power, maximum));
	}

	public void connect(final Actor<Double> target) {
		if (target == null) {
			throw new NullPointerException("target must not be null");
		}
		clock.listen(new LongActor() {
			@Override
			public void receive(long value) {
				target.receive(power * value);
			}
		});
	}
}
