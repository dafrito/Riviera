package com.bluespot.reflection.tests;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.bluespot.reflection.MutableCallStack;

public class CallStackTests {

	@Test
	public void testMostRecentFrameReturnsNull() {
		final MutableCallStack stack = new MutableCallStack();
		assertThat(stack.getMostRecentFrame(), nullValue());
	}
}
