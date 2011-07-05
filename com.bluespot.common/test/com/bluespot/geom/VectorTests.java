package com.bluespot.geom;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.bluespot.geom.vectors.Vector3d;
import com.bluespot.geom.vectors.Vector3f;
import com.bluespot.geom.vectors.Vector3i;

public final class VectorTests {

	@Test
	public void pointsCanBeMutable() {
		Vector3d.mutable(0d).set(Vector3d.frozen(0d, 1d, 0d));
	}

	@Test
	public void pointsSupportSetAndWith() throws Exception {
		assertTrue(Vector3d.mutable(1d).withX(4d).at(Vector3d.frozen(4d, 1d, 1d)));
		assertTrue(Vector3d.mutable(1d).withY(4d).at(Vector3d.frozen(1d, 4d, 1d)));
		assertTrue(Vector3d.mutable(1d).withZ(4d).at(Vector3d.frozen(1d, 1d, 4d)));

		assertTrue(Vector3f.mutable(1f).withX(4).at(Vector3f.frozen(4f, 1f, 1f)));
		assertTrue(Vector3f.mutable(1f).withY(4).at(Vector3f.frozen(1f, 4f, 1f)));
		assertTrue(Vector3f.mutable(1f).withZ(4).at(Vector3f.frozen(1f, 1f, 4f)));

		assertTrue(Vector3i.mutable(1).withX(4).at(Vector3i.frozen(4, 1, 1)));
		assertTrue(Vector3i.mutable(1).withY(4).at(Vector3i.frozen(1, 4, 1)));
		assertTrue(Vector3i.mutable(1).withZ(4).at(Vector3i.frozen(1, 1, 4)));
	}

	@Test
	public void pointsSupportSettingAxisValuesUsingWith() throws Exception {
		assertTrue(Vector3d.mutable(1d).with(Axis.XY, 4d).at(Vector3d.frozen(4d, 4d, 1d)));
		assertTrue(Vector3d.mutable(1d).with(Axis.XZ, 4d).at(Vector3d.frozen(4d, 1d, 4d)));
		assertTrue(Vector3d.mutable(1d).with(Axis.YZ, 4d).at(Vector3d.frozen(1d, 4d, 4d)));

		assertTrue(Vector3f.mutable(1f).with(Axis.XY, 4f).at(Vector3f.frozen(4f, 4f, 1f)));
		assertTrue(Vector3f.mutable(1f).with(Axis.XZ, 4f).at(Vector3f.frozen(4f, 1f, 4f)));
		assertTrue(Vector3f.mutable(1f).with(Axis.YZ, 4f).at(Vector3f.frozen(1f, 4f, 4f)));

		assertTrue(Vector3i.mutable(1).with(Axis.XY, 4).at(Vector3i.frozen(4, 4, 1)));
		assertTrue(Vector3i.mutable(1).with(Axis.XZ, 4).at(Vector3i.frozen(4, 1, 4)));
		assertTrue(Vector3i.mutable(1).with(Axis.YZ, 4).at(Vector3i.frozen(1, 4, 4)));
	}

	@Test
	public void pointsSupportCreatingAddedValues() throws Exception {
		assertTrue(Vector3d.mutable(0d).addedX(4d).at(Vector3d.frozen(4d, 0d, 0d)));
		assertTrue(Vector3d.mutable(0d).addedY(4d).at(Vector3d.frozen(0d, 4d, 0d)));
		assertTrue(Vector3d.mutable(0d).addedZ(4d).at(Vector3d.frozen(0d, 0d, 4d)));

		assertTrue(Vector3f.mutable(0f).addedX(4f).at(Vector3f.frozen(4f, 0f, 0f)));
		assertTrue(Vector3f.mutable(0f).addedY(4f).at(Vector3f.frozen(0f, 4f, 0f)));
		assertTrue(Vector3f.mutable(0f).addedZ(4f).at(Vector3f.frozen(0f, 0f, 4f)));

		assertTrue(Vector3i.mutable(0).addedX(4).at(Vector3i.frozen(4, 0, 0)));
		assertTrue(Vector3i.mutable(0).addedY(4).at(Vector3i.frozen(0, 4, 0)));
		assertTrue(Vector3i.mutable(0).addedZ(4).at(Vector3i.frozen(0, 0, 4)));
	}

	@Test
	public void pointsSupportAddingByAxis() throws Exception {
		assertTrue(Vector3d.mutable(0d).added(Axis.XZ, 4d).at(Vector3d.frozen(4d, 0d, 4d)));
		assertTrue(Vector3d.mutable(0d).added(Axis.XY, 4d).at(Vector3d.frozen(4d, 4d, 0d)));
		assertTrue(Vector3d.mutable(0d).added(Axis.YZ, 4d).at(Vector3d.frozen(0d, 4d, 4d)));

		assertTrue(Vector3f.mutable(0f).added(Axis.XZ, 4f).at(Vector3f.frozen(4f, 0f, 4f)));
		assertTrue(Vector3f.mutable(0f).added(Axis.XY, 4f).at(Vector3f.frozen(4f, 4f, 0f)));
		assertTrue(Vector3f.mutable(0f).added(Axis.YZ, 4f).at(Vector3f.frozen(0f, 4f, 4f)));

		assertTrue(Vector3i.mutable(0).added(Axis.XZ, 4).at(Vector3i.frozen(4, 0, 4)));
		assertTrue(Vector3i.mutable(0).added(Axis.XY, 4).at(Vector3i.frozen(4, 4, 0)));
		assertTrue(Vector3i.mutable(0).added(Axis.YZ, 4).at(Vector3i.frozen(0, 4, 4)));
	}

	@Test
	public void testVector3d() {
		final Vector3d point = Vector3d.mutable(2.0d, 3.0d, 4.0d);
		assertThat(point.x(), is(2.0d));
		assertThat(point.y(), is(3.0d));
		assertThat(point.z(), is(4.0d));

		final Vector3d other = Vector3d.mutable(2.0d, 3.0d, 4.0d);

		assertEquals(point, other);
		assertEquals(point.hashCode(), other.hashCode());
		assertThat(point, is(not(Vector3d.mutable(1.0d, 3.0d, 4.0d))));
		assertThat(point, is(not(Vector3d.mutable(2.0d, 1.0d, 4.0d))));
		assertThat(point, is(not(Vector3d.mutable(2.0d, 3.0d, 1.0d))));

		// Ensure that this doesn't throw a format exception
		point.toString();
	}

	@Test
	public void testVector3f() {
		final Vector3f point = Vector3f.mutable(2.0f, 3.0f, 4.0f);
		assertThat(point.x(), is(2.0f));
		assertThat(point.y(), is(3.0f));
		assertThat(point.z(), is(4.0f));

		final Vector3f other = Vector3f.mutable(2.0f, 3.0f, 4.0f);

		assertEquals(point, other);
		assertEquals(point.hashCode(), other.hashCode());
		assertThat(point, is(not(Vector3f.mutable(1.0f, 3.0f, 4.0f))));
		assertThat(point, is(not(Vector3f.mutable(2.0f, 1.0f, 4.0f))));
		assertThat(point, is(not(Vector3f.mutable(2.0f, 3.0f, 1.0f))));

		// Ensure that this doesn't throw a format exception
		point.toString();
	}

	@Test
	public void testVector3i() {
		final Vector3i point = Vector3i.mutable(2, 3, 4);
		assertThat(point.x(), is(2));
		assertThat(point.y(), is(3));
		assertThat(point.z(), is(4));

		final Vector3i other = Vector3i.mutable(2, 3, 4);

		assertEquals(point, other);
		assertEquals(point.hashCode(), other.hashCode());
		assertThat(point, is(not(Vector3i.mutable(1, 3, 4))));
		assertThat(point, is(not(Vector3i.mutable(2, 1, 4))));
		assertThat(point, is(not(Vector3i.mutable(2, 3, 1))));

		// Ensure that this doesn't throw a format exception
		point.toString();
	}
}
