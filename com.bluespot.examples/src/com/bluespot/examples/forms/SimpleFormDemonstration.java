package com.bluespot.examples.forms;

import javax.swing.JComponent;

import com.bluespot.demonstration.Demonstration;
import com.bluespot.swing.Components;
import com.bluespot.swing.GroupLayoutBuilder;

/**
 * Demonstrates {@link GroupLayoutBuilder}.
 * 
 * @author Aaron Faanes
 * 
 */
public final class SimpleFormDemonstration extends Demonstration {

	@Override
	protected JComponent newContentPane() {
		final GroupLayoutBuilder form = new GroupLayoutBuilder();
		form.label("<html>Type in a name of a program, folder, document, or <br/>Internet resource, and Windows will open it for you.</html>");
		form.field("Name");
		form.field("Lower Bound");
		form.field("Upper Bound");
		form.button("OK");
		return form.build();
	}

	/**
	 * Runs a {@link SimpleFormDemonstration} using {@link Demonstration}.
	 * 
	 * @param args
	 *            unused
	 */
	public static void main(final String[] args) {
		Components.LookAndFeel.SYSTEM.activate();
		Demonstration.launch(SimpleFormDemonstration.class);
	}

}
