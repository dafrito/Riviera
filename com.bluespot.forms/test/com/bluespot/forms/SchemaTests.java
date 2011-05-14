package com.bluespot.forms;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.bluespot.forms.submission.MutableSubmission;
import com.bluespot.logic.actors.Actor;
import com.bluespot.logic.actors.GuardedActor;
import com.bluespot.logic.predicates.Predicate;
import com.bluespot.logic.predicates.Predicates;
import com.bluespot.logic.predicates.builder.PredicateBuilder;

public class SchemaTests {

	private Schema<String> schema;
	private boolean flag = false;
	private MutableSubmission<String> submission;

	private final Actor<Object> actor = new Actor<Object>() {
		@Override
		public void receive(final Object value) {
			SchemaTests.this.flag = true;
		}
	};

	@Before
	public void setUp() {
		this.flag = false;
	}

	@Before
	public void newSchema() {
		final Map<String, Class<?>> types = new HashMap<String, Class<?>>();
		types.put("String", String.class);
		types.put("Number", Integer.class);
		types.put("Boolean", Boolean.class);

		final PredicateBuilder<Submission<String>> builder = new PredicateBuilder<Submission<String>>();

		final SubmissionFieldAdapter<String, String> adapter = new SubmissionFieldAdapter<String, String>("String",
				String.class);

		builder.has(adapter).that(Predicates.is("No time"));

		final Predicate<? super Submission<String>> pred = builder.build();

		this.schema = new Schema<String>(types, pred);

		this.submission = new MutableSubmission<String>(this.schema);
	}

	@Test(expected = NullPointerException.class)
	public void testNPEOnNullKey() {
		this.submission.put(null, "No time");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIAEOnIllegalKey() {
		this.submission.put("INVALID", "No time");
	}

	@Test(expected = NullPointerException.class)
	public void testNPEOnNullValue() {
		this.submission.put("String", null);
	}

	@Test(expected = ClassCastException.class)
	public void testCCEOnNotCastableValue() {
		this.submission.put("String", 42);
	}

	@Test
	public void testInvalidSubmission() {
		final GuardedActor<Submission<String>> sentinel = this.schema.newSentinel(this.actor);
		sentinel.receive(this.submission);
		assertTrue("Submission is invalid", !this.flag);
	}

	@Test
	public void testValidSubmission() {
		final GuardedActor<Submission<String>> sentinel = this.schema.newSentinel(this.actor);
		this.submission.put("String", "No time");
		sentinel.receive(this.submission);
		assertTrue("Submission is valid", this.flag);
	}
}
