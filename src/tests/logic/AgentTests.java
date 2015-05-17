package logic;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

import geom.algebra.NumericOperation;
import geom.algebra.NumericOperations;
import logic.agents.BruteForcingAgent;
import logic.agents.InputGenerator;
import logic.functions.Function;
import logic.functions.Functions;
import logic.functions.curryable.SafeMetaCurryable;

public class AgentTests {

	/**
	 * Create a simple pool that contains the necessities for deducing numeric
	 * functions.
	 * 
	 * @return a pool of values usable by {@link InputGenerator} objects
	 */
	private Set<Object> getPool() {
		Set<Object> values = new HashSet<Object>();
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
	public void testInputIteratorCanCreateALotOfInput() {
		InputGenerator<Number> iter = new InputGenerator<Number>(Number.class, getPool());
		for (int i = 0; i < 50; i++) {
			Assert.assertTrue(iter.hasNext());
			iter.next();
		}
	}

	/**
	 * {@link BruteForcingAgent} objects can construct (and thereby deduce)
	 * simple functions using currying. I use {@link Functions#divide(Number)}
	 * and other utility functions for most of the tests, but I've included a
	 * naive function just to prove that it's really working.
	 */
	@Ignore
	@Test
	public void testAgentDeducesSimpleFunctions() {
		BruteForcingAgent<Double, Number> agent = new BruteForcingAgent<Double, Number>(Double.class, getPool());
		Assert.assertNotNull(agent.apply(Functions.divide(2.0d)));
		Assert.assertNotNull(agent.apply(Functions.multiply(100.0d)));
		Assert.assertNotNull(agent.apply(new Function<Number, Number>() {

			@Override
			public Number apply(Number input) {
				if (input == null) {
					return null;
				}
				return input.doubleValue() + 25;
			}

		}));
	}

	/**
	 * This test isn't doable right now. We generate far too many results with
	 * composing enabled. I'm ignoring it since it never completes.
	 */
	@Ignore
	@Test
	public void testAgentDeducesComposedFunctions() {
		Set<Object> pool = getPool();
		pool.add(Functions.safeComposing());
		BruteForcingAgent<Double, Number> agent = new BruteForcingAgent<Double, Number>(Double.class, pool);

		Assert.assertNotNull(agent.apply(Functions.compose(Functions.add(3), Functions.multiply(2))));
		Assert.assertNotNull(agent.apply(new Function<Double, Number>() {
			@Override
			public Number apply(Double input) {
				if (input == null) {
					return null;
				}
				return 2 * input + 6;
			}
		}));
	}
}
