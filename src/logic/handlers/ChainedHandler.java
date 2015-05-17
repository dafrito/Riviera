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
package logic.handlers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aaron Faanes
 * @param <T>
 *            the type of handled value
 * 
 */
public class ChainedHandler<T> implements Handler<T> {

	public static enum Order {
		LAST_TO_FIRST,
		FIRST_TO_LAST
	};

	private List<Handler<? super T>> handlers = new ArrayList<Handler<? super T>>();

	public void addHandler(Handler<? super T> handler) {
		handlers.add(handler);
	}

	public void removeHandler(Handler<? super T> handler) {
		handlers.remove(handler);
	}

	private Order order = Order.LAST_TO_FIRST;

	public void setOrder(Order order) {
		this.order = order;
	}

	public Order getOrder() {
		return this.order;
	}

	@Override
	public boolean handle(T value) {
		if (order == Order.FIRST_TO_LAST) {
			for (Handler<? super T> handler : handlers) {
				if (handler.handle(value)) {
					return true;
				}
			}
		} else {
			for (int i = handlers.size() - 1; i >= 0; --i) {
				Handler<? super T> handler = handlers.get(i);
				if (handler.handle(value)) {
					return true;
				}
			}
		}
		return false;
	}
}
