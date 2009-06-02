package com.bluespot.junit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

public class InjectingRunner extends Runner {

	public static class InjectStatement extends Statement {

		private final Field field;

		private final Class<?> injectedClass;

		private final Statement nextStatement;

		private final Object target;

		public InjectStatement(final Statement next, final Object target, final Field field,
				final Class<?> injectedClass) {
			this.nextStatement = next;
			this.target = target;
			this.field = field;
			this.injectedClass = injectedClass;
		}

		@Override
		public void evaluate() throws Throwable {
			this.field.set(this.target, this.injectedClass.newInstance());
			this.nextStatement.evaluate();
		}

	}

	private class InternalRunner extends BlockJUnit4ClassRunner {

		public InternalRunner(final Class<?> klass) throws InitializationError {
			super(klass);
		}

		private Constructor<?> getConstructor(final Class<?> klass) {
			for (final Constructor<?> constructor : klass.getConstructors()) {
				if (constructor.getParameterTypes().length == 0) {
					return constructor;
				}
			}
			return null;
		}

		private Field[] getFields() {
			return this.getTestClass().getJavaClass().getDeclaredFields();
		}

		private Field getMatchingFieldForClass(final Class<?> klass) {
			for (final Field field : this.getFields()) {
				if (field.getAnnotation(Injected.class) == null) {
					continue;
				}
				if ((field.getModifiers() & Modifier.PUBLIC) == 0) {
					throw new IllegalStateException("@Injected field '" + field.getName() + "' must be public. Class: "
							+ this.getTestClass().getName());
				}
				if (field.getType().isAssignableFrom(klass)) {
					return field;
				}
			}
			return null;
		}

		private void validateInjection(final List<Throwable> errors) {
			final Class<?> klass = InjectingRunner.this.getInjectedValue();
			if (this.getConstructor(klass) == null) {
				errors.add(new Exception("Injected class must have a zero-arg constructor: " + klass));
			}
			if (this.getMatchingFieldForClass(klass) == null) {
				errors.add(new Exception(this.getTestClass().getName()
						+ " does not have a public @Injected field for type: " + klass));
			}
		}

		@Override
		protected void collectInitializationErrors(final List<Throwable> errors) {
			super.collectInitializationErrors(errors);
			this.validateInjection(errors);
		}

		@Override
		protected Statement withBefores(final FrameworkMethod method, final Object target,
				final Statement currentStatement) {
			Statement statement = super.withBefores(method, target, currentStatement);
			final Field field = this.getMatchingFieldForClass(InjectingRunner.this.getInjectedValue());
			statement = new InjectStatement(statement, target, field, InjectingRunner.this.getInjectedValue());
			return statement;
		}
	}

	private final Class<?> originalClass;

	private final List<Runner> runners = new ArrayList<Runner>();

	public InjectingRunner(final Class<?> klass) throws InitializationError {
		this.originalClass = klass;
		for (final Class<?> testClass : this.getTestClasses()) {
			this.runners.add(new InternalRunner(testClass));
		}
		this.runners.add(new BlockJUnit4ClassRunner(this.originalClass) {
			@Override
			protected void validateInstanceMethods(final List<Throwable> errors) {
				// Override this method because we don't want an exception
				// thrown
				// if our test suite has no methods; this may be the case if the
				// suite is merely a stub. The rest of the method is unchanged.
				this.validatePublicVoidNoArgMethods(After.class, false, errors);
				this.validatePublicVoidNoArgMethods(Before.class, false, errors);
				this.validateTestMethods(errors);
			}
		});
	}

	@Override
	public Description getDescription() {
		final Description description = Description.createSuiteDescription(this.getOriginalClass());
		for (final Runner runner : this.getUnderlyingRunners()) {
			for (final Description childDesc : runner.getDescription().getChildren()) {
				description.addChild(childDesc);
			}
		}
		return description;
	}

	/**
	 * Returns the class of the value that we plan to inject on this runner's
	 * {@code TestClass}
	 * 
	 * @return the injected value's class
	 */
	public Class<?> getInjectedValue() {
		return this.getAnnotation().injectedValue();
	}

	/**
	 * Returns the original class that the {@code RunWith} annotation was
	 * attached to.
	 * 
	 * @return the class that indirectly invoked this runner
	 */
	public Class<?> getOriginalClass() {
		return this.originalClass;
	}

	/**
	 * Returns the classes that contains the tests, annotated like a standard
	 * JUnit 4 test.
	 * 
	 * @return the classes that contains the tests for this runner
	 */
	public Class<?>[] getTestClasses() {
		return this.getAnnotation().testClasses();
	}

	public List<? extends Runner> getUnderlyingRunners() {
		return Collections.unmodifiableList(this.runners);
	}

	@Override
	public void run(final RunNotifier notifier) {
		for (final Runner runner : this.getUnderlyingRunners()) {
			runner.run(notifier);
		}
	}

	private InjectTest getAnnotation() {
		return this.getOriginalClass().getAnnotation(InjectTest.class);
	}
}
