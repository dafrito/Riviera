package com.bluespot.swing;

import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Dialogs {

    public static class CancelledException extends Exception {

        public static final String MESSAGE = "The operation was cancelled.";

        public CancelledException() {
            super(MESSAGE);
        }
    }

    public static File openFile() throws CancelledException {
        JFileChooser fileChooser = new JFileChooser(".");
        fileChooser.showOpenDialog(null);
        File file = fileChooser.getSelectedFile();
        if(file == null) 
            throw new CancelledException();
        return file;
    }
    
    public static File saveFile() throws CancelledException {
        JFileChooser fileChooser = new JFileChooser(".");
        fileChooser.showSaveDialog(null);
        File file = fileChooser.getSelectedFile();
        if(file == null) 
            throw new CancelledException();
        return file;
    }
    
    public static void info(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void warn(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
    }

    public static void error(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public static String getString(String message) throws CancelledException {
        while (true) {
            String input = JOptionPane.showInputDialog(message);
            if(input == null) {
                throw new CancelledException();
            }
            if(input.trim() == "") {
                Dialogs.error("Invalid input. Please try again.", "Error!");
                continue;
            }
            return input.trim();
        }
    }

    public static Integer getNumber(String message) throws CancelledException {
        while (true) {
            int number = -1;
            try {
                String numText = JOptionPane.showInputDialog(message);
                if(numText == null)
                    throw new CancelledException();
                number = Integer.parseInt(numText);
            } catch(NumberFormatException e) {
                Dialogs.error("The number was invalid!", "Invalid Number");
                continue;
            }
            if(number < 0) {
                Dialogs.error("The number was invalid!", "Invalid Number");
                continue;
            }
            return number;
        }
    }

    public static <T> T getSelection(String message, List<T> options, String title) throws CancelledException {
        int selection = JOptionPane.showOptionDialog(
            null,
            message,
            title,
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            options.toArray(),
            null);
        if(selection < 0)
            throw new CancelledException();
        return options.get(selection);
    }
}
