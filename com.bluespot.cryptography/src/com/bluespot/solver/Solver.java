package com.bluespot.solver;

/**
 * A strategy to decrypt messages.
 * 
 * @author Aaron Faanes
 * 
 * @param <D>
 *            the type of the decrypted data
 * @param <E>
 *            the type of the encrypted data
 */
public interface Solver<D, E> {
    public D decrypt(E encrypted);
}
