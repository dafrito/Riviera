package com.bluespot.forms.property;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DefaultPropertySheet {

	private boolean isImmutable;
	protected Map<String, Object> properties;

	public DefaultPropertySheet() {
		this(false);
	}

	public DefaultPropertySheet(final boolean isImmutable) {
		this.properties = new HashMap<String, Object>();
		this.isImmutable = isImmutable;
	}

	public DefaultPropertySheet(final boolean isImmutable, final DefaultPropertySheet other) {
		this(false);
		for (final String propertyName : other.getPropertyNames()) {
			this.setProperty(propertyName, other.getProperty(propertyName));
		}
		this.isImmutable = isImmutable;
	}

	public DefaultPropertySheet(final DefaultPropertySheet other) {
		this(false, other);
	}

	public DefaultPropertySheet freeze() {
		this.isImmutable = true;
		return this;
	}

	public DefaultPropertySheet getImmutableProperties() {
		return this.getProperties(true);
	}

	public DefaultPropertySheet getProperties(final boolean isNewPropertySheetImmutable) {
		return new DefaultPropertySheet(isNewPropertySheetImmutable, this);
	}

	public Object getProperty(final String propertyName) {
		return this.properties.get(propertyName);
	}

	public Set<String> getPropertyNames() {
		return this.properties.keySet();
	}

	public boolean isImmutable() {
		return this.isImmutable;
	}

	public void setProperty(final String propertyName, final Object value) {
		if (this.isImmutable()) {
			throw new UnsupportedOperationException("PropertySheet is immutable.");
		}
		this.properties.put(propertyName, value);
	}

}