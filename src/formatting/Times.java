package formatting;

/**
 * A collection of {@link Unit} objects that assist when displaying and
 * converting between precisions of time.
 * 
 * @author Aaron Faanes
 * 
 */
public final class Times {

	/**
	 * A unit of precision.
	 * 
	 * @author Aaron Faanes
	 * 
	 */
	public static class Unit {
		private final String name;

		private final int magnitude;

		private final Unit largerUnit;

		/**
		 * Constructs a {@link Unit} using the specified name. The constructed
		 * unit's {@code magnitude} gives it context relative to another unit.
		 * Specifically, one unit of the specified {@code largerUnit} type is
		 * equal to {@code magnitude} number of these units.
		 * 
		 * @param name
		 *            the name of this unit
		 * @param magnitude
		 *            the number of units of this precision that are equal to
		 *            <em>one</em> unit of the {@code largerUnit} type
		 * @param largerUnit
		 *            the larger unit that is used to contextually describe this
		 *            unit
		 */
		public Unit(final String name, final int magnitude, final Unit largerUnit) {
			this.name = name;
			this.magnitude = magnitude;
			this.largerUnit = largerUnit;
		}

		/**
		 * Expresses the specified time. The specified time is in the precision
		 * of this unit; there are <em>n</em> units that the client wishes to
		 * express, where {@code n} is {@code time}. The time will be
		 * automatically converted such that it is represented with the closest
		 * unit available.
		 * <p>
		 * More rigorously, the time will be converted such that the
		 * <em>value</em> used to describe the time is as small as possible
		 * without being lower than one. The unit and time are both
		 * appropriately converted in order to make this true.
		 * 
		 * @param time
		 *            the time that will be expressed in the returned the
		 *            string.
		 * @return a string that expresses the specified time, according to the
		 *         rules specified above
		 */
		public String express(final double time) {
			if (time < 0) {
				return "-" + this.express(Math.abs(time));
			}
			if (this.largerUnit != null && time >= this.largerUnit.getMagnitude()) {
				return this.getLargerUnit().express(time / this.getLargerUnit().getMagnitude());
			}
			return String.format("%,4.3f %s", time, Math.abs(time) != 1 ? this.getPluralName() : this.getName());
		}

		/**
		 * Returns the magnitude of this {@code Unit}. The magnitude is the
		 * ratio of this unit to this unit's {@link #getLargerUnit() largerUnit}
		 * .
		 * 
		 * @return the magnitude of this {@code Unit}, relative to the larger
		 *         unit
		 */
		public int getMagnitude() {
			return this.magnitude;
		}

		/**
		 * Returns the human-readable plural name of this unit.
		 * 
		 * @return the human-readable plural name of this unit
		 */
		public String getPluralName() {
			return this.getName() + "s";
		}

		/**
		 * Returns the human-readable name of this unit.
		 * 
		 * @return the human-readable name of this unit
		 */
		public String getName() {
			return this.name;
		}

		/**
		 * Returns the larger unit that is used to define the relative "size" of
		 * this unit.
		 * 
		 * @return the larger unit that is used to contextually define this unit
		 */
		public Unit getLargerUnit() {
			return this.largerUnit;
		}

	}

	/**
	 * Represents a unit of time in days precision.
	 */
	public static final Unit DAYS = new Unit("day", 24, null);

	/**
	 * Represents a unit of time in hours precision.
	 */
	public static final Unit HOURS = new Unit("hour", 60, DAYS);

	/**
	 * Represents a unit of time in minutes precision.
	 */
	public static final Unit MINUTES = new Unit("minute", 60, HOURS);

	/**
	 * Represents a unit of time in seconds precision.
	 */
	public static final Unit SECONDS = new Unit("second", 1000, MINUTES);

	/**
	 * Represents a unit of time in milliseconds precision. A second contains
	 * one thousand milliseconds.
	 */
	public static final Unit MILLISECONDS = new Unit("millisecond", 1000, SECONDS);

	/**
	 * Represents a unit of time in microseconds precision. A second contains
	 * one million, or {@code 1e6}, microseconds.
	 */
	public static final Unit MICROSECONDS = new Unit("microsecond", 1000, MILLISECONDS);

	/**
	 * Represents a unit of time in nanoseconds precision. A second contains one
	 * billion, {@code 1e9}, nanoseconds.
	 */
	public static final Unit NANOSECONDS = new Unit("nanosecond", 1000, MICROSECONDS);
}
