/**
 * 
 */
package com.bluespot.geom.points;

/**
 * A collection of utility methods working with points.
 * 
 * @author Aaron Faanes
 * 
 */
public final class Points {

	private Points() {
		throw new AssertionError("Instantiation is not allowed");
	}

	public static DoublePoint3D mutable(double x, double y, double z) {
		return DoublePoint3D.mutable(x, y, z);
	}

	public static DoublePoint3D frozen(double x, double y, double z) {
		return DoublePoint3D.frozen(x, y, z);
	}

	public static DoublePoint3D mutable(DoublePoint3D point) {
		return DoublePoint3D.mutable(point);
	}

	public static DoublePoint3D frozen(DoublePoint3D point) {
		return DoublePoint3D.frozen(point);
	}

	public static FloatPoint3D mutable(float x, float y, float z) {
		return FloatPoint3D.mutable(x, y, z);
	}

	public static FloatPoint3D frozen(float x, float y, float z) {
		return FloatPoint3D.frozen(x, y, z);
	}

	public static FloatPoint3D mutable(FloatPoint3D point) {
		return FloatPoint3D.mutable(point);
	}

	public static FloatPoint3D frozen(FloatPoint3D point) {
		return FloatPoint3D.frozen(point);
	}

	public static IntegerPoint3D mutable(int x, int y, int z) {
		return IntegerPoint3D.mutable(x, y, z);
	}

	public static IntegerPoint3D frozen(int x, int y, int z) {
		return IntegerPoint3D.frozen(x, y, z);
	}

	public static IntegerPoint3D mutable(IntegerPoint3D point) {
		return IntegerPoint3D.mutable(point);
	}

	public static IntegerPoint3D frozen(IntegerPoint3D point) {
		return IntegerPoint3D.frozen(point);
	}

}
