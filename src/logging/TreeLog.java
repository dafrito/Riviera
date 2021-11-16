/**
 * Copyright (c) 2013 Aaron Faanes
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package logging;

/**
 * A hierarchical log of {@link LogMessage} events.
 * 
 * @author Aaron Faanes
 * @param <T>
 *            the type of message
 */
public interface TreeLog<T> {

	/**
	 * Record the specified log message.
	 * 
	 * @param message
	 *            the message to log. May be null.
	 */
	void log(LogMessage<? extends T> message);

	/**
	 * Enter a given scope. Subsequent log messages occur within that scope.
	 * Implementations are free to ignore this scope, which would result in a
	 * flat log.
	 * 
	 * @param scope
	 *            the name of the scope. For example, "Reading foo.txt" or
	 *            "Parsing 'PRE' element".
	 * @param scopeGroup
	 *            the name of the scope group. For example, "File reads" or
	 *            "Element parsing". Implementations may choose to collapse
	 *            scopes that have the same scope group into a single scope
	 *            group node, so take care to use a reasonable plural name.
	 */
	void enter(LogMessage<? extends T> scope);

    /**
     * Logs metadata about the logging session, or the subject of the session.
     * Metadata is assumed to be in a key-value format, where they message's
     * category is the key, and its message is the value.
     *
     * @param metadata The message that contains metadata
     */
	void metadata(LogMessage<? extends T> metadata);

	/**
	 * Leave the current scope.
	 */
	void leave();

	/**
	 * Indicate that all open scopes should be immediately closed.
	 * <p>
	 * This is useful if an error has occurred that will disrupt normal logging.
	 * Without reset, new root output would remain inside the node that caused
	 * the initial error.
	 */
	void reset();
}
