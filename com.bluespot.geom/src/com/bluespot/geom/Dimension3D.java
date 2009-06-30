package com.bluespot.geom;

/**
 * Represents an immutable, three-dimensional volume of space.
 * 
 * @author Aaron Faanes
 * 
 */
public interface Dimension3D {

    /**
     * Represents a three-dimensional volume of space in double precision.
     * <p>
     * This implements {@link Object#equals(Object)} appropriately, but be aware
     * that floating-point values are by nature imprecise and dimensions that
     * are not exactly equal will not be in practice.
     * 
     * @author Aaron Faanes
     */
    public class Double implements Dimension3D {
        private final double width;
        private final double height;
        private final double depth;

        /**
         * Constructs a new three-dimensional volume using the specified values.
         * No restrictions are made on these values except that they cannot be
         * {@code NaN}.
         * 
         * @param width
         *            the width of this dimension
         * @param height
         *            the height of this dimension
         * @param depth
         *            the depth of this dimension
         * @throws IllegalArgumentException
         *             if any argument is {@code NaN}.
         */
        public Double(final double width, final double height, final double depth) {
            super();
            if (java.lang.Double.isNaN(width)) {
                throw new IllegalArgumentException("width is NaN");
            }
            if (java.lang.Double.isNaN(height)) {
                throw new IllegalArgumentException("height is NaN");
            }
            if (java.lang.Double.isNaN(depth)) {
                throw new IllegalArgumentException("depth is NaN");
            }
            this.width = width;
            this.height = height;
            this.depth = depth;
        }

        /**
         * Returns the width of this dimension.
         * 
         * @return the width of this dimension
         */
        public double getWidth() {
            return this.width;
        }

        /**
         * Returns the height of this dimension.
         * 
         * @return the height of this dimension
         */
        public double getHeight() {
            return this.height;
        }

        /**
         * Returns the depth of this dimension.
         * 
         * @return the depth of this dimension
         */
        public double getDepth() {
            return this.depth;
        }

        /**
         * Returns the volume of the space contained by this dimension. Note
         * that this will always return a positive value.
         * 
         * @return the volume of the space contained by this dimension.
         */
        public double getVolume() {
            return Math.abs(this.width * this.height * this.depth);
        }

        /**
         * Returns whether this dimension is empty. This does not necessarily
         * mean all of its dimensions are zero; it only means that one dimension
         * is zero.
         * 
         * @return whether one of the dimensions of this dimension is zero
         */
        public boolean isEmpty() {
            return this.width == 0 || this.height == 0 || this.depth == 0;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Dimension3D.Double)) {
                return false;
            }
            final Dimension3D.Double other = (Dimension3D.Double) obj;
            if (this.getWidth() != other.getWidth()) {
                return false;
            }
            if (this.getHeight() != other.getHeight()) {
                return false;
            }
            if (this.getDepth() != other.getDepth()) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int result = 13;
            final long widthLong = java.lang.Double.doubleToLongBits(this.getWidth());
            final long heightLong = java.lang.Double.doubleToLongBits(this.getHeight());
            final long depthLong = java.lang.Double.doubleToLongBits(this.getDepth());
            result = 31 * result + (int) (widthLong ^ (widthLong >>> 32));
            result = 31 * result + (int) (heightLong ^ (heightLong >>> 32));
            result = 31 * result + (int) (depthLong ^ (depthLong >>> 32));
            return result;
        }

        @Override
        public String toString() {
            return String.format("Dimension3D.Double[%f, %f, %f]", this.getWidth(), this.getHeight(), this.getDepth());
        }
    }

    /**
     * Represents a three-dimensional volume of space in float precision.
     * <p>
     * This implements {@link Object#equals(Object)} appropriately, but be aware
     * that floating-point values are by nature imprecise and dimensions that
     * are not exactly equal will not be in practice.
     * 
     * @author Aaron Faanes
     */
    public class Float implements Dimension3D {
        private final float width;
        private final float height;
        private final float depth;

        /**
         * Constructs a new three-dimensional volume using the specified values.
         * No restrictions are made on these values except that they cannot be
         * {@code NaN}.
         * 
         * @param width
         *            the width of this dimension
         * @param height
         *            the height of this dimension
         * @param depth
         *            the depth of this dimension
         * @throws IllegalArgumentException
         *             if any argument is {@code NaN}.
         */
        public Float(final float width, final float height, final float depth) {
            if (java.lang.Float.isNaN(width)) {
                throw new IllegalArgumentException("width is NaN");
            }
            if (java.lang.Float.isNaN(height)) {
                throw new IllegalArgumentException("height is NaN");
            }
            if (java.lang.Float.isNaN(depth)) {
                throw new IllegalArgumentException("depth is NaN");
            }
            this.width = width;
            this.height = height;
            this.depth = depth;
        }

        /**
         * Returns the width of this dimension.
         * 
         * @return the width of this dimension
         */
        public float getWidth() {
            return this.width;
        }

        /**
         * Returns the height of this dimension.
         * 
         * @return the height of this dimension
         */
        public float getHeight() {
            return this.height;
        }

        /**
         * Returns the depth of this dimension.
         * 
         * @return the depth of this dimension
         */
        public float getDepth() {
            return this.depth;
        }

        /**
         * Returns the volume of the space contained by this dimension. Note
         * that this will always return a positive value.
         * 
         * @return the volume of the space contained by this dimension.
         */
        public float getVolume() {
            return Math.abs(this.width * this.height * this.depth);
        }

        /**
         * Returns whether this dimension is empty. This does not necessarily
         * mean all of its dimensions are zero; it only means that one dimension
         * is zero.
         * 
         * @return whether one of the dimensions of this dimension is zero
         */
        public boolean isEmpty() {
            return this.width == 0 || this.height == 0 || this.depth == 0;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Dimension3D.Float)) {
                return false;
            }
            final Dimension3D.Float other = (Dimension3D.Float) obj;
            if (this.getWidth() != other.getWidth()) {
                return false;
            }
            if (this.getHeight() != other.getHeight()) {
                return false;
            }
            if (this.getDepth() != other.getDepth()) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int result = 17;
            result = 31 * result + java.lang.Float.floatToIntBits(this.getWidth());
            result = 31 * result + java.lang.Float.floatToIntBits(this.getHeight());
            result = 31 * result + java.lang.Float.floatToIntBits(this.getDepth());
            return result;
        }

        @Override
        public String toString() {
            return String.format("Dimension3D.Float[%f, %f, %f]", this.getWidth(), this.getHeight(), this.getDepth());
        }
    }

    /**
     * Represents a three-dimensional volume of space in integer precision.
     * 
     * @author Aaron Faanes
     */
    public class Integer implements Dimension3D {
        private final int width;
        private final int height;
        private final int depth;

        /**
         * Constructs a new three-dimensional volume using the specified values.
         * 
         * @param width
         *            the width of this dimension
         * @param height
         *            the height of this dimension
         * @param depth
         *            the depth of this dimension
         * @throws IllegalArgumentException
         *             if any argument is {@code NaN}.
         */
        public Integer(final int width, final int height, final int depth) {
            this.width = width;
            this.height = height;
            this.depth = depth;
        }

        /**
         * Returns the width of this dimension.
         * 
         * @return the width of this dimension
         */
        public int getWidth() {
            return this.width;
        }

        /**
         * Returns the height of this dimension.
         * 
         * @return the height of this dimension
         */
        public int getHeight() {
            return this.height;
        }

        /**
         * Returns the depth of this dimension.
         * 
         * @return the depth of this dimension
         */
        public int getDepth() {
            return this.depth;
        }

        /**
         * Returns the volume of the space contained by this dimension. Note
         * that this will always return a positive value.
         * 
         * @return the volume of the space contained by this dimension.
         */
        public int getVolume() {
            return Math.abs(this.width * this.height * this.depth);
        }

        /**
         * Returns whether this dimension is empty. This does not necessarily
         * mean all of its dimensions are zero; it only means that one dimension
         * is zero.
         * 
         * @return whether one of the dimensions of this dimension is zero
         */
        public boolean isEmpty() {
            return this.width == 0 || this.height == 0 || this.depth == 0;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Dimension3D.Integer)) {
                return false;
            }
            final Dimension3D.Integer other = (Dimension3D.Integer) obj;
            if (this.getWidth() != other.getWidth()) {
                return false;
            }
            if (this.getHeight() != other.getHeight()) {
                return false;
            }
            if (this.getDepth() != other.getDepth()) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int result = 11;
            result = 31 * result + this.getWidth();
            result = 31 * result + this.getHeight();
            result = 31 * result + this.getDepth();
            return result;
        }

        @Override
        public String toString() {
            return String.format("Dimension3D.Integer[%d, %d, %d]", this.getWidth(), this.getHeight(), this.getDepth());
        }
    }

}
