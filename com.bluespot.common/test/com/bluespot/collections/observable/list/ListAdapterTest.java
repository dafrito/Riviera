package com.bluespot.collections.observable.list;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;

import javax.swing.event.ListDataListener;

import org.junit.Before;
import org.junit.Test;

import com.bluespot.collections.observable.Observables;
import com.bluespot.logic.adapters.Adapter;

/**
 * Tests {@link Observables#adapt(Adapter, ObservableList, List)}.
 * 
 * @author Aaron Faanes
 * 
 */
public class ListAdapterTest {

	private final Adapter<String, Integer> adapter = new Adapter<String, Integer>() {

		@Override
		public Integer adapt(final String source) {
			return source.length();
		}
	};

	private List<Integer> nums;
	private ObservableList<String> strings;

	@Before
	public void setUp() {
		this.strings = new ObservableList<String>();
		this.nums = new ArrayList<Integer>();
	}

	@Test
	public void testClear() {
		Observables.adapt(this.adapter, this.strings, this.nums);
		this.strings.addAll(Arrays.asList("A", "BB", "CCC"));
		this.strings.clear();
		assertTrue(this.nums.isEmpty());
	}

	@Test(expected = ConcurrentModificationException.class)
	public void testComodificationOnAdd() {
		Observables.adapt(this.adapter, this.strings, this.nums);
		this.nums.add(2);
		this.strings.add("No time");
	}

	@Test(expected = ConcurrentModificationException.class)
	public void testComodificationOnRemove() {
		this.strings.add("A");
		Observables.adapt(this.adapter, this.strings, this.nums);
		this.nums.clear();
		this.strings.add("No time");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIAEOnNonEmptyTargetList() {
		Observables.adapt(this.adapter, this.strings, new ArrayList<Integer>(Arrays.asList(1, 2, 3)));
	}

	@Test
	public void testImplicitAdd() {
		this.strings.addAll(Arrays.asList("A", "BB", "CCC"));
		Observables.adapt(this.adapter, this.strings, this.nums);
		assertThat(this.nums, is(Arrays.asList(1, 2, 3)));
	}

	@Test
	public void testListenersAreEqual() {
		final ListDataListener a = Observables.adapt(this.adapter, this.strings, this.nums);
		final ListDataListener b = Observables.adapt(this.adapter, this.strings, this.nums);
		assertThat(a, is(b));
	}

	@Test
	public void testListenersAreNotEqual() {
		final ListDataListener a = Observables.adapt(this.adapter, this.strings, this.nums);
		final ListDataListener b = Observables.adapt(this.adapter, new ObservableList<String>(), this.nums);
		assertThat(a, not(b));
	}

	@Test(expected = NullPointerException.class)
	public void testNPEOnNullAdapter() {
		Observables.adapt(null, this.strings, this.nums);
	}

	@Test(expected = NullPointerException.class)
	public void testNPEOnNullSource() {
		Observables.adapt(this.adapter, null, this.nums);
	}

	@Test(expected = NullPointerException.class)
	public void testNPEOnNullTargetList() {
		Observables.adapt(this.adapter, this.strings, null);
	}

	@Test
	public void testSet() {
		Observables.adapt(this.adapter, this.strings, this.nums);
		this.strings.add("A");
		assertThat(this.nums, is(Arrays.asList(1)));
		this.strings.set(0, "BB");
		assertThat(this.nums, is(Arrays.asList(2)));
	}

	@Test
	public void testSimpleAdd() {
		Observables.adapt(this.adapter, this.strings, this.nums);
		this.strings.add("A");
		this.strings.add("BB");
		this.strings.add("CCC");
		assertThat(this.nums, is(Arrays.asList(1, 2, 3)));
		this.strings.add("A");
		assertThat(this.nums, is(Arrays.asList(1, 2, 3, 1)));
	}

	@Test
	public void testSimpleAddAll() {
		Observables.adapt(this.adapter, this.strings, this.nums);
		this.strings.addAll(Arrays.asList("A", "B", "C"));
		assertThat(this.nums, is(Arrays.asList(1, 1, 1)));
	}

	@Test
	public void testSimpleRemoval() {
		Observables.adapt(this.adapter, this.strings, this.nums);
		this.strings.addAll(Arrays.asList("A", "BB", "CCC"));
		this.strings.remove("A");
		assertThat(this.nums, is(Arrays.asList(2, 3)));
	}
}
