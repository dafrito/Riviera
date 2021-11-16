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
package gui.logging;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import logging.BufferedTreeLog;
import logging.LogParser;
import logging.StreamLog;

import logic.adapters.Adapter;

/**
 * @author Aaron Faanes
 * @param <Message>
 *            the type of log message
 * 
 */
public class LogViewer<Message> extends JFrame {

	/**
	 * The list of {@link LogPanel}s that are shown by this viewer.
	 */
	private final JTabbedPane logPanelTabs = new JTabbedPane();

	private final JMenuBar menuBar = new JMenuBar();

	final Map<String, List<LogPanel<Message>>> filteredOutputMap = new HashMap<String, List<LogPanel<Message>>>();

	public LogViewer(Adapter<String, Message> messageParser) {
		super();

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(this.logPanelTabs);

		this.setJMenuBar(this.menuBar);

		JMenu listenerMenu = new JMenu("Listener");
		this.menuBar.add(listenerMenu);
		listenerMenu.setMnemonic('L');

		JMenuItem openFile = new JMenuItem("Open log...", 'O');
		openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		openFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(LogViewer.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        File selectedFile = fileChooser.getSelectedFile();
                        var log = new BufferedTreeLog<Message>();
                        var name = selectedFile.getName();
                        new Thread(new StreamLog(log, new FileInputStream(selectedFile), name, v->v)).start();
                        addLogPanel(log, name);
                    } catch (FileNotFoundException ex) {
                        JOptionPane.showMessageDialog(LogViewer.this, ex.toString());
                    }
                }
			}
		});
		listenerMenu.add(openFile);

		JMenuItem renameTab = new JMenuItem("Rename Tab...", 'N');
		renameTab.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Object text = JOptionPane.showInputDialog(
						null,
						"Insert new output name",
						"Rename Output",
						JOptionPane.QUESTION_MESSAGE,
						null,
						null,
						logPanelTabs.getTitleAt(logPanelTabs.getSelectedIndex())
						);
				if (text != null) {
					logPanelTabs.setTitleAt(logPanelTabs.getSelectedIndex(), text.toString());
				}
			}
		});
		listenerMenu.add(renameTab);

		JMenuItem removeTab = new JMenuItem("Remove Tab", 'R');
		removeTab.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
		removeTab.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeLogPanel(getSelectedLogPanel());
			}
		});
		listenerMenu.add(removeTab);

		JMenuItem quit = new JMenuItem("Quit", 'Q');
		quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		quit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                dispatchEvent(new WindowEvent(LogViewer.this, WindowEvent.WINDOW_CLOSING));
			}
		});
		listenerMenu.add(quit);
	}

	@SuppressWarnings("unchecked")
	public LogPanel<? extends Message> getSelectedLogPanel() {
		return (LogPanel<? extends Message>) logPanelTabs.getSelectedComponent();
	}

	public void setSelectedLogPanel(LogPanel<? extends Message> panel) {
		logPanelTabs.setSelectedComponent(panel);
	}

	public JTabbedPane tabs() {
		return logPanelTabs;
	}

	public void addLogPanel(BufferedTreeLog<? extends Message> log, String name) {
		addLogPanel(new LogPanel<Message>(this, log, name));
	}

	/**
	 * @param panel
	 *            the panel to add
	 * @param name
	 *            the name of the new panel
	 */
	public void addLogPanel(LogPanel<Message> panel) {
		if (panel == null) {
			throw new NullPointerException("Panel must not be null");
		}
		logPanelTabs.add(panel);
	}

	public void removeLogPanel(LogPanel<? extends Message> panel) {
		if (panel == null) {
			return;
		}
		panel.prepareToRemove();
		logPanelTabs.remove(panel);
	}

	private static final long serialVersionUID = 4926830382755122234L;
}
