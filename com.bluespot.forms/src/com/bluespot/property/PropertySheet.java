package com.bluespot.property;

/**
 * Represents a map of properties, with convenient methods for conversion.
 * <p>
 * Getters are not required to losslessly preserve values passed to setters.
 * This allows implementations to have custom storage. Getters are, however,
 * required to return some valid value for any given type.
 * <p>
 * If a property has no stored value, a default value may be returned.
 * Conventional defaults are specified in the documentation for the getter
 * methods, but implementations are allowed to deviate to allow more intuitive
 * behavior.
 * 
 * @author Aaron Faanes
 */
public interface PropertySheet {

	/**
	 * An exception indicating this property sheet cannot be modified.
	 * 
	 * @author Aaron Faanes
	 */
	public static class ImmutablePropertySheetException extends UnsupportedOperationException {

		/**
		 * Creates a new {@link ImmutablePropertySheetException}.
		 */
		public ImmutablePropertySheetException() {
			super("This property sheet is immutable");
		}
	}

	/**
	 * Returns whether this property sheet has a stored value for specified
	 * property.
	 * 
	 * @param name
	 *            the name of the property
	 * @return {@code true} if a value exists for the specified property, and
	 *         {@code false} otherwise
	 */
	public boolean contains(final String name);

	/**
	 * Returns the stored value for the specified property as a {@code boolean}.
	 * <p>
	 * Conversion may take place in order to guarantee a {@code boolean} is
	 * returned. If conversion fails, the default value will be returned.
	 * <p>
	 * The default value for this property may vary depending on the
	 * implementation. By convention, {@code false} would be returned.
	 * 
	 * @param name
	 *            the name of the property
	 * @return the value of the specified property, if any. It will be converted
	 *         to a {@code boolean}.
	 * @see PropertySheet#setBoolean(String, boolean)
	 */
	public boolean getBoolean(final String name);

	/**
	 * Returns the stored value for the specified property as a {@code double}.
	 * <p>
	 * Conversion may take place in order to guarantee a {@code double} is
	 * returned. If conversion fails, the default value will be returned.
	 * <p>
	 * The default value for this property may vary depending on the
	 * implementation. By convention, {@code 0.0d} would be returned.
	 * 
	 * @param name
	 *            the name of the property
	 * @return the value of the specified property, if any. It will be converted
	 *         to a {@code double}.
	 * @see PropertySheet#setDouble(String, double)
	 */
	public double getDouble(final String name);

	/**
	 * Returns the stored value for the specified property as a {@code float}.
	 * <p>
	 * Conversion may take place in order to guarantee a {@code float} is
	 * returned. If conversion fails, the default value will be returned.
	 * <p>
	 * The default value for this property may vary depending on the
	 * implementation. By convention, {@code 0.0f} would be returned.
	 * 
	 * @param name
	 *            the name of the property
	 * @return the value of the specified property, if any. It will be converted
	 *         to a {@code float}.
	 * @see PropertySheet#setFloat(String, float)
	 */
	public double getFloat(final String name);

	/**
	 * Returns the stored value for the specified property as an {@code int}.
	 * <p>
	 * Conversion may take place in order to guarantee a {@code int} is
	 * returned. If conversion fails, the default value will be returned.
	 * <p>
	 * The default value for this property may vary depending on the
	 * implementation. By convention, {@code 0} would be returned.
	 * 
	 * @param name
	 *            the name of the property
	 * @return the value of the specified property, if any. It will be converted
	 *         to an {@code int}.
	 * @see PropertySheet#setInteger(String, int)
	 */
	public int getInteger(final String name);

	/**
	 * Returns the stored value for the specified property as a {@code long}.
	 * <p>
	 * Conversion may take place in order to guarantee a {@code long} is
	 * returned. If conversion fails, the default value will be returned.
	 * <p>
	 * The default value for this property may vary depending on the
	 * implementation. By convention, {@code 0L} would be returned.
	 * 
	 * @param name
	 *            the name of the property
	 * @return the value of the specified property, if any. It will be converted
	 *         to a {@code long}.
	 * @see PropertySheet#setLong(String, long)
	 */
	public long getLong(final String name);

	/**
	 * Returns the stored value for the specified property as an {@code Object}.
	 * <p>
	 * Boxing may take place in order to guarantee an {@code Object} is
	 * returned. If boxing fails, the default value will be returned.
	 * <p>
	 * The default value for this property may vary depending on the
	 * implementation. By convention, {@code null} would be returned.
	 * 
	 * @param name
	 *            the name of the property
	 * @return the value of the specified property, if any. It will be converted
	 *         to an {@code Object}.
	 * @see PropertySheet#setObject(String, Object)
	 */
	public Object getObject(final String name);

	/**
	 * Returns the stored value for the specified property as a {@code String}.
	 * <p>
	 * Conversion may take place in order to guarantee a {@code String} is
	 * returned. If conversion fails, the default value will be returned.
	 * <p>
	 * The default value for this property may vary depending on the
	 * implementation. By convention, {@code ""} would be returned.
	 * 
	 * @param name
	 *            the name of the property
	 * @return the value of the specified property, if any. It will be converted
	 *         to a {@code String}.
	 * @see PropertySheet#setString(String, String)
	 */
	public String getString(final String name);

	/**
	 * Resets the stored value for the specified property to the
	 * implementation-specific default value.
	 * <p>
	 * If a property sheet does not support modification, {@code false} should
	 * be returned.
	 * 
	 * @param name
	 *            the name of the property to reset
	 * @return {@code true} if this property sheet changed as a result of this
	 *         call
	 */
	public boolean reset(final String name);

	/**
	 * Resets all properties in this sheet to their implementation-specific
	 * default values.
	 * <p>
	 * If a property sheet does not support modification, nothing should be done
	 * here.
	 */
	public void resetAll();

	/**
	 * Sets the stored value of the specified property to the specified {@code
	 * boolean} value.
	 * <p>
	 * Implementations are not required to losslessly preserve the values passed
	 * into this method. Therefore, a call to the corresponding getter is not
	 * guaranteed to return a value equal to the specified value set here.
	 * <p>
	 * Use {@link PropertySheet#reset(String) reset(String)} if you wish to
	 * reset the current value of the specified property to its default value.
	 * 
	 * @param name
	 *            the name of the property
	 * @param value
	 *            the new value of the property
	 * @return {@code true} if this property sheet changed as a result of this
	 *         call
	 * @see PropertySheet#getBoolean(String)
	 * @throw {@link ImmutablePropertySheetException} if this property sheet
	 *        does not support modification.
	 */
	public boolean setBoolean(final String name, final boolean value);

	/**
	 * Sets the stored value of the specified property to the specified {@code
	 * double} value.
	 * <p>
	 * Implementations are not required to losslessly preserve the values passed
	 * into this method. Therefore, a call to the corresponding getter is not
	 * guaranteed to return a value equal to the specified value set here.
	 * <p>
	 * Use {@link PropertySheet#reset(String) reset(String)} if you wish to
	 * reset the current value of the specified property to its default value.
	 * 
	 * @param name
	 *            the name of the property
	 * @param value
	 *            the new value of the property
	 * @see PropertySheet#getDouble(String)
	 * @return {@code true} if this property sheet changed as a result of this
	 *         call
	 * @throw {@link ImmutablePropertySheetException} if this property sheet
	 *        does not support modification.
	 */
	public boolean setDouble(final String name, final double value);

	/**
	 * Sets the stored value of the specified property to the specified {@code
	 * float} value.
	 * <p>
	 * Implementations are not required to losslessly preserve the values passed
	 * into this method. Therefore, a call to the corresponding getter is not
	 * guaranteed to return a value equal to the specified value set here.
	 * <p>
	 * Use {@link PropertySheet#reset(String) reset(String)} if you wish to
	 * reset the current value of the specified property to its default value.
	 * 
	 * @param name
	 *            the name of the property
	 * @param value
	 *            the new value of the property
	 * @see PropertySheet#getFloat(String)
	 * @return {@code true} if this property sheet changed as a result of this
	 *         call
	 * @throw {@link ImmutablePropertySheetException} if this property sheet
	 *        does not support modification.
	 */
	public boolean setFloat(final String name, final float value);

	/**
	 * Sets the stored value of the specified property to the specified {@code
	 * int} value.
	 * <p>
	 * Implementations are not required to losslessly preserve the values passed
	 * into this method. Therefore, a call to the corresponding getter is not
	 * guaranteed to return a value equal to the specified value set here.
	 * <p>
	 * Use {@link PropertySheet#reset(String) reset(String)} if you wish to
	 * reset the current value of the specified property to its default value.
	 * 
	 * @param name
	 *            the name of the property
	 * @param value
	 *            the new value of the property
	 * @see PropertySheet#getInteger(String)
	 * @return {@code true} if this property sheet changed as a result of this
	 *         call
	 * @throw {@link ImmutablePropertySheetException} if this property sheet
	 *        does not support modification.
	 */
	public boolean setInteger(final String name, final int value);

	/**
	 * Sets the stored value of the specified property to the specified {@code
	 * long} value.
	 * <p>
	 * Implementations are not required to losslessly preserve the values passed
	 * into this method. Therefore, a call to the corresponding getter is not
	 * guaranteed to return a value equal to the specified value set here.
	 * <p>
	 * Use {@link PropertySheet#reset(String) reset(String)} if you wish to
	 * reset the current value of the specified property to its default value.
	 * 
	 * @param name
	 *            the name of the property
	 * @param value
	 *            the new value of the property
	 * @see PropertySheet#getLong(String)
	 * @return {@code true} if this property sheet changed as a result of this
	 *         call
	 * @throw {@link ImmutablePropertySheetException} if this property sheet
	 *        does not support modification.
	 */
	public boolean setLong(final String name, final long value);

	/**
	 * Sets the stored value of the specified property to the specified {@code
	 * Object} value.
	 * <p>
	 * Implementations are not required to losslessly preserve the values passed
	 * into this method. Therefore, a call to the corresponding getter is not
	 * guaranteed to return a value equal to the specified value set here.
	 * <p>
	 * Use {@link PropertySheet#reset(String) reset(String)} if you wish to
	 * reset the current value of the specified property to its default value.
	 * 
	 * @param name
	 *            the name of the property
	 * @param value
	 *            the new value of the property
	 * @see PropertySheet#getObject(String)
	 * @return {@code true} if this property sheet changed as a result of this
	 *         call
	 * @throw {@link ImmutablePropertySheetException} if this property sheet
	 *        does not support modification.
	 */
	public boolean setObject(final String name, final Object value);

	/**
	 * Sets the stored value of the specified property to the specified {@code
	 * String} value.
	 * <p>
	 * Implementations are not required to losslessly preserve the values passed
	 * into this method. Therefore, a call to the corresponding getter is not
	 * guaranteed to return a value equal to the specified value set here.
	 * <p>
	 * Use {@link PropertySheet#reset(String) reset(String)} if you wish to
	 * reset the current value of the specified property to its default value.
	 * 
	 * @param name
	 *            the name of the property
	 * @param value
	 *            the new value of the property
	 * @see PropertySheet#getString(String)
	 * @return {@code true} if this property sheet changed as a result of this
	 *         call
	 * @throw {@link ImmutablePropertySheetException} if this property sheet
	 *        does not support modification.
	 */
	public boolean setString(final String name, final String value);
}
