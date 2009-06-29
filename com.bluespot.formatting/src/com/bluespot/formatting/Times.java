package com.bluespot.formatting;
public final class Times {

    public static class Unit {
        private final String name;

        private final int magnitude;

        private final Unit largerUnit;

        public Unit(final String name, final int magnitude, final Unit largerUnit) {
            this.name = name;
            this.magnitude = magnitude;
            this.largerUnit = largerUnit;
        }

        public String express(final double time) {
            if (time < 0) {
                return "-" + this.express(Math.abs(time));
            }
            if (this.largerUnit != null && time >= this.largerUnit.getMagnitude()) {
                return this.getLargerUnit().express(time / this.getLargerUnit().getMagnitude());
            }
            return String.format("%,4.3f %s", time, Math.abs(time) != 1 ? this.getPluralName() : this.getName());
        }

        public int getMagnitude() {
            return this.magnitude;
        }

        public String getPluralName() {
            return this.getName() + "s";
        }

        public String getName() {
            return this.name;
        }

        public Unit getLargerUnit() {
            return this.largerUnit;
        }

    }

    public static final Unit DAYS = new Unit("day", 24, null);
    public static final Unit HOURS = new Unit("hour", 60, DAYS);
    public static final Unit MINUTES = new Unit("minute", 60, HOURS);
    public static final Unit SECONDS = new Unit("second", 1000, MINUTES);
    public static final Unit MILLISECONDS = new Unit("millisecond", 1000, SECONDS);
    public static final Unit MICROSECONDS = new Unit("microsecond", 1000, MILLISECONDS);
    public static final Unit NANOSECONDS = new Unit("nanosecond", 1000, MICROSECONDS);
}
