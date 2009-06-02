package com.bluespot.util.tests;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.bluespot.util.Ranges;


public class RangeTest {
    
    @Test
    public void testDifferentStep() {
        int count = 0;
        int num = 0;
        for(int i : Ranges.range(0, 10, 2)) {
            count++;
            num = i;
        }
        assertThat(count, is(5));
        assertThat(num, is(8));
    }
    
    @Test
    public void testBiggerStep() {
        int count = 0;
        int num = 0;
        for(int i : Ranges.range(0, 10, 3)) {
            count++;
            num = i;
        }
        assertThat(count, is(4));
        assertThat(num, is(9));
    }
    
    @Test
    public void testDecrementRange() {
        int count = 0;
        int num = 0;
        for(int i : Ranges.range(10, 0)) {
            count++;
            num = i;
        }
        assertThat(count, is(10));
        assertThat(num, is(1));
    }
    
    
    @Test
    public void testIntegerRange() {
        int count = 0;
        int num = 0;
        for(int i : Ranges.range(10)) {
            count++;
            num = i;
        }
        assertThat(count, is(10));
        assertThat(num, is(9));
    }
}
