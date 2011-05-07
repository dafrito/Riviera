package com.bluespot.examples.graphics.boxfan;

/**
 * Represents some target of angular velocity. It offers a way to set its speed,
 * and, over time, will adjust its angle.
 * 
 * @author Aaron Faanes
 */
public final class Rotation {

	@SuppressWarnings("unused")
	private int speedDegrees;

	/**
	 * Sets the desired speed of rotation, in degrees.
	 * 
	 * @param speedDegrees
	 *            the desired rotation speed, in degrees
	 */
	public void setSpeed(final int speedDegrees) {
		this.speedDegrees = speedDegrees;
	}

	/**
	 * Returns the immediate angle. This has been adjusted by speed and varies
	 * over time.
	 * 
	 * @return the angle of the first fan blade
	 */
	public int getAngle() {
		return 0;
	}

}
