package com.bluespot.junit;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;

public class NoopRunner extends Runner {

	private final Class<?> testClass;

	public NoopRunner(final Class<?> klass) {
		this.testClass = klass;
	}

	@Override
	public Description getDescription() {
		return Description.createSuiteDescription(this.testClass);
	}

	@Override
	public void run(final RunNotifier notifier) {
		// Do nothing.
	}

}
