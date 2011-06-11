/**
 * 
 */
package com.bluespot.logic;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

import com.bluespot.logic.iterators.Iterators;

/**
 * Test {@link Iterators} and its ilk.
 * 
 * @author Aaron Faanes
 * 
 */
public class IteratorTests {

	@Test
	public void iteratorsCanMakeCrazyIterators() throws Exception {
		Assert.assertEquals(
				Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
				Iterators.fill(Iterators.range(1, 10))
				);
	}

	@Test
	public void iteratorsSupportLimitedRemoval() {
		Assert.assertEquals(
				Arrays.asList(0, 1, 1, 2, 3, 5, 8, 13, 21, 34),
				Iterators.fill(Iterators.fibonacci(), 10)
				);
	}
}
