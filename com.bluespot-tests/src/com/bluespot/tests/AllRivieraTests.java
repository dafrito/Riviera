package com.bluespot.tests;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
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
public final class AllRivieraTests {

    /**
     * Tests to explicitly exclude, for whatever reason
     */
    private static final Set<String> EXCLUDED = new HashSet<String>(Arrays.asList("com.bluespot.junit.InjectTest",
            AllRivieraTests.class.getName()));

    /**
     * A set of tests, populated when this is run as a JUnit test.
     */
    private final Set<Class<?>> tests = new HashSet<Class<?>>();

    private final StringBuilder currentPackage = new StringBuilder();

    private String getCurrentPackage() {
        return this.currentPackage.toString();
    }

    private void enterNestedPackage(final String nestedPackageName) {
        if (this.currentPackage.length() == 0) {
            this.currentPackage.append(nestedPackageName);
        } else {
            this.currentPackage.append('.');
            this.currentPackage.append(nestedPackageName);
        }
    }

    private void leaveNestedPackage(final String name) {
        if (this.currentPackage.length() == name.length()) {
            this.currentPackage.setLength(0);
        } else {
            // We subtract an additional one for the "." in between package
            // names
            this.currentPackage.substring(0, this.currentPackage.length() - name.length() - 1);
        }
    }

    private String getClassNameFromFile(final File file) {
        final String fileName = file.getName();
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }

    /**
     * Adds all test classes from the specified file. If the file is a folder,
     * then it will be recursed.
     * 
     * @param file
     *            the file that potentially is, or potentially contains, test
     *            classes
     */
    public void addClassesFrom(final File file) {
        if (file.getName().startsWith(".")) {
            // Return; it's not interesting to us.
            return;
        }
        if (this.isTestClass(file)) {
            final String className = this.getCurrentPackage() + "." + this.getClassNameFromFile(file);
            if (AllRivieraTests.EXCLUDED.contains(className)) {
                return;
            }
            try {
                this.tests.add(Class.forName(className));
            } catch (final ClassNotFoundException e) {
                System.out.printf("Class looks like a test, but couldn't be found: %s%n", className);
            }
            return;
        }
        this.enterNestedPackage(file.getName());
        final File[] children = file.listFiles();
        if (children == null) {
            return;
        }
        for (final File child : children) {
            this.addClassesFrom(child);
        }
        this.leaveNestedPackage(file.getName());
    }

    private boolean isTestClass(final File file) {
        return file.getName().endsWith("Test.java") || file.getName().endsWith("Tests.java");
    }

    private boolean isProjectDir(final File file) {
        return file.isDirectory() && !file.getName().startsWith(".");
    }

    /**
     * Adds all test files in the specified project to this object's tests set.
     * 
     * @param projectFile
     *            the folder containing the test files. The folder should be
     *            structured like an Eclipse project
     */
    public void addProject(final File projectFile) {
        if (!this.isProjectDir(projectFile)) {
            return;
        }
        final File srcDir = new File(projectFile, "src");
        if (srcDir.exists() && srcDir.isDirectory()) {
            for (final File child : srcDir.listFiles()) {
                this.addClassesFrom(child);
            }
        }
    }

    /**
     * Returns the list of tests this object has found.
     * 
     * @return the list of tests this object has found
     * @see #addProject(File)
     * @see #addClassesFrom(File)
     */
    public Set<Class<?>> getTests() {
        return Collections.unmodifiableSet(this.tests);
    }

    /**
     * Creates a new test suite out of all the test projects in Riviera.
     * <p>
     * Any test that is intended to be found here must be on the classpath for
     * this project. Refer to {@link AllRivieraTests} for more information
     * concerning which tests will be included.
     * 
     * @return a test suite containing all test classes in Riviera
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite("Riviera Tests");

        final AllRivieraTests tests = new AllRivieraTests();

        for (final File child : new File("..").listFiles()) {
            tests.addProject(child);
        }
        for (final Class<?> klass : tests.getTests()) {
            if (!Modifier.isAbstract(klass.getModifiers()) && klass.getConstructors().length > 0) {
                suite.addTest(new JUnit4TestAdapter(klass));
            }
        }

        return suite;
    }
}
