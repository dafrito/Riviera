package com.bluespot.solver;

import java.util.Set;

/**
 * A strategy to decrypt data.
 * 
 * @author Aaron Faanes
 * 
 * @param <D>
 *            the type of the decrypted data
 * @param <E>
 *            the type of the encrypted data
 */
public interface Solver<E, D> {

	public Set<String> solve(E encrypted);

	public void addSolverListener(SolverListener<? super D> listener);
}
