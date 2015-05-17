package logic;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import logic.actors.Actors;
import logic.actors.GuardedActor;
import logic.predicates.Predicates;

public class GuardedActorTests {

	@Test
	public void testSentinel() {

		final List<String> strings = new ArrayList<String>();

		final GuardedActor<String> sentinel = new GuardedActor<String>(Predicates.lowerCase(), Actors.addTo(strings));

		sentinel.receive("no time");
		sentinel.receive("NO TIME");

		assertThat(strings, is(Arrays.asList("no time")));
	}
}
