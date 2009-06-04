package com.bluespot.reflection.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.bluespot.reflection.Reflection;
import com.bluespot.reflection.CallStack.Frame;

public class ReflectionTest {

	@Test
	public void testFramesAreEqual() {
		final Frame frame = Reflection.getCurrentFrame();
		assertThat(frame, notNullValue());
		assertThat(frame, is(Reflection.getCurrentFrame()));
	}

	@Test
	public void testMalformedFrame() {
		final Frame frame = new Frame("badclassname", "fooMethod");
		assertThat(frame, notNullValue());
		assertThat(frame.getPackageName(), is(""));
		assertThat(frame.getPackage(), nullValue());
	}
}
