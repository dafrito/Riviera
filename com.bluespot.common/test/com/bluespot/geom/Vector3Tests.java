package com.bluespot.geom;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.bluespot.geom.vectors.Vector3d;
import com.bluespot.geom.vectors.Vector3f;
import com.bluespot.geom.vectors.Vector3i;

public class Vector3Tests {

	@Test
	public void testCopyConstructorDouble() {
		final Vector3d dim = Vector3d.mutable(2, 3, 4);
		assertEquals(dim, dim.copy());
	}

	@Test
	public void testCopyConstructorFloat() {
		final Vector3f dim = Vector3f.mutable(2, 3, 4);
		assertEquals(dim, dim.copy());
	}

	@Test
	public void testCopyConstructorInteger() {
		final Vector3i dim = Vector3i.mutable(2, 3, 4);
		assertEquals(dim, dim.copy());
	}

	@Test
	public void testCubeConstructorDouble() {
		final Vector3d dim = Vector3d.mutable(2);
		assertEquals(dim, Vector3d.mutable(2, 2, 2));
	}

	@Test
	public void testCubeConstructorFloat() {
		final Vector3f dim = Vector3f.mutable(2);
		assertEquals(dim, Vector3f.mutable(2, 2, 2));
	}

	@Test
	public void testCubeConstructorInteger() {
		final Vector3i dim = Vector3i.mutable(2);
		assertEquals(dim, Vector3i.mutable(2, 2, 2));
	}

	@Test
	public void testDimension3d() {
		final Vector3d dim = Vector3d.mutable(2.0d, 3.0d, 4.0d);
		assertThat(dim.x(), is(2.0d));
		assertThat(dim.y(), is(3.0d));
		assertThat(dim.z(), is(4.0d));
		assertThat(dim.volume(), is(2.0d * 3.0d * 4.0d));

		final Vector3d other = Vector3d.mutable(2.0d, 3.0d, 4.0d);
		assertEquals(dim, other);
		assertThat(dim.hashCode(), is(other.hashCode()));
		assertThat(dim, is(not(Vector3d.mutable(1.0d, 3.0d, 4.0d))));
		assertThat(dim, is(not(Vector3d.mutable(2.0d, 1.0d, 4.0d))));
		assertThat(dim, is(not(Vector3d.mutable(2.0d, 3.0d, 1.0d))));

		// Ensure that this doesn't throw
		dim.toString();
	}

	@Test
	public void testDimension3f() {
		final Vector3f dim = Vector3f.mutable(2.0f, 3.0f, 4.0f);
		assertThat(dim.x(), is(2.0f));
		assertThat(dim.y(), is(3.0f));
		assertThat(dim.z(), is(4.0f));
		assertThat(dim.volume(), is(2.0f * 3.0f * 4.0f));

		final Vector3f other = Vector3f.mutable(2.0f, 3.0f, 4.0f);
		assertEquals(dim, other);
		assertThat(dim.hashCode(), is(other.hashCode()));
		assertThat(dim, is(not(Vector3f.mutable(1.0f, 3.0f, 4.0f))));
		assertThat(dim, is(not(Vector3f.mutable(2.0f, 1.0f, 4.0f))));
		assertThat(dim, is(not(Vector3f.mutable(2.0f, 3.0f, 1.0f))));

		// Ensure that this doesn't throw
		dim.toString();
	}

	@Test
	public void testDimension3DInteger() {
		final Vector3i dim = Vector3i.mutable(2, 3, 4);
		assertThat(dim.x(), is(2));
		assertThat(dim.y(), is(3));
		assertThat(dim.z(), is(4));
		assertThat(dim.volume(), is(2 * 3 * 4));

		final Vector3i other = Vector3i.mutable(2, 3, 4);
		assertEquals(dim, other);
		assertThat(dim.hashCode(), is(other.hashCode()));
		assertThat(dim, is(not(Vector3i.mutable(1, 3, 4))));
		assertThat(dim, is(not(Vector3i.mutable(2, 1, 4))));
		assertThat(dim, is(not(Vector3i.mutable(2, 3, 1))));

		// Ensure that this doesn't throw
		dim.toString();
	}
}
