package com.bluespot.logging.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import com.bluespot.logging.CallStackHandler;
import com.bluespot.tree.PrintVisitor;
import com.bluespot.tree.Tree;
import com.bluespot.tree.TreeWalker;

public class CallStackHandlerTest {

    protected Logger logger;
    protected TreeWalker<LogRecord> builder;
    
    protected class Bar {
        private String name;
        
        private final Logger barLogger = CallStackHandlerTest.this.getLogger();
        
        public Bar(String name) {
            this.setName(name);
        }

        public void setName(String name) {
            this.barLogger.info("Setting bar name: " + name);
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
    
    protected class Foo {
        List<Bar> bars = new ArrayList<Bar>();
        
        private final Logger fooLogger = CallStackHandlerTest.this.getLogger();

        public Foo() {
            this.fooLogger.info("Created Foo");
        }
        
        public void addBar(String name) {
            this.fooLogger.entering("com.dafrito.tests.CallStackHandlerTest$Foo", "addBar");
            this.fooLogger.info("Adding bar with name: " + name);
            if(name.length() > 0)
                this.addBar(name.substring(0, name.length() - 1));
            this.bars.add(new Bar(name));
            this.fooLogger.exiting("com.dafrito.tests.CallStackHandlerTest$Foo", "addBar");
        }
        
        public void addBars(String... names) {
            this.fooLogger.info("Adding lots of bars: " + names.length);
            for(String name : names) {
                this.fooLogger.info("Adding this name: " + name);
                this.addBar(name);
            }
        }
    }
    
    public Logger getLogger() {
        return this.logger;
    }
    
    @Before
    public void init() {
        this.builder = new TreeWalker<LogRecord>(new Tree<LogRecord>(null));
        this.logger = Logger.getAnonymousLogger();
        this.logger.setLevel(Level.FINEST);
        this.logger.addHandler(new CallStackHandler(this.builder));
    }
    
    public void runSimplestOperation() {
        Foo foo = new Foo();
        foo.addBars("A", "B", "C", "D");
    }
    
    public void runRandomOperations() {
        Foo foo = new Foo();
        foo.addBar("No time");
        foo.addBars("A", "B", "C", "D");
        Foo anotherFoo = new Foo();
        anotherFoo.addBar("Cheese!");
        new Bar("Crumpet.");
    }
    
    @Test
    public void testSanity() {
        assertThat(this.getClass().getName(), is("com.dafrito.tests.CallStackHandlerTest"));
    }
    
    @Test
    public void testLoggingStuff() {
        this.runSimplestOperation();
        Tree<LogRecord> tree = this.builder.getCurrentNode();
        assertThat(tree.size(), is(not(0)));
        this.builder.getCurrentNode().visit(new PrintVisitor<LogRecord>() {

            @Override
            public String toString(LogRecord record) {
                return record != null ? record.getSourceMethodName() + ": " + record.getMessage() : "null";
            }
        });
    }
}
