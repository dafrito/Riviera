package com.bluespot.logic;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import com.bluespot.logic.agents.Agent;
import com.bluespot.logic.agents.InputGenerator;
import com.bluespot.logic.functions.Function;
import com.bluespot.logic.functions.Functions;

public class AgentTests {

	public Collection<? extends Function<Object, ?>> getFunctions() {
		Collection<Function<Object, ?>> functions = new ArrayList<Function<Object, ?>>();
		functions.add(Functions.identity());
		functions.add(Functions.value(0.0d));
		functions.add(Functions.cast(Number.class, Functions.add(1)));
		functions.add(Functions.cast(Number.class, Functions.subtract(1)));
		functions.add(Functions.cast(Number.class, Functions.divide(2)));
		functions.add(Functions.cast(Number.class, Functions.multiply(2)));
		return functions;
	}

	@Test
	public void testInputIterator() {
		InputGenerator<Number> iter = new InputGenerator<Number>(getFunctions(), Number.class);
		for (int i = 0; i < 10; i++) {
			if (!iter.hasNext()) {
				break;
			}
			iter.next();
		}
	}

	@Test
	public void testAgent() {
		Agent<Integer, Number> agent = new Agent<Integer, Number>(Integer.class, getFunctions());
		agent.apply(Functions.add(4));
	}
}
