package com.bluespot.util.tests;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import com.bluespot.util.Ranges;

public class RangeTest {

	@Test
	public void testBiggerStep() {
		int count = 0;
		int num = 0;
		for (final int i : Ranges.range(0, 10, 3)) {
			count++;
			num = i;
		}
		Assert.assertThat(count, CoreMatchers.is(4));
		Assert.assertThat(num, CoreMatchers.is(9));
	}

	@Test
	public void testDecrementRange() {
		int count = 0;
		int num = 0;
		for (final int i : Ranges.range(10, 0)) {
			count++;
			num = i;
		}
		Assert.assertThat(count, CoreMatchers.is(10));
		Assert.assertThat(num, CoreMatchers.is(1));
	}

	@Test
	public void testDifferentStep() {
		int count = 0;
		int num = 0;
		for (final int i : Ranges.range(0, 10, 2)) {
			count++;
			num = i;
		}
		Assert.assertThat(count, CoreMatchers.is(5));
		Assert.assertThat(num, CoreMatchers.is(8));
	}

	@Test
	public void testIntegerRange() {
		int count = 0;
		int num = 0;
		for (final int i : Ranges.range(10)) {
			count++;
			num = i;
		}
		Assert.assertThat(count, CoreMatchers.is(10));
		Assert.assertThat(num, CoreMatchers.is(9));
	}
}
