/**
 * 
 */
package parsing;

public class Group<T extends Token<?>> {

	/**
	 * @param starts
	 * @return this group, so that method invocations can be chained
	 */
	public Group<T> start(String... starts) {
		// TODO Auto-generated method stub
		return this;
	}

	/**
	 * @param starts
	 * @return this group, so that method invocations can be chained
	 */
	public Group<T> start(char... starts) {
		// TODO Auto-generated method stub
		return this;
	}

	/**
	 * @param endings
	 * @return this group, so that method invocations can be chained
	 */
	public Group<T> end(String... endings) {
		// TODO Auto-generated method stub
		return this;
	}

	/**
	 * @param endings
	 * @return this group, so that method invocations can be chained
	 */
	public Group<T> end(char... endings) {
		// TODO Auto-generated method stub
		return this;
	}

	/**
	 * 
	 */
	public void drop() {
		// TODO Auto-generated method stub

	}

	/**
	 * @param string
	 */
	public void token(TokenFactory<? super T> string) {
		// TODO Auto-generated method stub

	}

}