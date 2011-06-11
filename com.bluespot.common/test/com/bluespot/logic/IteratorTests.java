/**
 * 
 */
package com.bluespot.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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

	@Test
	public void staggeredIteratorOperatesSuccessivelyOnUnderlyingIterators() throws Exception {
		Assert.assertEquals(
				Arrays.asList(0, 2, 3, 1, 1, 3, 2, 0, 3),
				Iterators.fill(
						Iterators.stagger(Iterators.range(0, 2)).add(
								Iterators.range(2, 0)).add(
								Iterators.constant(3)),
								9));
	}

	@Test
	public void iteratorsHasNextIsIdempotent() throws Exception {
		Iterator<Integer> iter = Iterators.chain(Iterators.count(0, 1, 3)).add(Iterators.count(3, -1, 0));
		List<Integer> nums = new ArrayList<Integer>();
		while (iter.hasNext()) {
			// Spurious call!
			iter.hasNext();
			nums.add(iter.next());
		}
		Assert.assertEquals(Arrays.asList(0, 1, 2, 3, 3, 2, 1, 0), nums);
	}
}
