package com.bluespot.forms;

public enum ElementType {
	CHECKBOXES {

	},
	COMBOBOX {

	},
	FILE {

	},
	FLOW {

	},
	FORM {

	},
	NUMBER {

	},
	OPTION {

	},
	STRING {

	},
	UNKNOWN {

	};

	public static ElementType getElementType(final String name) {
		if (name == null) {
			throw new NullPointerException();
		}
		try {
			return ElementType.valueOf(name.toUpperCase());
		} catch (final IllegalArgumentException e) {
			return ElementType.UNKNOWN;
		}
	}

}
