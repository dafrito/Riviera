package com.bluespot.logging.tests;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.logging.Logger;

import com.bluespot.logging.ListHandler;

import org.junit.Test;

public class LoggerTest {

    @Test
    public void testSomething() {
        ListHandler handler = new ListHandler();
        Logger parentLogger = Logger.getLogger("com.dafrito.rfe");
        parentLogger.addHandler(handler);
        Logger childLogger = Logger.getLogger("com.dafrito.rfe.gui");
        childLogger.info("No time");
        assertThat(handler.getRecords().size(), is(1));
    }
}
