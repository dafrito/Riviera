package com.bluespot.geom;

/**
 * Represents a single point in space. This interface provides {@code Point3D}
 * classes using {@link Double}, {@link Float}, or {@link Integer} precision.
 * 
 * @author Aaron Faanes
 * 
 */
public interface Point3D {

    /**
     * Represents a single point in space in {@code double} precision. Be aware
     * that while this class implements {@link #equals(Object)} appropriately,
     * it may yield unexpected results due to the inherent impreciseness of
     * floating-point values.
     * 
     * @author Aaron Faanes
     * 
     */
    public final static class Double implements Point3D {
        private final double z;
        private final double y;
        private final double x;

        /**
         * Constructs a point using the specified coordinates. There are no
         * restrictions on the values of these points except that none of them
         * can be {@code NaN}.
         * 
         * @param x
         *            the x-coordinate of this point
         * @param y
         *            the y-coordinate of this point
         * @param z
         *            the z-coordinate of this point
         * @throws IllegalArgumentException
         *             if any coordinate is {@code NaN}
         */
        public Double(final double x, final double y, final double z) {
            if (java.lang.Double.isNaN(x)) {
                throw new IllegalArgumentException("x is NaN");
            }
            if (java.lang.Double.isNaN(y)) {
                throw new IllegalArgumentException("y is NaN");
            }
            if (java.lang.Double.isNaN(z)) {
                throw new IllegalArgumentException("z is NaN");
            }
            this.x = x;
            this.y = y;
            this.z = z;
        }

        /**
         * Returns the x-coordinate of this point.
         * 
         * @return the x-coordinate of this point
         */
        public double getX() {
            return this.x;
        }

        /**
         * Returns the y-coordinate of this point.
         * 
         * @return the y-coordinate of this point
         */
        public double getY() {
            return this.y;
        }

        /**
         * Returns the z-coordinate of this point.
         * 
         * @return the z-coordinate of this point
         */
        public double getZ() {
            return this.z;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Point3D.Double)) {
                return false;
            }
            final Point3D.Double other = (Point3D.Double) obj;
            if (this.getX() != other.getX()) {
                return false;
            }
            if (this.getY() != other.getY()) {
                return false;
            }
            if (this.getZ() != other.getZ()) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int result = 13;
            final long xLong = java.lang.Double.doubleToLongBits(this.getX());
            final long yLong = java.lang.Double.doubleToLongBits(this.getY());
            final long zLong = java.lang.Double.doubleToLongBits(this.getZ());
            result = 31 * result + (int) (xLong ^ (xLong >>> 32));
            result = 31 * result + (int) (yLong ^ (yLong >>> 32));
            result = 31 * result + (int) (zLong ^ (zLong >>> 32));
            return result;
        }

        @Override
        public String toString() {
            return String.format("Point3D.Double[%f, %f, %f]", this.getX(), this.getY(), this.getZ());
        }

    }

    /**
     * Represents a single point in space in {@code float} precision. Be aware
     * that while this class implements {@link #equals(Object)} appropriately,
     * it may yield unexpected results due to the inherent impreciseness of
     * floating-point values.
     * 
     * @author Aaron Faanes
     * 
     */
    public final static class Float implements Point3D {

        /**
         * Represents a point at {@code (0, 0, 0)}.
         */
        public static final Float ORIGIN = new Float(0, 0, 0);

        private final float z;
        private final float y;
        private final float x;

        /**
         * Constructs a point using the specified coordinates. There are no
         * restrictions on the values of these points except that none of them
         * can be {@code NaN}.
         * 
         * @param x
         *            the x-coordinate of this point
         * @param y
         *            the y-coordinate of this point
         * @param z
         *            the z-coordinate of this point
         * @throws IllegalArgumentException
         *             if any coordinate is {@code NaN}
         */
        public Float(final float x, final float y, final float z) {
            if (java.lang.Float.isNaN(x)) {
                throw new IllegalArgumentException("x is NaN");
            }
            if (java.lang.Float.isNaN(y)) {
                throw new IllegalArgumentException("y is NaN");
            }
            if (java.lang.Float.isNaN(z)) {
                throw new IllegalArgumentException("z is NaN");
            }
            this.x = x;
            this.y = y;
            this.z = z;
        }

        /**
         * Returns the x-coordinate of this point.
         * 
         * @return the x-coordinate of this point
         */
        public float getX() {
            return this.x;
        }

        /**
         * Returns the y-coordinate of this point.
         * 
         * @return the y-coordinate of this point
         */
        public float getY() {
            return this.y;
        }

        /**
         * Returns the z-coordinate of this point.
         * 
         * @return the z-coordinate of this point
         */
        public float getZ() {
            return this.z;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Point3D.Float)) {
                return false;
            }
            final Point3D.Float other = (Point3D.Float) obj;
            if (this.getX() != other.getX()) {
                return false;
            }
            if (this.getY() != other.getY()) {
                return false;
            }
            if (this.getZ() != other.getZ()) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int result = 11;
            result = 31 * result + java.lang.Float.floatToIntBits(this.getX());
            result = 31 * result + java.lang.Float.floatToIntBits(this.getY());
            result = 31 * result + java.lang.Float.floatToIntBits(this.getZ());
            return result;
        }

        @Override
        public String toString() {
            return String.format("Point3D.Float[%f, %f, %f]", this.getX(), this.getY(), this.getZ());
        }

        /**
         * Creates and returns a new {@link Float Point3D.Float} that is this
         * point translated by the specified {@link Vector}.
         * 
         * @param vector
         *            the vector used to create the new point
         * @return a new point that is this point translated by the specified
         *         vector
         */
        public Float add(final Vector vector) {
            return new Point3D.Float(this.getX() + (float) vector.getX(), this.getY() + (float) vector.getY(),
                    this.getZ() + (float) vector.getZ());
        }

    }

    /**
     * Represents a single point in space in {@code int} precision.
     * 
     * @author Aaron Faanes
     * 
     */
    public final static class Integer implements Point3D {
        private final int z;
        private final int y;
        private final int x;

        /**
         * Constructs a point using the specified coordinates.
         * 
         * @param x
         *            the x-coordinate of this point
         * @param y
         *            the y-coordinate of this point
         * @param z
         *            the z-coordinate of this point
         * @throws IllegalArgumentException
         *             if any coordinate is {@code NaN}
         */
        public Integer(final int x, final int y, final int z) {
            if (java.lang.Float.isNaN(x)) {
                throw new IllegalArgumentException("x is NaN");
            }
            if (java.lang.Float.isNaN(y)) {
                throw new IllegalArgumentException("y is NaN");
            }
            if (java.lang.Float.isNaN(z)) {
                throw new IllegalArgumentException("z is NaN");
            }
            this.x = x;
            this.y = y;
            this.z = z;
        }

        /**
         * Returns the x-coordinate of this point.
         * 
         * @return the x-coordinate of this point
         */
        public int getX() {
            return this.x;
        }

        /**
         * Returns the y-coordinate of this point.
         * 
         * @return the y-coordinate of this point
         */
        public int getY() {
            return this.y;
        }

        /**
         * Returns the z-coordinate of this point.
         * 
         * @return the z-coordinate of this point
         */
        public int getZ() {
            return this.z;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Point3D.Integer)) {
                return false;
            }
            final Point3D.Integer other = (Point3D.Integer) obj;
            if (this.getX() != other.getX()) {
                return false;
            }
            if (this.getY() != other.getY()) {
                return false;
            }
            if (this.getZ() != other.getZ()) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int result = 17;
            result = 31 * result + this.getX();
            result = 31 * result + this.getY();
            result = 31 * result + this.getZ();
            return result;
        }

        @Override
        public String toString() {
            return String.format("Point3D.Integer[%d, %d, %d]", this.getX(), this.getY(), this.getZ());
        }

    }
}
