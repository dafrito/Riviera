package com.bluespot.logic;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.swing.JTextField;

import org.junit.Test;

import com.bluespot.logic.adapters.ParsedIntegerAdapter;
import com.bluespot.logic.values.BufferedValue;
import com.bluespot.logic.values.MutableValue;
import com.bluespot.logic.values.Value;

public class ValueTests {

    @Test
    public void testAdaptedValue() {
        final String string = "No time";
        assertThat(string.length(), is(7));
        final MutableValue<String> value = Values.value(string);
        final Value<Integer> num = Values.adapt(value, Adapters.stringLength());
        assertThat(num.get(), is(string.length()));
        assertThat(Values.adapt(num, Adapters.stringValue()).get(), is("7"));
        value.set("Base");
        assertThat(Values.adapt(num, Adapters.stringValue()).get(), is("4"));
    }

    @Test
    public void testTextComponent() {
        final JTextField field = new JTextField("42");
        final Value<String> componentValue = Values.adapt(field, Adapters.componentText());
        final Value<Integer> num = Values.adapt(componentValue, new ParsedIntegerAdapter());
        assertThat(num.get(), is(42));
    }

    @Test
    public void testBufferedValue() {
        final MutableValue<String> value = Values.value("No time");
        final BufferedValue<String> buffered = new BufferedValue<String>(value);
        assertThat(buffered.get(), is(value.get()));
        value.set("Base");
        assertThat(buffered.get(), is("No time"));
        buffered.refresh();
        assertThat(buffered.get(), is("Base"));
    }
}
