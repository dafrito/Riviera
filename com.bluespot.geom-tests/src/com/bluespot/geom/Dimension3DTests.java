package com.bluespot.geom;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class Dimension3DTests {

    @Test
    public void testDimension3DDouble() {
        final Dimension3D.Double dim = new Dimension3D.Double(2, 3, 4);
        assertThat(dim.getWidth(), is(2.0d));
        assertThat(dim.getHeight(), is(3.0d));
        assertThat(dim.getDepth(), is(4.0d));
        assertThat(dim.getVolume(), is(2.0d * 3.0d * 4.0d));
        assertThat(dim.isEmpty(), is(false));

        final Dimension3D.Double other = new Dimension3D.Double(2, 3, 4);
        assertEquals(dim, other);
        assertThat(dim.hashCode(), is(other.hashCode()));

        // Ensure that this doesn't throw
        dim.toString();
    }

    @Test
    public void testDimension3DFloat() {
        final Dimension3D.Float dim = new Dimension3D.Float(2, 3, 4);
        assertThat(dim.getWidth(), is(2.0f));
        assertThat(dim.getHeight(), is(3.0f));
        assertThat(dim.getDepth(), is(4.0f));
        assertThat(dim.getVolume(), is(2.0f * 3.0f * 4.0f));
        assertThat(dim.isEmpty(), is(false));

        final Dimension3D.Float other = new Dimension3D.Float(2, 3, 4);
        assertEquals(dim, other);
        assertThat(dim.hashCode(), is(other.hashCode()));

        // Ensure that this doesn't throw
        dim.toString();
    }

    @Test
    public void testDimension3DInteger() {
        final Dimension3D.Integer dim = new Dimension3D.Integer(2, 3, 4);
        assertThat(dim.getWidth(), is(2));
        assertThat(dim.getHeight(), is(3));
        assertThat(dim.getDepth(), is(4));
        assertThat(dim.getVolume(), is(2 * 3 * 4));
        assertThat(dim.isEmpty(), is(false));

        final Dimension3D.Integer other = new Dimension3D.Integer(2, 3, 4);
        assertEquals(dim, other);
        assertThat(dim.hashCode(), is(other.hashCode()));

        // Ensure that this doesn't throw
        dim.toString();
    }
}
