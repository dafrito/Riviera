package com.bluespot.geom;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public final class Point3DTests {

    @Test
    public void testPoint3DDouble() {
        final Point3D.Double point = new Point3D.Double(2.0d, 3.0d, 4.0d);
        assertThat(point.getX(), is(2.0d));
        assertThat(point.getY(), is(3.0d));
        assertThat(point.getZ(), is(4.0d));

        final Point3D.Double other = new Point3D.Double(2.0d, 3.0d, 4.0d);

        assertEquals(point, other);
        assertEquals(point.hashCode(), other.hashCode());
        assertThat(point, is(not(new Point3D.Double(1.0d, 3.0d, 4.0d))));
        assertThat(point, is(not(new Point3D.Double(2.0d, 1.0d, 4.0d))));
        assertThat(point, is(not(new Point3D.Double(2.0d, 3.0d, 1.0d))));

        // Ensure that this doesn't throw a format exception
        point.toString();
    }

    @Test
    public void testPoint3DFloat() {
        final Point3D.Float point = new Point3D.Float(2.0f, 3.0f, 4.0f);
        assertThat(point.getX(), is(2.0f));
        assertThat(point.getY(), is(3.0f));
        assertThat(point.getZ(), is(4.0f));

        final Point3D.Float other = new Point3D.Float(2.0f, 3.0f, 4.0f);

        assertEquals(point, other);
        assertEquals(point.hashCode(), other.hashCode());
        assertThat(point, is(not(new Point3D.Float(1.0f, 3.0f, 4.0f))));
        assertThat(point, is(not(new Point3D.Float(2.0f, 1.0f, 4.0f))));
        assertThat(point, is(not(new Point3D.Float(2.0f, 3.0f, 1.0f))));

        // Ensure that this doesn't throw a format exception
        point.toString();
    }

    @Test
    public void testPoint3DInteger() {
        final Point3D.Integer point = new Point3D.Integer(2, 3, 4);
        assertThat(point.getX(), is(2));
        assertThat(point.getY(), is(3));
        assertThat(point.getZ(), is(4));

        final Point3D.Integer other = new Point3D.Integer(2, 3, 4);

        assertEquals(point, other);
        assertEquals(point.hashCode(), other.hashCode());
        assertThat(point, is(not(new Point3D.Integer(1, 3, 4))));
        assertThat(point, is(not(new Point3D.Integer(2, 1, 4))));
        assertThat(point, is(not(new Point3D.Integer(2, 3, 1))));

        // Ensure that this doesn't throw a format exception
        point.toString();
    }
}
