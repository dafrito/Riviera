package swing;

import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * Collection of utility methods dealing with creating dialogs.
 * 
 * @author Aaron Faanes
 * 
 */
public final class Dialogs {

	private Dialogs() {
		// Suppress default constructor to ensure non-instantiability.
		throw new AssertionError("Instantiation not allowed");
	}

	/**
	 * An exception indicating that some operation was canceled and not
	 * completed.
	 * 
	 * @author Aaron Faanes
	 * 
	 */
	public static class CancelledException extends Exception {

		private static final long serialVersionUID = 6805783603685032905L;

		private static final String MESSAGE = "The operation was cancelled.";

		/**
		 * Constructs a new {@link CancelledException}.
		 */
		public CancelledException() {
			super(CancelledException.MESSAGE);
		}

	}

	/**
	 * Displays a dialog that requests an {@code int} from the user using the
	 * specified message. The number will be parsed using
	 * {@link Integer#parseInt(String)}. The dialog will display an error if the
	 * input was invalid, and loop until either:
	 * <ul>
	 * <li>A valid {@code int} has been received, or</li>
	 * <li>The user explicitly closes the dialog</li>
	 * </ul>
	 * If the dialog was closed, a {@link CancelledException} is thrown.
	 * 
	 * @param message
	 *            the message to display to the user
	 * @return an {@code int} value that was parsed from the user's input
	 * @throws CancelledException
	 *             if the user explicitly closed the dialog
	 */
	public static int getInteger(final String message) throws CancelledException {
		while (true) {
			try {
				final String numText = JOptionPane.showInputDialog(message);
				if (numText == null) {
					throw new CancelledException();
				}
				return Integer.parseInt(numText);
			} catch (final NumberFormatException e) {
				Dialogs.error("The number was invalid.", "Invalid Input");
			}
		}
	}

	/**
	 * @see #getString(String, String)
	 */
	@SuppressWarnings("javadoc")
	public static String getString(final String message) throws CancelledException {
		return Dialogs.getString(message, null);
	}

	/**
	 * Displays a dialog that requests a {@code String} from the user using the
	 * specified message. The entered string must contain at least one
	 * non-whitespace character. The dialog will display an error if the input
	 * was invalid, and loop until either:
	 * <ul>
	 * <li>A valid {@code String} has been received, or</li>
	 * <li>The user explicitly closes the dialog</li>
	 * </ul>
	 * If the dialog was closed, a {@link CancelledException} is thrown.
	 * 
	 * @param message
	 *            the message to display to the user
	 * @param defaultValue
	 *            the default value shown
	 * @return a {@code String} value that was parsed from the user's input. The
	 *         returned value will be trimmed
	 * @throws CancelledException
	 *             if the user explicitly closed the dialog
	 */
	public static String getString(final String message, final String defaultValue) throws CancelledException {
		while (true) {
			final String input = JOptionPane.showInputDialog(message, defaultValue);
			if (input == null) {
				throw new CancelledException();
			}
			if (input.trim().equals("")) {
				Dialogs.error("Invalid input. Please try again.", "Invalid Input");
				continue;
			}
			return input.trim();
		}
	}

	/**
	 * Displays a selection dialog using the specified options. The user must
	 * select one of the specified options and the selected option will be
	 * returned.
	 * 
	 * @param <T>
	 *            the type of value to select
	 * @param message
	 *            the message to show to the user
	 * @param options
	 *            the options available for selection
	 * @param title
	 *            the title of the dialog
	 * @return the selected option
	 * @throws CancelledException
	 *             if the user explicitly closed the dialog
	 */
	public static <T> T getSelection(final String message, final List<T> options, final String title)
			throws CancelledException {
		final int selection = JOptionPane.showOptionDialog(null, message, title, JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, options.toArray(), null);
		if (selection < 0) {
			throw new CancelledException();
		}
		return options.get(selection);
	}

	/**
	 * Displays an open dialog that requests for the user to select a file.
	 * 
	 * @return the selected {@code File} object
	 * @throws CancelledException
	 *             if the user explicitly closes the dialog
	 * @see JFileChooser#showOpenDialog(java.awt.Component)
	 */
	public static File openFile() throws CancelledException {
		final JFileChooser fileChooser = new JFileChooser(".");
		fileChooser.showOpenDialog(null);
		final File file = fileChooser.getSelectedFile();
		if (file == null) {
			throw new CancelledException();
		}
		return file;
	}

	/**
	 * Displays a save dialog that requests for the user to select a file.
	 * 
	 * @return the selected {@code File} object
	 * @throws CancelledException
	 *             if the user explicitly closes the dialog
	 * @see JFileChooser#showSaveDialog(java.awt.Component)
	 */
	public static File saveFile() throws CancelledException {
		final JFileChooser fileChooser = new JFileChooser(".");
		fileChooser.showSaveDialog(null);
		final File file = fileChooser.getSelectedFile();
		if (file == null) {
			throw new CancelledException();
		}
		return file;
	}

	/**
	 * Displays a information dialog with the specified message and title.
	 * 
	 * @param message
	 *            the message to use in the displayed dialog
	 * @param title
	 *            the title of the displayed dialog
	 * @see JOptionPane#showMessageDialog(java.awt.Component, Object, String,
	 *      int)
	 * @see JOptionPane#INFORMATION_MESSAGE
	 */
	public static void info(final String message, final String title) {
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Displays a warning dialog with the specified message and title.
	 * 
	 * @param message
	 *            the message to use in the displayed dialog
	 * @param title
	 *            the title of the displayed dialog
	 * @see JOptionPane#showMessageDialog(java.awt.Component, Object, String,
	 *      int)
	 * @see JOptionPane#WARNING_MESSAGE
	 */
	public static void warn(final String message, final String title) {
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
	}

	/**
	 * Displays a error dialog with the specified message and title.
	 * 
	 * @param message
	 *            the message to use in the displayed dialog
	 * @param title
	 *            the title of the displayed dialog
	 * @see JOptionPane#showMessageDialog(java.awt.Component, Object, String,
	 *      int)
	 * @see JOptionPane#ERROR_MESSAGE
	 */
	public static void error(final String message, final String title) {
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Helper method that calls {@link Dialogs#error(String, String)} with a
	 * default title.
	 * 
	 * @param message
	 *            the message that describes the error that occurred
	 */
	public static void error(final String message) {
		Dialogs.error(message, "Error");
	}

}
