package com.bluespot.logic;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.bluespot.logic.visitors.Sentinel;

public class SentinelTests {

	@Test
	public void testSentinel() {

		final List<String> strings = new ArrayList<String>();

		final Sentinel<String> sentinel = new Sentinel<String>(Predicates.lowerCase(), Visitors.addTo(strings));

		sentinel.accept("no time");
		sentinel.accept("NO TIME");

		assertThat(strings, is(Arrays.asList("no time")));
	}
}
