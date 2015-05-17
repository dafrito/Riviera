package geom;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import geom.vectors.Vector3d;
import geom.vectors.Vector3f;
import geom.vectors.Vector3i;

public final class VectorTests {

	@Test
	public void pointsCanBeMutable() {
		Vector3d.mutable(0d).set(Vector3d.frozen(0d, 1d, 0d));
	}

	@Test
	public void pointsSupportSettingAxisValuesUsingWith() throws Exception {
		assertTrue(Vector3d.mutable(1d).set(Axis.XY, 4d).at(Vector3d.frozen(4d, 4d, 1d)));
		assertTrue(Vector3d.mutable(1d).set(Axis.XZ, 4d).at(Vector3d.frozen(4d, 1d, 4d)));
		assertTrue(Vector3d.mutable(1d).set(Axis.YZ, 4d).at(Vector3d.frozen(1d, 4d, 4d)));

		assertTrue(Vector3f.mutable(1f).set(Axis.XY, 4f).at(Vector3f.frozen(4f, 4f, 1f)));
		assertTrue(Vector3f.mutable(1f).set(Axis.XZ, 4f).at(Vector3f.frozen(4f, 1f, 4f)));
		assertTrue(Vector3f.mutable(1f).set(Axis.YZ, 4f).at(Vector3f.frozen(1f, 4f, 4f)));

		assertTrue(Vector3i.mutable(1).set(Axis.XY, 4).at(Vector3i.frozen(4, 4, 1)));
		assertTrue(Vector3i.mutable(1).set(Axis.XZ, 4).at(Vector3i.frozen(4, 1, 4)));
		assertTrue(Vector3i.mutable(1).set(Axis.YZ, 4).at(Vector3i.frozen(1, 4, 4)));
	}

	@Test
	public void pointsSupportAddingByAxis() throws Exception {
		assertTrue(Vector3d.mutable(0d).add(Axis.XZ, 4d).at(Vector3d.frozen(4d, 0d, 4d)));
		assertTrue(Vector3d.mutable(0d).add(Axis.XY, 4d).at(Vector3d.frozen(4d, 4d, 0d)));
		assertTrue(Vector3d.mutable(0d).add(Axis.YZ, 4d).at(Vector3d.frozen(0d, 4d, 4d)));

		assertTrue(Vector3f.mutable(0f).add(Axis.XZ, 4f).at(Vector3f.frozen(4f, 0f, 4f)));
		assertTrue(Vector3f.mutable(0f).add(Axis.XY, 4f).at(Vector3f.frozen(4f, 4f, 0f)));
		assertTrue(Vector3f.mutable(0f).add(Axis.YZ, 4f).at(Vector3f.frozen(0f, 4f, 4f)));

		assertTrue(Vector3i.mutable(0).add(Axis.XZ, 4).at(Vector3i.frozen(4, 0, 4)));
		assertTrue(Vector3i.mutable(0).add(Axis.XY, 4).at(Vector3i.frozen(4, 4, 0)));
		assertTrue(Vector3i.mutable(0).add(Axis.YZ, 4).at(Vector3i.frozen(0, 4, 4)));
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
