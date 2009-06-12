package com.bluespot.logging.tests;

import java.util.Collection;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.bluespot.logging.handlers.ListHandler;

public class ListHandlerTest {

    private LogRecord fineRecord;
    private LogRecord finestRecord;
    private ListHandler handler;

    @Before
    public void setUp() {
        this.handler = new ListHandler();
        this.fineRecord = new LogRecord(Level.FINE, "I'm fine.");
        this.finestRecord = new LogRecord(Level.FINEST, "This is the finest message.");
    }

    @Test
    public void testForLogLevelness() {
        this.handler.setLevel(Level.FINER);
        this.handler.publish(this.fineRecord);
        this.handler.publish(this.finestRecord);
        Assert.assertThat(this.handler.getRecords().size(), CoreMatchers.is(1));
    }

    @Test
    public void testForNonUniqueElementEntry() {
        this.handler.publish(this.fineRecord);
        this.handler.publish(this.fineRecord);
        Assert.assertThat(this.handler.getRecords().size(), CoreMatchers.is(2));
    }

    @Test
    public void testHandlerUsesMyList() {
        final List<LogRecord> crazyList = new Stack<LogRecord>();
        this.handler = new ListHandler(crazyList);
        Assert.assertThat(this.handler.getRecords(), CoreMatchers.is(crazyList));
    }

    @Test
    public void testSingleAddition() {
        this.handler.publish(this.fineRecord);
        final Collection<LogRecord> records = this.handler.getRecords();
        Assert.assertThat(records.size(), CoreMatchers.is(1));
        Assert.assertThat(records.contains(this.fineRecord), CoreMatchers.is(true));
    }

}
