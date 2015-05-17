/**
 * 
 */
package script.parsing.tokens;

import parsing.Token;
import script.parsing.ScriptElement;

/**
 * A RiffScript {@link Token}. This is used to ease the transition from
 * {@link ScriptElement} since this class's visitor type can be found at
 * runtime.
 * 
 * @author Aaron Faanes
 * 
 */
public interface RiffToken extends Token<RiffTokenVisitor> {

}
