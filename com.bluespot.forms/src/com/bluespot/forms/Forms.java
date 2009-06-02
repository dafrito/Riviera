package com.bluespot.forms;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.bluespot.forms.model.field.Form;

public class Forms {

	public static class FormFactoryException extends Exception {
		public FormFactoryException() {
			super();
		}

		public FormFactoryException(final String message) {
			super(message);
		}

		public FormFactoryException(final String message, final Throwable cause) {
			super(message, cause);
		}

		public FormFactoryException(final Throwable cause) {
			super(cause);
		}
	}

	private Forms() {
		// Not instantiable.
	}

	public static List<Form> fromXML(final File file) throws FormFactoryException {
		final SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(true);
		factory.setNamespaceAware(false);
		try {
			final SAXParser parser = factory.newSAXParser();
			final XMLFormParser formParser = new XMLFormParser();
			parser.parse(file, formParser);
			return formParser.getForms();
		} catch (final SAXException e) {
			throw new FormFactoryException(e);
		} catch (final ParserConfigurationException e) {
			throw new FormFactoryException(e);
		} catch (final IOException e) {
			throw new FormFactoryException(e);
		}
	}
}
