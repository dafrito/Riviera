package com.bluespot.property;

/**
 * A property sheet that contains, and can contain, no values.
 * 
 * @author Aaron Faanes
 */
public class EmptyPropertySheet implements PropertySheet {

	@Override
	public boolean contains(final String name) {
		return false;
	}

	@Override
	public boolean getBoolean(final String name) {
		return false;
	}

	@Override
	public double getDouble(final String name) {
		return 0;
	}

	@Override
	public double getFloat(final String name) {
		return 0;
	}

	@Override
	public int getInteger(final String name) {
		return 0;
	}

	@Override
	public long getLong(final String name) {
		return 0;
	}

	@Override
	public Object getObject(final String name) {
		return null;
	}

	@Override
	public String getString(final String name) {
		return "";
	}

	@Override
	public boolean reset(final String name) {
		return false;
	}

	@Override
	public void resetAll() {
		// Do nothing; this property sheet is empty.
	}

	@Override
	public boolean setBoolean(final String name, final boolean value) {
		throw new PropertySheet.ImmutablePropertySheetException();
	}

	@Override
	public boolean setDouble(final String name, final double value) {
		throw new PropertySheet.ImmutablePropertySheetException();
	}

	@Override
	public boolean setFloat(final String name, final float value) {
		throw new PropertySheet.ImmutablePropertySheetException();
	}

	@Override
	public boolean setInteger(final String name, final int value) {
		throw new PropertySheet.ImmutablePropertySheetException();
	}

	@Override
	public boolean setLong(final String name, final long value) {
		throw new PropertySheet.ImmutablePropertySheetException();
	}

	@Override
	public boolean setObject(final String name, final Object value) {
		throw new PropertySheet.ImmutablePropertySheetException();
	}

	@Override
	public boolean setString(final String name, final String value) {
		throw new PropertySheet.ImmutablePropertySheetException();
	}

}
