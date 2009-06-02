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
    private T value;
    private Map<String, JTextField> fieldMap = new HashMap<String, JTextField>();

    public BeanView(Class<T> viewedClass) throws IntrospectionException {
        
        GroupLayoutBuilder builder = new GroupLayoutBuilder(this);
        
        this.beanInfo = Introspector.getBeanInfo(viewedClass);
        for(PropertyDescriptor pd : this.beanInfo.getPropertyDescriptors()) {
            PropertyEditor editor = PropertyEditorManager.findEditor(pd.getPropertyType());
            if(editor == null) {
                continue;
            }
            JTextField textField = new JTextField();
            textField.setEditable(false);
            textField.setText(editor.getAsText());
            this.fieldMap.put(pd.getName(), textField);
            builder.addLabeledPair(pd.getDisplayName(), textField);
        }

        builder.build();
    }

    @SuppressWarnings("unchecked")
    public BeanView(T value) throws IntrospectionException {
        this((Class<T>)value.getClass());
        this.setValue(value);
    }

    @Override
    protected void printComponent(Graphics g) {
        super.printComponent(g);
        
    }
    
    protected void refresh() {
        if(this.value == null) {
            this.setEnabled(false);
            return;
        }
        this.setEnabled(true);
        for(PropertyDescriptor pd : this.beanInfo.getPropertyDescriptors()) {
            JTextField field = this.fieldMap.get(pd.getName());
            PropertyEditor editor = pd.createPropertyEditor(this.getValue());
            if(editor == null)
                editor = PropertyEditorManager.findEditor(pd.getPropertyType());
            if(editor == null)
                continue;
            Object propertyValue = null;
            try {
               propertyValue = pd.getReadMethod().invoke(this.getValue()); 
            } catch(Exception e) {
                // Just continue
                continue;
            }
            field.setText(propertyValue != null ? propertyValue.toString() : "null");
        }
    }

    public void setValue(T value) {
        this.value = value;
        this.refresh();
    }

    public T getValue() {
        return this.value;
    }

}
