package com.bluespot.tests;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Creates a test suite of all tests in Riviera. This recurses all projects and
 * collects classes that look like tests and adds them to the Riviera test
 * suite.
 * <p>
 * Notice that you must add test classes to this project's class path, otherwise
 * you'll get logged warnings about classes that weren't found. If you want to
 * explicitly ignore a class that's getting matched, add it to
 * AllRivieraTests.EXCLUDED
 * <p>
 * In order to get a class to get added as a test case, name it XXXTest.java or
 * XXXTests.java. You shouldn't name it AllXXXTest.java since we may ignore
 * those types of tests in the future.
 * 
 * @author Aaron Faanes
 * 
 */
public class AllRivieraTests {

	/**
	 * Tests to explicitly exclude, for whatever reason
	 */
	private static final Set<String> EXCLUDED = new HashSet<String>(Arrays.asList("com.bluespot.junit.InjectTest"));

	/**
	 * A set of tests, populated when this is run as a JUnit test.
	 */
	private static final Set<Class<?>> TESTS = new HashSet<Class<?>>();

	private static String getChildPackage(final String parentPackageName, final String childPackage) {
		if (parentPackageName.equals("")) {
			return childPackage;
		}
		return parentPackageName + "." + childPackage;
	}

	private static String getClassNameFromFile(final File file) {
		final String fileName = file.getName();
		return fileName.substring(0, fileName.lastIndexOf('.'));
	}

	public static void addClassesFrom(final String parentPackageName, final File file) {
		if (file.getName().startsWith(".")) {
			// Return; it's not interesting to us.
			return;
		}
		if (AllRivieraTests.isTestClass(file)) {
			final String className = parentPackageName + "." + getClassNameFromFile(file);
			if (EXCLUDED.contains(className)) {
				return;
			}
			try {
				TESTS.add(Class.forName(className));
			} catch (final ClassNotFoundException e) {
				System.out.printf("Class looks like a test, but couldn't be found: %s%n", className);
			}
			return;
		}
		final String packageName = getChildPackage(parentPackageName, file.getName());
		final File[] children = file.listFiles();
		if (children == null) {
			return;
		}
		for (final File child : children) {
			AllRivieraTests.addClassesFrom(packageName, child);
		}
	}

	private static boolean isTestClass(final File file) {
		return file.getName().endsWith("Test.java") || file.getName().endsWith("Tests.java");
	}

	private static boolean isProjectDir(final File file) {
		return file.isDirectory() && !file.getName().startsWith(".");
	}

	public static void addProject(final File projectFile) {
		if (!AllRivieraTests.isProjectDir(projectFile)) {
			return;
		}
		final File srcDir = new File(projectFile, "src");
		if (srcDir.exists() && srcDir.isDirectory()) {
			for (final File child : srcDir.listFiles()) {
				AllRivieraTests.addClassesFrom("", child);
			}
		}
	}

	public static Test suite() {
		final TestSuite suite = new TestSuite("Riviera Tests");
		for (final File child : new File("..").listFiles()) {
			AllRivieraTests.addProject(child);
		}
		// Explicitly exclude this class; it seems like there's be serious
		// recursion issues if we included it.
		AllRivieraTests.TESTS.remove(AllRivieraTests.class);
		for (final Class<?> klass : AllRivieraTests.TESTS) {
			if (!Modifier.isAbstract(klass.getModifiers()) && klass.getConstructors().length > 0) {
				suite.addTest(new JUnit4TestAdapter(klass));
			}
		}
		return suite;
	}
}
