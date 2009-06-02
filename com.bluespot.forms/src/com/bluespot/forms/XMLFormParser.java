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

	private static class UnexpectedAttributeTypeException extends SAXException {
		public UnexpectedAttributeTypeException(final AttributeType attributeType) {
			this(attributeType.toString());
		}

		public UnexpectedAttributeTypeException(final String attributeType) {
			super("Unexpected attribute type: " + attributeType);
		}
	}

	private static class UnexpectedElementTypeException extends SAXException {
		public UnexpectedElementTypeException(final ElementType elementType) {
			this(elementType.toString());
		}

		public UnexpectedElementTypeException(final String elementType) {
			super("Unexpected element type: " + elementType);
		}
	}

	protected static abstract class ParseState {
		public abstract void endElement(XMLFormParser parser, String elementTypeName) throws SAXException;

		public String getId(final Attributes attributes) {
			return attributes.getValue(AttributeType.ID.toString());
		}

		public void populateFormItem(final Field<?> field, final Attributes attributes) throws SAXException {
			for (int index = 0; index < attributes.getLength(); index++) {
				final String attributeTypeName = attributes.getLocalName(index);
				final AttributeType attributeType = AttributeType.getAttributeType(attributeTypeName.toUpperCase());

				final String attributeValue = attributes.getValue(index);

				switch (attributeType) {
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

		public abstract void startElement(XMLFormParser parser, String elementTypeName, Attributes attributes)
				throws SAXException;
	}

	private List<Form> forms = null;

	private final Deque<ParseState> stateStack = new ArrayDeque<ParseState>();

	@Override
	public void characters(final char[] ch, final int start, final int length) throws SAXException {
		// TODO Auto-generated method stub
		super.characters(ch, start, length);
	}

	@Override
	public void endDocument() throws SAXException {
		System.out.println("Finished!");
	}

	@Override
	public void endElement(final String uri, final String localName, final String name) throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, name);
	}

	public List<Form> getForms() {
		return Collections.unmodifiableList(this.forms);
	}

	public ParseState getParseState() {
		ParseState state = this.stateStack.peekLast();
		if (state == null) {
			state = XMLFormParser.LOOKING_FOR_FORM;
		}
		return state;
	}

	public void popParseState() {
		this.stateStack.pop();
	}

	public void pushParseState(final ParseState state) {
		this.stateStack.push(state);
	}

	@Override
	public void startDocument() throws SAXException {
		this.forms = new ArrayList<Form>();
	}

	@Override
	public void startElement(final String uri, final String localName, final String name, final Attributes attributes)
			throws SAXException {
		this.getParseState().startElement(this, name, attributes);
	}

	protected Form getCurrentForm() {
		if (this.forms.isEmpty()) {
			return null;
		}
		return this.forms.get(this.forms.size() - 1);
	}

	protected void pushForm(final Form form) {
		this.forms.add(form);
	}

	public static void main(final String[] args) throws FormFactoryException {
		Forms.fromXML(new File("forms.xml"));
	}

	protected static final ParseState BUILDING_FORM = new ParseState() {

		@Override
		public void endElement(final XMLFormParser parser, final String elementTypeName) throws SAXException {
			// TODO Auto-generated method stub

		}

		@Override
		public void startElement(final XMLFormParser parser, final String elementTypeName, final Attributes attributes)
				throws SAXException {
			final ElementType elementType = ElementType.getElementType(elementTypeName);
			switch (elementType) {
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

	protected static final ParseState LOOKING_FOR_FORM = new ParseState() {

		@Override
		public void endElement(final XMLFormParser parser, final String elementTypeName) throws SAXException {
			// TODO Auto-generated method stub

		}

		@Override
		public void startElement(final XMLFormParser parser, final String elementTypeName, final Attributes attributes)
				throws SAXException {
			final ElementType elementType = ElementType.getElementType(elementTypeName);
			switch (elementType) {
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
	};

}
