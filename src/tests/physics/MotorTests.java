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

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Test;

import logic.actors.Actor;
import timing.MutableClock;

/**
 * @author Aaron Faanes
 * 
 */
public class MotorTests {

	private Motor newMotor() {
		return new Motor(new MutableClock());
	}

	private Motor newMotor(double power) {
		return new Motor(new MutableClock(), power);
	}

	/**
	 * Presumably, a physics system would provide motors to ensure the maximum
	 * output is reasonable, but motors can also be created in a headless
	 * fashion. Of course, there should be some constraint that disallows
	 * introducing these headless motors into a preexisting physics system.
	 */
	@Test
	public void motorsCanBeHeadless() {
		newMotor();
	}

	/**
	 * This seems like the natural thing to do, but I don't really like having
	 * positive infinity introduced so carelessly.
	 */
	@Test
	public void motorsDefaultToInfiniteMaximumOutput() {
		Motor motor = new Motor(new MutableClock());
		assertEquals(Double.POSITIVE_INFINITY, motor.getMaximumPower(), 0d);
	}

	@Test
	public void motorsRefuseToExceedTheirMaximumOutput() {
		Motor motor = newMotor(40);
		assertEquals(40d, motor.getMaximumPower(), 1e-3);
		motor.setPower(80);
		assertEquals(40d, motor.getPower(), 1e-3);
	}

	@Test
	public void motorsCanBeTurnedOff() {
		Motor motor = newMotor(40);
		motor.setPower(80);
		motor.setPower(0);
		assertEquals(0d, motor.getPower(), 1e-3);
	}

	@Test
	public void motorOutputCannotBeNegative() {
		Motor motor = newMotor(40);
		motor.setPower(40);
		motor.setPower(-10);
		assertEquals(0d, motor.getPower(), 1e-3);
	}

	@Test(expected = IllegalArgumentException.class)
	public void motorsCrashOnNaN() {
		newMotor(Double.NaN);
	}

	@Test(expected = IllegalArgumentException.class)
	public void motorOutputCannotBeNaN() {
		Motor motor = newMotor(10);
		motor.setPower(Double.NaN);
	}

	@Test
	public void motorsCanOnlyPerformWorkAfterTimeDuration() throws Exception {
		final MutableClock clock = new MutableClock();
		final Motor motor = new Motor(clock, 40);
		motor.connect(new Actor<Double>() {
			@Override
			public void receive(Double value) {
				Assert.assertEquals(motor.getPower() * clock.getElapsed(), value);
			}
		});
		clock.elapse(10);
	}
}
