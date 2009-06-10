package com.bluespot.logic.adapters;

import javax.swing.JComponent;

/**
 * An adapter that gets the name of a given {@link JComponent}.
 * 
 * @author Aaron Faanes
 * @see JComponent#getName()
 * @see JComponent#setName(String)
 */
public class ComponentNameAdapter implements Adapter<JComponent, String> {

	private ComponentNameAdapter() {
		// Suppress default constructor
	}

	@Override
	public String adapt(final JComponent source) {
		if (source == null) {
			return null;
		}
		return source.getName();
	}

	@Override
	public String toString() {
		return "component name";
	}

	private static final ComponentNameAdapter INSTANCE = new ComponentNameAdapter();

	/**
	 * Returns the only {@link ComponentNameAdapter} object.
	 * 
	 * @return the only component name adapter
	 */
	public static ComponentNameAdapter getInstance() {
		return INSTANCE;
	}
}
