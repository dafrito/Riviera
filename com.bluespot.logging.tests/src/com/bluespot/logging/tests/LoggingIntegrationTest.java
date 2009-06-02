package com.bluespot.logging.tests;

import static com.bluespot.logging.Logging.log;
import static com.bluespot.logging.Logging.logEntry;
import static com.bluespot.logging.Logging.logExit;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bluespot.logging.CallStackHandler;
import com.bluespot.tree.PrintVisitor;
import com.bluespot.tree.Tree;

public class LoggingIntegrationTest {

    public static class Bar {
        private String name;
        
        public Bar(String name) {
            log("Creating bar!");
            this.setName(name);
        }

        public void setName(String name) {
            log("Setting bar name: " + name);
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
    
    public static class Foo {
        List<Bar> bars = new ArrayList<Bar>();

        public Foo() {
            log("Created Foo");
        }
        
        public void addBar(String name) {
            logEntry();
            log("Adding bar with name: %s", name);
            this.bars.add(new Bar(name));
            logExit();
        }
        
        public void addBars(String... names) {
            log("Adding lots of bars: " + names.length);
            for(String name : names) {
                log("Adding this name: " + name);
                this.addBar(name);
            }
        }
    }

    private CallStackHandler handler;
    private Tree<LogRecord> tree;
    private Logger logger = Logger.getLogger("com.dafrito.tests");
    
    @Before
    public void init() {
        this.tree = new Tree<LogRecord>(null);
        this.handler = new CallStackHandler(this.tree.walker());
        this.logger.addHandler(this.handler);
    }
    
    @After
    public void cleanUp() {
        this.logger.removeHandler(this.handler);
    }
    
    public static void runSimplestOperation() {
        Foo foo = new Foo();
        foo.addBars("A", "B", "C", "D");
    }
    
    public static void runRandomOperations() {
        logEntry();
        log("Running random operations");
        Foo foo = new Foo();
        foo.addBar("No time");
        foo.addBars("A", "B", "C", "D");
        Foo anotherFoo = new Foo();
        anotherFoo.addBar("Cheese!");
        new Bar("Crumpet.");
        logExit();
    }
    
    @Test
    public void testLoggingStuff() {
        LoggingIntegrationTest.runSimplestOperation();
        assertTrue(this.tree.size() > 0);
        this.tree.visit(new PrintVisitor<LogRecord>() {

            @Override
            public String toString(LogRecord record) {
                return record != null ? record.getSourceMethodName() + ": " + record.getMessage() : "null";
            }
        });
    }
}
