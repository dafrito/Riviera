package com.bluespot.swing.examples;

import javax.swing.JFrame;

import com.bluespot.demonstration.AbstractDemonstration;
import com.bluespot.swing.SimpleSwingForm;

public class FormDemonstration extends AbstractDemonstration {

	private SimpleSwingForm form;

	@Override
	public void initializeFrame(final JFrame frame) {
		frame.setSize(400, 400);

		this.form = new SimpleSwingForm();
		this.form.addTextField("First Name:");
		this.form.addTextField("Last Name:");
		this.form.addTextField("Email:");
		this.form.addTextField("Mandatory Order Number:");

		this.form.addButton("Cancel");
		this.form.addButton("Submit");

		frame.getContentPane().add(this.form.build());

	}

	public static void main(final String[] args) {
		// We really should throw this in a invokeLater, but doing so kills
		// our callstack. For a more realistic run, use SwingUtilities.
		new FormDemonstration().run();
	}

}
