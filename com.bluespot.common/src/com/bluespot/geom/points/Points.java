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

	public static Point3d mutable(double v) {
		return Point3d.mutable(v, v, v);
	}

	public static Point3d mutable(double x, double y, double z) {
		return Point3d.mutable(x, y, z);
	}

	public static Point3d frozen(double v) {
		return Point3d.frozen(v, v, v);
	}

	public static Point3d frozen(double x, double y, double z) {
		return Point3d.frozen(x, y, z);
	}

	public static Point3d mutable(Point3d point) {
		return Point3d.mutable(point);
	}

	public static Point3d frozen(Point3d point) {
		return Point3d.frozen(point);
	}

	public static Point3f mutable(float x, float y, float z) {
		return Point3f.mutable(x, y, z);
	}

	public static Point3f mutable(float v) {
		return Point3f.mutable(v, v, v);
	}

	public static Point3f frozen(float x, float y, float z) {
		return Point3f.frozen(x, y, z);
	}

	public static Point3f frozen(float v) {
		return Point3f.frozen(v, v, v);
	}

	public static Point3f mutable(Point3f point) {
		return Point3f.mutable(point);
	}

	public static Point3f frozen(Point3f point) {
		return Point3f.frozen(point);
	}

	public static Point3i mutable(int x, int y, int z) {
		return Point3i.mutable(x, y, z);
	}

	public static Point3i mutable(int v) {
		return Point3i.mutable(v, v, v);
	}

	public static Point3i frozen(int x, int y, int z) {
		return Point3i.frozen(x, y, z);
	}

	public static Point3i frozen(int v) {
		return Point3i.frozen(v, v, v);
	}

	public static Point3i mutable(Point3i point) {
		return Point3i.mutable(point);
	}

	public static Point3i frozen(Point3i point) {
		return Point3i.frozen(point);
	}

}
