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

    private final Class<?> originalClass;
    
    private final List<Runner> runners = new ArrayList<Runner>();

    public InjectingRunner(Class<?> klass) throws InitializationError {
        this.originalClass = klass;
        for(Class<?> testClass : this.getTestClasses()) {
            this.runners.add(new InternalRunner(testClass));
        }
        this.runners.add(new BlockJUnit4ClassRunner(this.originalClass) {
            @Override
            protected void validateInstanceMethods(List<Throwable> errors) {
                // Override this method because we don't want an exception thrown
                // if our test suite has no methods; this may be the case if the
                // suite is merely a stub. The rest of the method is unchanged. 
                validatePublicVoidNoArgMethods(After.class, false, errors);
                validatePublicVoidNoArgMethods(Before.class, false, errors);
                validateTestMethods(errors);
            }
        });
    }
    
    private InjectTest getAnnotation() {
        return this.getOriginalClass().getAnnotation(InjectTest.class);
    }

    /**
     * Returns the class of the value that we plan to inject on this runner's {@code TestClass}
     * @return the injected value's class
     */
    public Class<?> getInjectedValue() {
        return this.getAnnotation().injectedValue();
    }
    
    /**
     * Returns the classes that contains the tests, annotated like a standard JUnit 4 test.
     * @return the classes that contains the tests for this runner
     */
    public Class<?>[] getTestClasses() {
        return this.getAnnotation().testClasses();
    }

    /**
     * Returns the original class that the {@code RunWith} annotation was attached to. 
     * @return the class that indirectly invoked this runner
     */
    public Class<?> getOriginalClass() {
        return this.originalClass;
    }
    
    public List<? extends Runner> getUnderlyingRunners() {
        return Collections.unmodifiableList(this.runners);
    }

    @Override
    public Description getDescription() {
        Description description = Description.createSuiteDescription(this.getOriginalClass());
        for(Runner runner : this.getUnderlyingRunners()) {
            for(Description childDesc : runner.getDescription().getChildren()) {
                description.addChild(childDesc);
            }
        }
        return description;
    }
    
    @Override
    public void run(RunNotifier notifier) {
        for(Runner runner : this.getUnderlyingRunners()) {
            runner.run(notifier);
        }
    }
    
    private class InternalRunner extends BlockJUnit4ClassRunner {

        public InternalRunner(Class<?> klass) throws InitializationError {
            super(klass);
        }

        @Override
        protected void collectInitializationErrors(List<Throwable> errors) {
            super.collectInitializationErrors(errors);
            this.validateInjection(errors);
        }

        private void validateInjection(List<Throwable> errors) {
            Class<?> klass = InjectingRunner.this.getInjectedValue();
            if(this.getConstructor(klass) == null) {
                errors.add(new Exception("Injected class must have a zero-arg constructor: " + klass));
            }
            if(this.getMatchingFieldForClass(klass) == null) {
                errors.add(new Exception(this.getTestClass().getName() + " does not have a public @Injected field for type: " + klass));
            }
        }

        private Constructor<?> getConstructor(Class<?> klass) {
            for(Constructor<?> constructor : klass.getConstructors()) {
                if(constructor.getParameterTypes().length == 0) {
                    return constructor;
                }
            }
            return null;
        }

        @Override
        protected Statement withBefores(FrameworkMethod method, final Object target, Statement currentStatement) {
            Statement statement = super.withBefores(method, target, currentStatement);
            Field field = this.getMatchingFieldForClass(getInjectedValue());
            statement = new InjectStatement(statement, target, field, getInjectedValue());
            return statement;
        }

        private Field[] getFields() {
            return this.getTestClass().getJavaClass().getDeclaredFields();
        }

        private Field getMatchingFieldForClass(Class<?> klass) {
            for(Field field : this.getFields()) {
                if(field.getAnnotation(Injected.class) == null) {
                    continue;
                }
                if((field.getModifiers() & Modifier.PUBLIC) == 0) {
                    throw new IllegalStateException("@Injected field '" + field.getName() + "' must be public. Class: " + this.getTestClass().getName());
                }
                if(field.getType().isAssignableFrom(klass)) {
                    return field;
                }
            }
            return null;
        }
    }

    public static class InjectStatement extends Statement {

        private final Statement nextStatement;

        private final Object target;

        private final Field field;

        private final Class<?> injectedClass;

        public InjectStatement(Statement next, Object target, Field field, Class<?> injectedClass) {
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
}
