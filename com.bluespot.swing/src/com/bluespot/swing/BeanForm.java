package com.bluespot.swing;

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

/**
 * A very simple and incomplete view of a bean.
 * 
 * @author Aaron Faanes
 * 
 * @param <T>
 *            the type of the viewed bean
 */
public class BeanForm<T> extends JPanel {

    private static final long serialVersionUID = -8964151905286569804L;

    private BeanInfo beanInfo;
    private final Map<String, JTextField> fieldMap = new HashMap<String, JTextField>();
    private T value;

    /**
     * Constructs a bean view that views the specified class.
     * 
     * @param viewedClass
     *            the viewed class
     * @throws IntrospectionException
     *             if bean introspection fails
     * @see Introspector#getBeanInfo(Class)
     */
    public BeanForm(final Class<T> viewedClass) throws IntrospectionException {

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
            builder.field(pd.getDisplayName(), textField);
        }

        builder.build();
    }

    /**
     * Constructs a bean view that views the specified value.
     * 
     * @param value
     *            the viewed value
     * @throws IntrospectionException
     *             if bean introspection fails
     * @see Introspector#getBeanInfo(Class)
     */
    @SuppressWarnings("unchecked")
    public BeanForm(final T value) throws IntrospectionException {
        // Guaranteed to succeed
        this((Class<T>) value.getClass());
        this.setValue(value);
    }

    /**
     * Returns the current viewed value.
     * 
     * @return the current viewed value
     */
    public T getValue() {
        return this.value;
    }

    /**
     * Sets the viewed value
     * 
     * @param value
     *            the new viewed value
     */
    public void setValue(final T value) {
        this.value = value;
        this.refresh();
    }

    private void refresh() {
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
