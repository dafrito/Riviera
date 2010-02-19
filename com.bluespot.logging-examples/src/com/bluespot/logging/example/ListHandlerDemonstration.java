package com.bluespot.logging.example;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListModel;
import javax.swing.event.MouseInputAdapter;

import com.bluespot.collections.observable.list.ObservableList;
import com.bluespot.demonstration.Demonstration;
import com.bluespot.logging.handlers.ListHandler;

/**
 * Demonstrates {@link ListHandler} used in conjunction with an
 * {@link ObservableList}.
 * 
 * @author Aaron Faanes
 * 
 */
public final class ListHandlerDemonstration extends Demonstration {

    @Override
    protected JComponent newContentPane() {
        final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setPreferredSize(new Dimension(400, 400));

        final Logger logger = Logger.getLogger(this.getClass().getPackage().getName());

        final JList list = new JList(this.createLoggingListModel(logger));

        splitPane.setLeftComponent(new JScrollPane(list));

        final JPanel panel = new JPanel();
        panel.add(new JLabel("Click here to log mouse events!"));
        panel.addMouseListener(new MouseInputAdapter() {

            @Override
            public void mouseClicked(final MouseEvent event) {
                logger.info(event.toString());
            }

        });
        splitPane.setRightComponent(panel);
        splitPane.setResizeWeight(.8);
        return splitPane;
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
        Demonstration.launch(ListHandlerDemonstration.class);
    }

}
