package reflection;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

/**
 * A collection of methods designed to make common reflection tasks easier.
 * 
 * @author Aaron Faanes
 * 
 */
public final class Reflection {

	private Reflection() {
		// Suppress default constructor to ensure non-instantiability
		throw new AssertionError("Instantiation not allowed");
	}

	/**
	 * Invokes the specified constructor, returning the newly created object.
	 * This will make a best-effort to successfully invoke the specified method.
	 * Specifically
	 * 
	 * @param <T>
	 *            the type of the created object
	 * @param constructor
	 *            the constructor that is invoked to create the object
	 * @return the newly constructed object
	 * @throws InvocationTargetException
	 *             if an exception is thrown during construction
	 * @throws SecurityException
	 *             if this method fails to change the accessibility on the
	 *             specified constructor due to a security violation, see
	 *             {@link AccessibleObject#setAccessible(boolean)}.
	 * @throws IllegalArgumentException
	 *             if the constructor's declaring class is abstract
	 */
	private static <T> T invoke(final Constructor<T> constructor) throws InvocationTargetException {
		if (Modifier.isAbstract(constructor.getDeclaringClass().getModifiers())) {
			throw new IllegalArgumentException("Constructor's declaring class is abstract");
		}
		final boolean forceAccessible = !constructor.isAccessible();
		try {
			if (forceAccessible) {
				/*
				 * We just allow the SecurityException to propagate here if it's
				 * thrown
				 */
				constructor.setAccessible(true);
			}
			return constructor.newInstance();
		} catch (final IllegalAccessException e) {
			/*
			 * We check for accessbility before invoking the new instance stuff,
			 * so I'm not sure this would ever be executed. At any rate, we
			 * throw an assertion error if it were to occur.
			 */
			throw new AssertionError(e);
		} catch (final InstantiationException e) {
			/*
			 * We throw an assertion error because I don't think this ever
			 * fires. We preemptively check for it being abstract and the
			 * constructor has no arguments, so there's not a reason for this to
			 * happen.
			 */
			throw new AssertionError(e);
		} finally {
			try {
				if (forceAccessible) {
					// Clean up after ourselves
					constructor.setAccessible(false);
				}
			} catch (final SecurityException ex) {
				/*
				 * An exception thrown here means we left our constructor in an
				 * inconsistent state, so the best recourse is to simply
				 * propagate the exception
				 */
				throw ex;
			}
		}
	}

	/**
	 * Creates a new object using the zero-argument, or nullary, constructor of
	 * the specified class. This will make a best-effort to circumvent access
	 * rules without throwing security exceptions. This also tries to minimize
	 * the amount of checked exceptions that are thrown when attempting to
	 * instantiate this class.
	 * <p>
	 * As a consequence, while less checked exceptions are thrown, the
	 * underlying problems are still irrecoverable. For example, abstract
	 * classes cause a checked {@link InstantiationException}. We check for the
	 * preconditions for that exception, and instead avoid them or throw runtime
	 * exceptions.
	 * 
	 * @param <T>
	 *            the type of object that is created
	 * @param klass
	 *            the class from which a new object will be created. It must not
	 *            be abstract, and it must have a zero-arg constructor. If the
	 *            constructor is inaccessible, due to it being private for
	 *            example, then the client must have sufficient permissions to
	 *            force it to be accessible.
	 * @return a new instance of the specified class
	 * @throws IllegalArgumentException
	 *             if the class is abstract
	 * @throws SecurityException
	 *             if the attempt to make an inaccessible class accessible
	 *             fails. See {@link AccessibleObject#setAccessible(boolean)}.
	 * @throws InvocationTargetException
	 *             if an exception is thrown during construction
	 */
	public static <T> T invokeZeroArgConstructor(final Class<T> klass) throws InvocationTargetException {
		try {
			final Constructor<T> constructor = klass.getConstructor();
			return Reflection.invoke(constructor);
		} catch (final NoSuchMethodException e) {
			throw new IllegalArgumentException(klass + " does not have zero-arg constructor", e);
		}
	}

}
