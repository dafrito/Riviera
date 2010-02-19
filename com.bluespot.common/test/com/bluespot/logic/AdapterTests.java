package com.bluespot.logic;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.bluespot.logic.adapters.MapEntryAdapter;

public class AdapterTests {

    @Test
    public void testValidParseInteger() {
        assertThat(Adapters.parseInt().adapt("42"), is(42));
    }

    @Test
    public void testInalidParseInteger() {
        assertThat(Adapters.parseInt().adapt("SEVEN"), is(nullValue()));
    }

    @Test
    public void testCastingAdapter() {
        assertThat(Adapters.cast(Object.class, String.class).adapt("No time"), is("No time"));
    }

    @Test
    public void testBadCast() {
        assertThat(Adapters.cast(Object.class, Integer.class).adapt("No time"), is(nullValue()));
    }

    @Test
    public void testEntry() {
        final Map<String, Integer> nums = new HashMap<String, Integer>();
        nums.put("Foo", 42);
        final MapEntryAdapter<String, Integer> adapter = Adapters.entryFor("Foo");
        assertThat(adapter.adapt(nums), is(42));
    }

    @Test
    public void testEmptyEntry() {
        final Map<String, Integer> nums = new HashMap<String, Integer>();
        final MapEntryAdapter<String, Integer> adapter = Adapters.entryFor("Foo");
        assertThat(adapter.adapt(nums), is(nullValue()));
    }
}
