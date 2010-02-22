package com.bluespot.encryption;

import java.util.Set;

/**
 * A strategy to encrypt and decrypt messages.
 * 
 * @author Aaron Faanes
 * 
 * @param <D>
 *            the type of the decrypted data
 * @param <E>
 *            the type of the encrypted data
 */
public interface Encryption<D, E> {
    public Set<D> decrypt(E encrypted);

    public E encrypt(D decrypted);
}
