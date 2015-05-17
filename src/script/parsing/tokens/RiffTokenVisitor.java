/**
 * 
 */
package script.parsing.tokens;

import parsing.TokenVisitor;
import script.parsing.ScriptGroup;
import script.parsing.ScriptKeywordType;
import script.parsing.ScriptOperatorType;

/**
 * @author Aaron Faanes
 * 
 */
public interface RiffTokenVisitor extends TokenVisitor {

	public void enterContext(Cursor cursor);

	public void visitGroup(ScriptGroup group);

	public void visitKeyword(ScriptKeywordType keyword);

	public void visitOperator(ScriptOperatorType operator);

	public void exitContext(Cursor cursor);
}
