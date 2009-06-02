package com.bluespot.forms;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.bluespot.forms.Forms.FormFactoryException;
import com.bluespot.forms.model.field.Field;
import com.bluespot.forms.model.field.Form;

public class XMLFormParser extends DefaultHandler {

    private List<Form> forms = null;
    private Deque<ParseState> stateStack = new ArrayDeque<ParseState>();

    private static class UnexpectedElementTypeException extends SAXException {
        public UnexpectedElementTypeException(ElementType elementType) {
            this(elementType.toString());
        }

        public UnexpectedElementTypeException(String elementType) {
            super("Unexpected element type: " + elementType);
        }
    }

    private static class UnexpectedAttributeTypeException extends SAXException {
        public UnexpectedAttributeTypeException(AttributeType attributeType) {
            this(attributeType.toString());
        }

        public UnexpectedAttributeTypeException(String attributeType) {
            super("Unexpected attribute type: " + attributeType);
        }
    }

    protected Form getCurrentForm() {
        if(this.forms.isEmpty())
            return null;
        return this.forms.get(this.forms.size() - 1);
    }

    protected static abstract class ParseState {
        public abstract void startElement(XMLFormParser parser, String elementTypeName, Attributes attributes)
                throws SAXException;

        public abstract void endElement(XMLFormParser parser, String elementTypeName) throws SAXException;

        public void populateFormItem(Field<?> field, Attributes attributes) throws SAXException {
            for(int index = 0; index < attributes.getLength(); index++) {
                String attributeTypeName = attributes.getLocalName(index);
                AttributeType attributeType = AttributeType.getAttributeType(attributeTypeName.toUpperCase());

                String attributeValue = attributes.getValue(index);

                switch(attributeType) {
                    case ID:
                        // Ignore; this would have already been parsed.
                        break;
                    case DESCRIPTION:
                        field.setInformationBlurb(attributeValue);
                        break;
                    case LABEL:
                        field.setLabel(attributeValue);
                        break;
                    case SELECTED:
                    case VALUE:
                    case UNKNOWN:
                        throw new UnexpectedAttributeTypeException(attributeTypeName);
                    default:
                        throw new UnexpectedAttributeTypeException(attributeType);
                }
            }
        }

        public String getId(Attributes attributes) {
            return attributes.getValue(AttributeType.ID.toString());
        }
    }

    protected static final ParseState LOOKING_FOR_FORM = new ParseState() {

        @Override
        public void startElement(XMLFormParser parser, String elementTypeName, Attributes attributes)
                throws SAXException {
            ElementType elementType = ElementType.getElementType(elementTypeName);
            switch(elementType) {
                case FORM:
                    parser.pushForm(new Form(this.getId(attributes)));
                    parser.pushParseState(XMLFormParser.BUILDING_FORM);
                    return;
                case UNKNOWN:
                    return;
                default:
                    throw new UnexpectedElementTypeException(elementType);
            }
        }

        @Override
        public void endElement(XMLFormParser parser, String elementTypeName) throws SAXException {
            // TODO Auto-generated method stub

        }
    };

    protected static final ParseState BUILDING_FORM = new ParseState() {

        @Override
        public void endElement(XMLFormParser parser, String elementTypeName) throws SAXException {
            // TODO Auto-generated method stub

        }

        @Override
        public void startElement(XMLFormParser parser, String elementTypeName, Attributes attributes)
                throws SAXException {
            ElementType elementType = ElementType.getElementType(elementTypeName);
            switch(elementType) {
                case OPTION:

                case UNKNOWN:
                    throw new UnexpectedElementTypeException(elementTypeName);
                case CHECKBOXES:
                    break;
                case COMBOBOX:
                    break;
                case FILE:
                    break;
                case FLOW:
                    break;
                case FORM:
                    break;
                case NUMBER:
                    break;
                case STRING:
                    break;
                default:
                    throw new UnexpectedElementTypeException(elementType);
            }
        }

    };

    public List<Form> getForms() {
        return Collections.unmodifiableList(this.forms);
    }

    public ParseState getParseState() {
        ParseState state = this.stateStack.peekLast();
        if(state == null)
            state = LOOKING_FOR_FORM;
        return state;
    }

    public void pushParseState(ParseState state) {
        this.stateStack.push(state);
    }

    public void popParseState() {
        this.stateStack.pop();
    }

    @Override
    public void startDocument() throws SAXException {
        this.forms = new ArrayList<Form>();
    }

    @Override
    public void endDocument() throws SAXException {
        System.out.println("Finished!");
    }

    protected void pushForm(Form form) {
        this.forms.add(form);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        // TODO Auto-generated method stub
        super.characters(ch, start, length);
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        this.getParseState().startElement(this, name, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        // TODO Auto-generated method stub
        super.endElement(uri, localName, name);
    }

    public static void main(String[] args) throws FormFactoryException {
        Forms.fromXML(new File("forms.xml"));
    }

}
