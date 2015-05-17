/**
 * 
 */
package parsing;

/**
 * A factory that creates {@link Token} objects.
 * 
 * @author Aaron Faanes
 * @param <T>
 *            the type of {@link Token} created by this factory
 * 
 */
public interface TokenFactory<T extends Token<?>> {
	public T create(String content);
}
