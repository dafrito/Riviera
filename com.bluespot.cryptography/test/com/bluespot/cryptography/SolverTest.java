package com.bluespot.cryptography;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Ignore;
import org.junit.Test;

import com.bluespot.dictionary.Dictionary;
import com.bluespot.solver.Solver;
import com.bluespot.solver.SolverListener;
import com.bluespot.solver.substitution.SubstitutionSolver;

public class SolverTest {

	public static final String ANDY = "Ev oui lyh nayt drec, oui ryja duu silr desa uh ouin ryhtc. ku kad y zup";

	public static final String BRETT = "BRETT IS A FLAMING HOMOSEXUAL AND HIS FACE IS ALSO GAY BUT HE IS MOSTLY JUST GAY";

	public static final String UNDERLING = "BICYCLES EAT LARGE COWS EVERY FUCKING DAY";

	public static final String BOSS = "ISS NVVK DIPXYWA PIT AVSUY QIAOP PWZEHVNWIEDZ. CDYT ZVM LOTK HDY AVSMHOVT HV HDOA HYFH, ZVM COSS QY IQSY HV NYH HDY ITACYW, CDOPD OA IKMGQWIHY.";

	public static final String AARON = "Trlz cdg jrd xrzgdz. Mphz hz ice mrr oksp jkq.";

	@Test
	public void testListener() {
		final Dictionary dict = new Dictionary("word");
		Solver<String, String> solver = new SubstitutionSolver(dict);

		final Set<String> matches = new HashSet<String>();
		final AtomicBoolean flag = new AtomicBoolean();

		solver.addSolverListener(new SolverListener<String>() {

			@Override
			public void finished() {
				flag.set(true);
			}

			@Override
			public void onSolution(String result) {
				matches.add(result);
			}
		});
		solver.solve("word");
		assertThat(matches.size(), is(1));
		assertTrue(matches.contains("word"));
		assertTrue(flag.get());
	}

	@Test
	public void testDictionaryMakesLettersLowercase() {
		Dictionary dict = new Dictionary("jobs", "JOBS");
		assertThat(dict.size(), is(1));
		assertTrue(dict.contains("jobs"));
	}

	@Test
	public void testSimplestMatch() {
		final Dictionary dict = new Dictionary("word");
		final Set<String> matches = new SubstitutionSolver(dict).solve("word");
		assertThat(matches.size(), is(1));
		assertTrue(matches.contains("word"));
	}

	@Test
	public void testFourWords() {
		Dictionary dict = new Dictionary("jobs are for losers".split(" "));
		Set<String> matches = new SubstitutionSolver(dict).solve("jobs are for losers");
		assertThat(matches.size(), is(1));
		assertTrue(matches.contains("jobs are for losers"));
	}

	@Test
	public void testFourWordsWithPunctuation() {
		Dictionary dict = new Dictionary("jobs are for losers".split(" "));
		Set<String> matches = new SubstitutionSolver(dict).solve("jobs are for losers.");
		assertThat(matches.size(), is(1));
		assertTrue(matches.contains("jobs are for losers."));
	}

	@Ignore
	@Test
	public void testFourWordsWithDifficultEncryption() {
		Set<String> matches = new SubstitutionSolver().solve("Mphz hz ice mrr oksp jkq.");
		assertTrue(matches.contains("this is way more fun."));
	}

	@Test
	public void testSimplestEncryptedWord() {
		Dictionary dict = new Dictionary("jobs");
		Set<String> matches = new SubstitutionSolver(dict).solve("abcd");
		assertThat(matches.size(), is(1));
		assertTrue(matches.contains("jobs"));
	}

	@Test
	public void testDictionaryIgnoresCapitalization() {
		Dictionary dict = new Dictionary("jobs");
		assertTrue(dict.contains("JOBS"));
	}

}
