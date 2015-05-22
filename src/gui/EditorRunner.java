package gui;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import logic.actors.Actor;
import swing.Components;
import gui.logging.LogViewer;
import gui.script.ScriptEditor;
import logging.BufferedTreeLog;
import logging.Logs;
import logging.TreeLogServer;

/**
 * Create a new editing environment.
 * 
 * @author Aaron Faanes
 * 
 */
public class EditorRunner implements Runnable {

	public static void main(String[] args) {
        EditorRunner runner = new EditorRunner();

        if(args.length > 0) {
            try {
                // Try to parse the port number.
                runner.setLoggingPort(Integer.parseInt(args[0]));
            } catch(NumberFormatException ex) {
                System.err.println(ex.toString());
            }
        }

        // Invoke the runner.
		SwingUtilities.invokeLater(runner);
	}

	private LogViewer<Object> createLogViewer() {
		final LogViewer<Object> logFrame = new LogViewer<Object>();
		logFrame.setSize(1024, 768);
		logFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Components.center(logFrame);
		logFrame.setVisible(true);

		Logs.addListener(new Actor<BufferedTreeLog<Object>>() {

			@Override
			public void receive(BufferedTreeLog<Object> log) {
				logFrame.addLogPanel(log, Thread.currentThread().getName());
			}
		});

		return logFrame;
	}

	private ScriptEditor createScriptEditor() {
		ScriptEditor scriptFrame = new ScriptEditor();
		scriptFrame.setSize(800, 600);
		scriptFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Components.center(scriptFrame);
		scriptFrame.setVisible(true);
		return scriptFrame;
	}

	private void createLogServer(LogViewer<Object> viewer) {
		try {
			TreeLogServer server = new TreeLogServer(loggingPort());
			server.setSink(viewer);

			new Thread(server).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		if (!SwingUtilities.isEventDispatchThread()) {
			throw new IllegalStateException("Runner must be run from EDT");
		}
		Components.LookAndFeel.GTK.activate();

		LogViewer<Object> viewer = createLogViewer();

		createLogServer(viewer);
		viewer.setTitle("Log Viewer on Port " + loggingPort());

		//createScriptEditor();
	}

    private int _loggingPort = 28122;
    public void setLoggingPort(int loggingPort)
    {
        _loggingPort = loggingPort;
    }
    public int loggingPort()
    {
        return _loggingPort;
    }
}
