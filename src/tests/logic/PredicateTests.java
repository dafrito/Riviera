package logic;

import static logic.adapters.Adapters.childFile;
import static logic.adapters.Adapters.fileName;
import static logic.predicates.Predicates.not;
import static logic.predicates.Predicates.startsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import logic.adapters.Adapter;
import logic.predicates.AdaptingPredicate;
import logic.predicates.EqualityPredicate;
import logic.predicates.IdentityPredicate;
import logic.predicates.InversePredicate;
import logic.predicates.Predicate;
import logic.predicates.Predicates;
import logic.predicates.UnanimousPredicate;
import logic.predicates.builder.PredicateBuilder;

public class PredicateTests {

	final Adapter<String, Integer> adaptLength = new Adapter<String, Integer>() {

		@Override
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
		@Override
		public boolean test(final Integer value) {
			if (value == null) {
				return false;
			}
			return value.intValue() % 2 == 0;
		}
	};

	final Predicate<Integer> divisibleByThree = new Predicate<Integer>() {

		@Override
		public boolean test(final Integer value) {
			if (value == null) {
				return false;
			}
			return value.intValue() % 3 == 0;
		}
	};

	@Test
	public void testEquality() {
		final PredicateBuilder<File> predicate = new PredicateBuilder<File>();
		predicate.has(fileName()).that(not(startsWith(".")));
		predicate.has(childFile("src"));
		System.out.println(predicate.build());
		assertTrue(predicate.build().equals(predicate.build()));
	}

	@Test
	public void testHasCanStillCheckForNullValues() {
		final PredicateBuilder<String> builder = new PredicateBuilder<String>();
		builder.has(this.adaptLength).addRequirement(Predicates.nullValue());
		assertThat(builder.build().test(null), is(true));
	}

	@Test
	public void testHasImplicitlyMeansNotNull() {
		final PredicateBuilder<String> builder = new PredicateBuilder<String>();
		builder.has(this.adaptLength);
		assertThat(builder.build().test(null), is(false));
	}

	@Test
	public void testLowerCase() {
		assertThat(Predicates.lowerCase().test("no time"), is(true));
		assertThat(Predicates.lowerCase().test("No Time"), is(false));
		assertThat(Predicates.lowerCase().test("No TiMe"), is(false));
		assertThat(Predicates.lowerCase().test("   "), is(true));
		assertThat(Predicates.lowerCase().test(""), is(true));
		assertThat(Predicates.lowerCase().test(null), is(false));
	}

	@Test
	public void testUpperCase() {
		assertThat(Predicates.upperCase().test("NO TIME"), is(true));
		assertThat(Predicates.upperCase().test("no time"), is(false));
		assertThat(Predicates.upperCase().test("No TiMe"), is(false));
		assertThat(Predicates.upperCase().test("   "), is(true));
		assertThat(Predicates.upperCase().test(""), is(true));
		assertThat(Predicates.upperCase().test(null), is(false));
	}

	@Test
	public void testEvenPredicate() {
		Assert.assertThat(this.evenPredicate.test(0), CoreMatchers.is(true));
		Assert.assertThat(this.evenPredicate.test(1), CoreMatchers.is(false));
		Assert.assertThat(this.evenPredicate.test(2), CoreMatchers.is(true));
		Assert.assertThat(this.evenPredicate.test(null), CoreMatchers.is(false));
	}

	@Test
	public void testDivisibleByThree() {
		Assert.assertThat(this.divisibleByThree.test(0), CoreMatchers.is(true));
		Assert.assertThat(this.divisibleByThree.test(1), CoreMatchers.is(false));
		Assert.assertThat(this.divisibleByThree.test(3), CoreMatchers.is(true));
		Assert.assertThat(this.divisibleByThree.test(null), CoreMatchers.is(false));
	}

	@Test
	public void testEqualityPredicate() {
		final EqualityPredicate<Integer> predicate = new EqualityPredicate<Integer>(42);
		Assert.assertThat(predicate.test(42), CoreMatchers.is(true));
		Assert.assertThat(predicate.test(43), CoreMatchers.is(false));
		Assert.assertThat(predicate.test(null), CoreMatchers.is(false));
	}

	@Test(expected = NullPointerException.class)
	public void testNPEOnNullEqualityPredicate() {
		new EqualityPredicate<Object>(null);
	}

	@Test
	public void testIdentityPredicate() {
		final List<Integer> list = Arrays.asList(1, 2, 3);

		final IdentityPredicate<List<Integer>> predicate = new IdentityPredicate<List<Integer>>(list);
		Assert.assertThat(Arrays.asList(1, 2, 3), CoreMatchers.is(Arrays.asList(1, 2, 3)));
		Assert.assertThat(predicate.test(Arrays.asList(1, 2, 3)), CoreMatchers.is(false));
		Assert.assertThat(predicate.test(list), CoreMatchers.is(true));
		Assert.assertThat(predicate.test(Arrays.asList(4, 5, 6)), CoreMatchers.is(false));
		Assert.assertThat(predicate.test(null), CoreMatchers.is(false));
	}

	@Test(expected = NullPointerException.class)
	public void testNPEOnNullIdentityPredicate() {
		new IdentityPredicate<Object>(null);
	}

	@Test
	public void testInversePredicate() {
		final InversePredicate<Integer> predicate = new InversePredicate<Integer>(this.evenPredicate);
		Assert.assertThat(predicate.test(1), CoreMatchers.is(true));
		Assert.assertThat(predicate.test(2), CoreMatchers.is(false));
		Assert.assertThat(predicate.test(null), CoreMatchers.is(true));
	}

	@Test(expected = NullPointerException.class)
	public void testNPEOnNullInversePredicate() {
		new InversePredicate<Object>(null);
	}

	@Test
	public void testNullPredicate() {
		final Predicate<Object> predicate = Predicates.nullValue();
		Assert.assertThat(predicate.test(new Object()), CoreMatchers.is(false));
		Assert.assertThat(predicate.test(null), CoreMatchers.is(true));
	}

	@Test
	public void testAdapter() {
		final AdaptingPredicate<String, Integer> predicate = new AdaptingPredicate<String, Integer>(this.adaptLength,
				this.evenPredicate);
		Assert.assertThat(predicate.test("C"), CoreMatchers.is(false));
		Assert.assertThat(predicate.test("BB"), CoreMatchers.is(true));
		Assert.assertThat(predicate.test("AAA"), CoreMatchers.is(false));
		Assert.assertThat(predicate.test(null), CoreMatchers.is(false));
		Assert.assertThat(predicate.test(""), CoreMatchers.is(true));
	}

	@Test
	public void testHas() {
		final PredicateBuilder<String> builder = new PredicateBuilder<String>();
		builder.has(this.adaptLength).that(Predicates.is(4));
		builder.is(Predicates.notNullValue());
		builder.anyOf(Predicates.is("FOUR"));

		final Predicate<? super String> predicate = builder.build();

		System.out.printf("Predicate: Value %s", predicate);

		Assert.assertThat(builder.build().test("FOUR"), CoreMatchers.is(true));
	}

	@Test
	public void testPredicateBuilder() {

		final PredicateBuilder<Integer> builder = new PredicateBuilder<Integer>();
		builder.anyOf(this.divisibleByThree).or(this.evenPredicate);
		builder.is(Predicates.not(Predicates.nullValue()));
		final Predicate<? super Integer> predicate = builder.build();

		Assert.assertThat(predicate.test(2), CoreMatchers.is(true));
		Assert.assertThat(predicate.test(3), CoreMatchers.is(true));
		Assert.assertThat(predicate.test(6), CoreMatchers.is(true));
		Assert.assertThat(predicate.test(0), CoreMatchers.is(true));
		Assert.assertThat(predicate.test(1), CoreMatchers.is(false));
		Assert.assertThat(predicate.test(null), CoreMatchers.is(false));
	}

	@Test
	public void testUnanimousPredicate() {

		final List<Predicate<? super Integer>> predicates = new ArrayList<Predicate<? super Integer>>();
		predicates.add(this.divisibleByThree);
		predicates.add(this.evenPredicate);

		final UnanimousPredicate<Integer> predicate = new UnanimousPredicate<Integer>(predicates);
		Assert.assertThat(predicate.test(2), CoreMatchers.is(false));
		Assert.assertThat(predicate.test(3), CoreMatchers.is(false));
		Assert.assertThat(predicate.test(6), CoreMatchers.is(true));
		Assert.assertThat(predicate.test(0), CoreMatchers.is(true));
		Assert.assertThat(predicate.test(null), CoreMatchers.is(false));
	}

	@Test
	public void testUnanimousPredicateDoesNotInterceptNull() {
		final List<Predicate<? super Integer>> predicates = new ArrayList<Predicate<? super Integer>>();
		predicates.add(Predicates.nullValue());
		predicates.add(Predicates.truth());
		final UnanimousPredicate<Integer> predicate = new UnanimousPredicate<Integer>(predicates);
		Assert.assertThat(predicate.test(null), CoreMatchers.is(true));
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
	public void testStartsWith() {
		final Predicate<String> predicate = Predicates.startsWith("foo");
		assertThat(predicate.test("foobar"), is(true));
		assertThat(predicate.test("bar"), is(false));
		assertThat(predicate.test("barfoo"), is(false));
		assertThat(predicate.test("foo"), is(true));
		assertThat(predicate.test(null), is(false));
		assertThat(Predicates.startsWith("f[o]o").test("foo"), is(false));
		assertThat(Predicates.startsWith(".").test("notime"), is(false));
		assertThat(Predicates.startsWith(".").test(".otime"), is(true));
	}

	@Test
	public void testEndsWith() {
		final Predicate<String> predicate = Predicates.endsWith("foo");
		assertThat(predicate.test("barfoo"), is(true));
		assertThat(predicate.test("foobar"), is(false));
		assertThat(predicate.test("bar"), is(false));
		assertThat(predicate.test("foo"), is(true));
		assertThat(predicate.test(null), is(false));
		assertThat(Predicates.endsWith("f[o]o").test("foo"), is(false));
		assertThat(Predicates.endsWith(".").test("notime"), is(false));
		assertThat(Predicates.endsWith(".").test("notime."), is(true));
	}

	@Test
	public void testRegex() {
		Assert.assertThat(Predicates.matches("notime").test("notime"), CoreMatchers.is(true));
		Assert.assertThat(Predicates.matches("bad").test("notime"), CoreMatchers.is(false));
		Assert.assertThat(Predicates.matches("cheeses?").test("cheese"), CoreMatchers.is(true));
		Assert.assertThat(Predicates.matches("^a+$").test("aaaaa"), CoreMatchers.is(true));
	}
}
