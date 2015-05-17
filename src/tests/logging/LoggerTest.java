package logging;

import java.util.logging.Logger;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import logging.handlers.ListHandler;

public class LoggerTest {

	@Test
	public void testSomething() {
		final ListHandler handler = new ListHandler();
		final Logger parentLogger = Logger.getLogger("com.dafrito.rfe");
		parentLogger.addHandler(handler);
		final Logger childLogger = Logger.getLogger("gui");
		childLogger.info("No time");
		Assert.assertThat(handler.getRecords().size(), CoreMatchers.is(1));
	}
}
