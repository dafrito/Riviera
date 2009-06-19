package com.bluespot.forms.model.input;

import java.util.HashMap;
import java.util.Map;

import com.bluespot.forms.property.DefaultPropertySheet;

public class AggregateInputMethod implements InputMethod<DefaultPropertySheet> {

    private final Map<String, InputMethod<?>> inputMethods = new HashMap<String, InputMethod<?>>();

    public void addInputMethod(final String name, final InputMethod<?> inputMethod) {
        this.inputMethods.put(name, inputMethod);
    }

    public void clearInputMethods() {
        this.inputMethods.clear();
    }

    public DefaultPropertySheet getValue() {
        final DefaultPropertySheet properties = new DefaultPropertySheet();
        for (final Map.Entry<String, InputMethod<?>> entry : this.inputMethods.entrySet()) {
            properties.setProperty(entry.getKey(), entry.getValue());
        }
        return properties.freeze();
    }

    public InputMethod<?> removeInputMethod(final String name) {
        return this.inputMethods.remove(name);
    }

}
