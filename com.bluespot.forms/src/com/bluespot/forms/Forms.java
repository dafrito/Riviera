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
    
    private Forms() {
        // Not instantiable.
    }
    
    public static List<Form> fromXML(File file) throws FormFactoryException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        factory.setNamespaceAware(false);
        try { 
            SAXParser parser = factory.newSAXParser();
            XMLFormParser formParser = new XMLFormParser();
            parser.parse(file, formParser);
            return formParser.getForms();
        } catch(SAXException e) {
            throw new FormFactoryException(e);
        } catch(ParserConfigurationException e) {
            throw new FormFactoryException(e);
        } catch(IOException e) {
            throw new FormFactoryException(e);
        }
    }

    public static class FormFactoryException extends Exception {
        public FormFactoryException(Throwable cause) {
            super(cause);
        }

        public FormFactoryException() {
            super();
        }

        public FormFactoryException(String message, Throwable cause) {
            super(message, cause);
        }

        public FormFactoryException(String message) {
            super(message);
        }
    }
}
