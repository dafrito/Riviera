package com.bluespot.reflection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.bluespot.reflection.CallStackFrame;
import com.bluespot.reflection.CallStacks;

public class CallStacksTest {

	@Test
	public void testFramesAreEqual() {
		final CallStackFrame frame = CallStacks.getCurrentFrame();
		assertThat(frame, notNullValue());
		assertThat(frame, is(CallStacks.getCurrentFrame()));
	}

	@Test
	public void testMalformedFrame() {
		final CallStackFrame frame = new CallStackFrame("badclassname", "fooMethod");
		assertThat(frame, notNullValue());
		assertThat(frame.getPackageName(), is(""));
		assertThat(frame.getPackage(), nullValue());
	}
}
