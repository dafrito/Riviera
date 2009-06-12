package com.bluespot.logging.example;

import java.awt.event.MouseEvent;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListModel;
import javax.swing.event.MouseInputAdapter;

import com.bluespot.collections.observable.list.ObservableList;
import com.bluespot.demonstration.AbstractDemonstration;
import com.bluespot.demonstration.Runner;
import com.bluespot.logging.handlers.ListHandler;

/**
 * Demonstrates {@link ListHandler} used in conjunction with an
 * {@link ObservableList}.
 * 
 * @author Aaron Faanes
 * 
 */
public final class ListHandlerDemonstration extends AbstractDemonstration {

    @Override
    public void initializeFrame(final JFrame frame) {
        frame.setSize(400, 400);

        final JSplitPane splitPane = new JSplitPane();
        frame.setContentPane(splitPane);

        final Logger logger = Logger.getLogger(this.getClass().getPackage().getName());

        final JList list = new JList(this.createLoggingListModel(logger));

        splitPane.setLeftComponent(new JScrollPane(list));

        final JPanel panel = new JPanel();
        panel.addMouseListener(new MouseInputAdapter() {

            @Override
            public void mouseClicked(final MouseEvent event) {
                logger.info(event.toString());
            }

        });
        splitPane.setRightComponent(panel);
    }

    private ListModel createLoggingListModel(final Logger associatedLogger) {
        final ObservableList<LogRecord> listModel = new ObservableList<LogRecord>();
        final Handler handler = new ListHandler(listModel);
        associatedLogger.addHandler(handler);
        return listModel;
    }

    /**
     * Creates a new list handler demonstration.
     * 
     * @param args
     *            unused
     */
    public static void main(final String[] args) {
        Runner.run(new ListHandlerDemonstration(), true);
    }

}
