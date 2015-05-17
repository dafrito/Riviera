/**
 * 
 */
package parsing;

/**
 * A visitor of {@link Token} objects. Each hierarchy of tokens should have a
 * corresponding visitor, with methods for each logical type.
 * 
 * @author Aaron Faanes
 * @see Token
 * @see TokenFactory
 */
public interface TokenVisitor {
	public void visitUnparsed(String unparsed);
}
