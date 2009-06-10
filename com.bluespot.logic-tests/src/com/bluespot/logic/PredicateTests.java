package com.bluespot.logic;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.bluespot.logic.adapters.Adapter;
import com.bluespot.logic.predicates.AdaptingPredicate;
import com.bluespot.logic.predicates.EqualityPredicate;
import com.bluespot.logic.predicates.IdentityPredicate;
import com.bluespot.logic.predicates.InversePredicate;
import com.bluespot.logic.predicates.Predicate;
import com.bluespot.logic.predicates.UnanimousPredicate;
import com.bluespot.logic.predicates.builder.PredicateBuilder;

public class PredicateTests {

	final Adapter<String, Integer> adaptLength = new Adapter<String, Integer>() {

		public Integer adapt(final String source) {
			if (source == null) {
				return null;
			}
			return source.length();
		}

		@Override
		public String toString() {
			return "string-length";
		}
	};

	final Predicate<Integer> evenPredicate = new Predicate<Integer>() {
		public boolean test(final Integer value) {
			if (value == null) {
				return false;
			}
			return value.intValue() % 2 == 0;
		}
	};

	final Predicate<Integer> divisibleByThree = new Predicate<Integer>() {

		public boolean test(final Integer value) {
			if (value == null) {
				return false;
			}
			return value.intValue() % 3 == 0;
		}
	};

	@Test
	public void testEvenPredicate() {
		assertThat(this.evenPredicate.test(0), is(true));
		assertThat(this.evenPredicate.test(1), is(false));
		assertThat(this.evenPredicate.test(2), is(true));
		assertThat(this.evenPredicate.test(null), is(false));
	}

	@Test
	public void testDivisibleByThree() {
		assertThat(this.divisibleByThree.test(0), is(true));
		assertThat(this.divisibleByThree.test(1), is(false));
		assertThat(this.divisibleByThree.test(3), is(true));
		assertThat(this.divisibleByThree.test(null), is(false));
	}

	@Test
	public void testEqualityPredicate() {
		final EqualityPredicate<Integer> predicate = new EqualityPredicate<Integer>(42);
		assertThat(predicate.test(42), is(true));
		assertThat(predicate.test(43), is(false));
		assertThat(predicate.test(null), is(false));
	}

	@Test(expected = NullPointerException.class)
	public void testNPEOnNullEqualityPredicate() {
		new EqualityPredicate<Object>(null);
	}

	@Test
	public void testIdentityPredicate() {
		final List<Integer> list = Arrays.asList(1, 2, 3);

		final IdentityPredicate<List<Integer>> predicate = new IdentityPredicate<List<Integer>>(list);
		assertThat(Arrays.asList(1, 2, 3), is(Arrays.asList(1, 2, 3)));
		assertThat(predicate.test(Arrays.asList(1, 2, 3)), is(false));
		assertThat(predicate.test(list), is(true));
		assertThat(predicate.test(Arrays.asList(4, 5, 6)), is(false));
		assertThat(predicate.test(null), is(false));
	}

	@Test(expected = NullPointerException.class)
	public void testNPEOnNullIdentityPredicate() {
		new IdentityPredicate<Object>(null);
	}

	@Test
	public void testInversePredicate() {
		final InversePredicate<Integer> predicate = new InversePredicate<Integer>(this.evenPredicate);
		assertThat(predicate.test(1), is(true));
		assertThat(predicate.test(2), is(false));
		assertThat(predicate.test(null), is(true));
	}

	@Test(expected = NullPointerException.class)
	public void testNPEOnNullInversePredicate() {
		new InversePredicate<Object>(null);
	}

	@Test
	public void testNullPredicate() {
		final Predicate<Object> predicate = Predicates.nullValue();
		assertThat(predicate.test(new Object()), is(false));
		assertThat(predicate.test(null), is(true));
	}

	@Test
	public void testAdapter() {
		final AdaptingPredicate<String, Integer> predicate = new AdaptingPredicate<String, Integer>(this.adaptLength,
				this.evenPredicate);
		assertThat(predicate.test("C"), is(false));
		assertThat(predicate.test("BB"), is(true));
		assertThat(predicate.test("AAA"), is(false));
		assertThat(predicate.test(null), is(false));
		assertThat(predicate.test(""), is(true));
	}

	@Test
	public void testHas() {
		final PredicateBuilder<String> builder = new PredicateBuilder<String>();
		builder.has(this.adaptLength).that(Predicates.is(4));
		builder.is(Predicates.notNullValue());
		builder.anyOf(Predicates.is("FOUR"));

		final Predicate<? super String> predicate = builder.build();

		System.out.printf("Predicate: Value %s", predicate);

		assertThat(builder.build().test("FOUR"), is(true));
	}

	@Test
	public void testPredicateBuilder() {

		final PredicateBuilder<Integer> builder = new PredicateBuilder<Integer>();
		builder.anyOf(this.divisibleByThree).or(this.evenPredicate);
		builder.is(Predicates.not(Predicates.nullValue()));
		final Predicate<? super Integer> predicate = builder.build();

		assertThat(predicate.test(2), is(true));
		assertThat(predicate.test(3), is(true));
		assertThat(predicate.test(6), is(true));
		assertThat(predicate.test(0), is(true));
		assertThat(predicate.test(1), is(false));
		assertThat(predicate.test(null), is(false));
	}

	@Test
	public void testUnanimousPredicate() {

		final List<Predicate<? super Integer>> predicates = new ArrayList<Predicate<? super Integer>>();
		predicates.add(this.divisibleByThree);
		predicates.add(this.evenPredicate);

		final UnanimousPredicate<Integer> predicate = new UnanimousPredicate<Integer>(predicates);
		assertThat(predicate.test(2), is(false));
		assertThat(predicate.test(3), is(false));
		assertThat(predicate.test(6), is(true));
		assertThat(predicate.test(0), is(true));
		assertThat(predicate.test(null), is(false));
	}

	@Test
	public void testUnanimousPredicateDoesNotInterceptNull() {
		final List<Predicate<? super Integer>> predicates = new ArrayList<Predicate<? super Integer>>();
		predicates.add(Predicates.nullValue());
		predicates.add(Predicates.truth());
		final UnanimousPredicate<Integer> predicate = new UnanimousPredicate<Integer>(predicates);
		assertThat(predicate.test(null), is(true));
	}

	@Test(expected = NullPointerException.class)
	public void testUnanimousPredicateThrowsOnNullPredicate() {
		final List<Predicate<? super Integer>> predicates = new ArrayList<Predicate<? super Integer>>();
		predicates.add(Predicates.nullValue());
		predicates.add(Predicates.truth());
		predicates.add(null);
		new UnanimousPredicate<Integer>(predicates);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUnanimousPredicateThrowsOnEmptyPredicate() {
		new UnanimousPredicate<Integer>(Collections.<Predicate<? super Integer>> emptyList());
	}

	@Test
	public void testRegex() {
		assertThat(Predicates.matches("notime").test("notime"), is(true));
		assertThat(Predicates.matches("bad").test("notime"), is(false));
		assertThat(Predicates.matches("cheeses?").test("cheese"), is(true));
		assertThat(Predicates.matches("^a+$").test("aaaaa"), is(true));
	}
}
