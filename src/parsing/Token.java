/**
 * 
 */
package parsing;

/**
 * A parsed token of data.
 * 
 * @author Aaron Faanes
 * @param <V>
 *            the type of {@link TokenVisitor} that can visit tokens of this
 *            type.
 * @see Parser
 * @see TokenVisitor
 * @see TokenFactory
 */
public interface Token<V extends TokenVisitor> {
	public void accept(V visitor);
}
