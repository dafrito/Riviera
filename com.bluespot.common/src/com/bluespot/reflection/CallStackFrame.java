package com.bluespot.reflection;

/**
 * Represents a single frame in a call stack.
 * <p>
 * This class is immutable.
 * 
 * @author Aaron Faanes
 * 
 */
public class CallStackFrame {

    private final String className;

    private final String methodName;

    private final String packageName;

    /**
     * Constructs a frame from the specified class and method name. This method
     * will make a best-effort to create a valid frame from the inputs and will
     * not throw on invalid ones.
     * 
     * @param qualifiedClassName
     *            the fully qualified class name of the method. Inner classes
     *            should be included with this name
     * @param methodName
     *            the name of the method. Do not include the return type or
     *            parameters
     */
    public CallStackFrame(final String qualifiedClassName, final String methodName) {
        this.className = qualifiedClassName != null ? qualifiedClassName : "";
        this.methodName = methodName != null ? methodName : "";
        final int lastPeriod = this.className.lastIndexOf('.');
        if (lastPeriod >= 0) {
            this.packageName = this.className.substring(0, lastPeriod);
        } else {
            this.packageName = "";
        }
    }

    /**
     * @return the class name of this name. This will include any inner or
     *         anonymous classes
     * 
     */
    public String getClassName() {
        return this.className;
    }

    /**
     * @return the name of the method from which this frame originated represent
     */
    public String getMethodName() {
        return this.methodName;
    }

    /**
     * @return the package from which this frame originated. This may return
     *         {@code null} if a package cannot be found for this frame's
     *         package name
     * @see #getPackageName()
     */
    public Package getPackage() {
        return Package.getPackage(this.getPackageName());
    }

    /**
     * @return the name of the package from which this frame originated
     * @see #getPackage()
     */
    public String getPackageName() {
        return this.packageName;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CallStackFrame)) {
            return false;
        }
        final CallStackFrame otherEntry = (CallStackFrame) other;
        if (!this.getClassName().equals(otherEntry.getClassName())) {
            return false;
        }
        if (!this.getMethodName().equals(otherEntry.getMethodName())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.getClassName().hashCode();
        result = 31 * result + this.getMethodName().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("CallStack.Frame(%s, %s)", this.getClassName(), this.getMethodName());
    }
}
