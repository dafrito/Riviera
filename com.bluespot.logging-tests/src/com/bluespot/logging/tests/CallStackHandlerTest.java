package com.bluespot.logging.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.bluespot.logging.CallStackHandler;
import com.bluespot.tree.PrintVisitor;
import com.bluespot.tree.Tree;
import com.bluespot.tree.TreeWalker;

public class CallStackHandlerTest {

	public static final String className = CallStackHandlerTest.class.getName();

	protected class Bar {
		private final Logger barLogger = CallStackHandlerTest.this.getLogger();

		private String name;

		public Bar(final String name) {
			this.setName(name);
		}

		public String getName() {
			return this.name;
		}

		public void setName(final String name) {
			this.barLogger.info("Setting bar name: " + name);
			this.name = name;
		}
	}

	protected class Foo {
		private final Logger fooLogger = CallStackHandlerTest.this.getLogger();

		List<Bar> bars = new ArrayList<Bar>();

		public Foo() {
			this.fooLogger.info("Created Foo");
		}

		public void addBar(final String name) {
			this.fooLogger.entering(className + "$Foo", "addBar");
			this.fooLogger.info("Adding bar with name: " + name);
			if (name.length() > 0) {
				this.addBar(name.substring(0, name.length() - 1));
			}
			this.bars.add(new Bar(name));
			this.fooLogger.exiting(className + "$Foo", "addBar");
		}

		public void addBars(final String... names) {
			this.fooLogger.info("Adding lots of bars: " + names.length);
			for (final String name : names) {
				this.fooLogger.info("Adding this name: " + name);
				this.addBar(name);
			}
		}
	}

	protected TreeWalker<LogRecord> builder;

	protected Logger logger;

	public Logger getLogger() {
		return this.logger;
	}

	@Before
	public void setUp() {
		this.builder = new TreeWalker<LogRecord>(new Tree<LogRecord>(null));
		this.logger = Logger.getAnonymousLogger();
		this.logger.setLevel(Level.FINEST);
		this.logger.addHandler(new CallStackHandler(this.builder));
	}

	public void runRandomOperations() {
		final Foo foo = new Foo();
		foo.addBar("No time");
		foo.addBars("A", "B", "C", "D");
		final Foo anotherFoo = new Foo();
		anotherFoo.addBar("Cheese!");
		new Bar("Crumpet.");
	}

	public void runSimplestOperation() {
		final Foo foo = new Foo();
		foo.addBars("A", "B", "C", "D");
	}

	@Test
	public void testLoggingStuff() {
		this.runSimplestOperation();
		final Tree<LogRecord> tree = this.builder.getCurrentNode();
		Assert.assertThat(tree.size(), CoreMatchers.is(CoreMatchers.not(0)));
		this.builder.getCurrentNode().visit(new PrintVisitor<LogRecord>() {

			@Override
			public String toString(final LogRecord record) {
				return record != null ? record.getSourceMethodName() + ": " + record.getMessage() : "null";
			}
		});
	}

	public void testSanity() {
		Assert.assertThat(this.getClass().getName(), CoreMatchers.is(className));
	}
}
