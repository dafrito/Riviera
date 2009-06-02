package com.bluespot.swing;

import java.awt.Graphics;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class BeanView<T> extends JPanel {

	private BeanInfo beanInfo;
	private final Map<String, JTextField> fieldMap = new HashMap<String, JTextField>();
	private T value;

	public BeanView(final Class<T> viewedClass) throws IntrospectionException {

		final GroupLayoutBuilder builder = new GroupLayoutBuilder(this);

		this.beanInfo = Introspector.getBeanInfo(viewedClass);
		for (final PropertyDescriptor pd : this.beanInfo.getPropertyDescriptors()) {
			final PropertyEditor editor = PropertyEditorManager.findEditor(pd.getPropertyType());
			if (editor == null) {
				continue;
			}
			final JTextField textField = new JTextField();
			textField.setEditable(false);
			textField.setText(editor.getAsText());
			this.fieldMap.put(pd.getName(), textField);
			builder.addLabeledPair(pd.getDisplayName(), textField);
		}

		builder.build();
	}

	@SuppressWarnings("unchecked")
	public BeanView(final T value) throws IntrospectionException {
		this((Class<T>) value.getClass());
		this.setValue(value);
	}

	public T getValue() {
		return this.value;
	}

	public void setValue(final T value) {
		this.value = value;
		this.refresh();
	}

	@Override
	protected void printComponent(final Graphics g) {
		super.printComponent(g);

	}

	protected void refresh() {
		if (this.value == null) {
			this.setEnabled(false);
			return;
		}
		this.setEnabled(true);
		for (final PropertyDescriptor pd : this.beanInfo.getPropertyDescriptors()) {
			final JTextField field = this.fieldMap.get(pd.getName());
			PropertyEditor editor = pd.createPropertyEditor(this.getValue());
			if (editor == null) {
				editor = PropertyEditorManager.findEditor(pd.getPropertyType());
			}
			if (editor == null) {
				continue;
			}
			Object propertyValue = null;
			try {
				propertyValue = pd.getReadMethod().invoke(this.getValue());
			} catch (final Exception e) {
				// Just continue
				continue;
			}
			field.setText(propertyValue != null ? propertyValue.toString() : "null");
		}
	}

}
