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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gui.logging.LogViewer;

/**
 * @author Aaron Faanes
 * 
 */
public class TreeLogServer implements Runnable {

	private ServerSocket serverSocket;
	private LogViewer<? super String> sink;

	public TreeLogServer(int port) throws IOException {
		serverSocket = new ServerSocket(port);
	}
	private void serve(Socket connection) throws IOException {
		if (this.sink == null) {
			return;
		}
		BufferedTreeLog<String> log = new BufferedTreeLog<>();
        String name = String.format("%s:%d", connection.getInetAddress().getHostAddress(), connection.getPort());
		sink.addLogPanel(log, name);
        new Thread(new StreamLog(log, connection.getInputStream(), name, v->v)).start();
	}

	@Override
	public void run() {
		if (this.sink == null) {
			return;
		}
		try {
			while (true) {
				serve(serverSocket.accept());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setSink(LogViewer<? super String> sink) {
		this.sink = sink;
	}
}
