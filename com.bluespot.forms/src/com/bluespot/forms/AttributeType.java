package com.bluespot.forms;

public enum AttributeType {
	DESCRIPTION, ID, LABEL, SELECTED, UNKNOWN, VALUE;

	public static AttributeType getAttributeType(final String name) {
		if (name == null) {
			throw new NullPointerException();
		}
		try {
			return AttributeType.valueOf(name.toUpperCase());
		} catch (final IllegalArgumentException e) {
			return AttributeType.UNKNOWN;
		}
	}

}
