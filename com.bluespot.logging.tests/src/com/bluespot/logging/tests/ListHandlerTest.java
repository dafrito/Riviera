package com.bluespot.logging.tests;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Collection;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.junit.Before;
import org.junit.Test;

import com.bluespot.logging.ListHandler;

public class ListHandlerTest {
    
    private ListHandler handler;
    private LogRecord fineRecord;
    private LogRecord finestRecord;
    
    @Before
    public void initialize() {
        this.handler = new ListHandler();
        this.fineRecord = new LogRecord(Level.FINE, "I'm fine.");
        this.finestRecord = new LogRecord(Level.FINEST, "This is the finest message.");
    }
    
    @Test
    public void testSingleAddition() {
        this.handler.publish(this.fineRecord);
        Collection<LogRecord> records = this.handler.getRecords();
        assertThat(records.size(), is(1));
        assertThat(records.contains(this.fineRecord), is(true));
    }
    
    @Test
    public void testHandlerUsesMyList() {
        List<LogRecord> crazyList = new Stack<LogRecord>();
        this.handler = new ListHandler(crazyList);
        assertThat(this.handler.getRecords(), is(crazyList));
    }
    
    @Test
    public void testForLogLevelness() {
        this.handler.setLevel(Level.FINER);
        this.handler.publish(this.fineRecord);
        this.handler.publish(this.finestRecord);
        assertThat(this.handler.getRecords().size(), is(1));
    }
    
    @Test
    public void testForNonUniqueElementEntry() {
        this.handler.publish(this.fineRecord);
        this.handler.publish(this.fineRecord);
        assertThat(this.handler.getRecords().size(), is(2));
    }
    
}

