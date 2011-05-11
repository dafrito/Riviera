package com.bluespot.logic;

import java.util.Collection;
import java.util.HashSet;

import junit.framework.Assert;

import org.junit.Test;

import com.bluespot.logic.agents.Agent;
import com.bluespot.logic.agents.InputGenerator;
import com.bluespot.logic.functions.Functions;
import com.bluespot.logic.functions.NumericOperation;
import com.bluespot.logic.functions.NumericOperations;
import com.bluespot.logic.functions.SafeMetaCurryable;

public class AgentTests {

	public Collection<Object> getPool() {
		Collection<Object> values = new HashSet<Object>();
		values.add(0.0d);
		values.add(1.0d);
		for (NumericOperation op : NumericOperations.values()) {
			values.add(Functions.protect(Number.class, op, Number.class));
		}
		values.add(Functions.identity());
		values.add(SafeMetaCurryable.newInstance());
		return values;
	}

	@Test
	public void testInputIterator() {
		InputGenerator<Number> iter = new InputGenerator<Number>(Number.class, getPool());
		for (int i = 0; i < 50; i++) {
			Assert.assertTrue(iter.hasNext());
			System.out.println(iter.next());
			//iter.next();
		}
	}

	@Test
	public void testAgent() {
		Agent<Integer, Number> agent = new Agent<Integer, Number>(Integer.class, getPool());
		//agent.apply(Functions.add(4));
	}
}
