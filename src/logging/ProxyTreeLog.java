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
 * @author Aaron Faanes
 * @param <Message>
 *            the type of logged value
 * 
 */
public class ProxyTreeLog<Message> implements TreeLog<Message> {

	private TreeLog<? super Message> sink;

	public TreeLog<? super Message> getSink() {
		if (sink == null) {
			return NoopTreeLog.instance();
		}
		return sink;
	}

	public void setSink(TreeLog<? super Message> sink) {
		this.sink = sink;
	}

	@Override
	public void log(LogMessage<? extends Message> message) {
		sink.log(message);
	}

	@Override
	public void metadata(LogMessage<? extends Message> message) {
		sink.metadata(message);
	}

	@Override
	public void enter(LogMessage<? extends Message> scope) {
		sink.enter(scope);
	}

	@Override
	public void leave() {
		sink.leave();
	}

	@Override
	public void reset() {
		sink.reset();
	}
}
