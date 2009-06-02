package com.bluespot.swing;

import java.awt.Component;
import java.awt.Container;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ListModel;

public abstract class NamingRepeater<C extends Component, E> extends Repeater<C, E> {

	protected final PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {

		public void propertyChange(final PropertyChangeEvent evt) {
			if (evt.getPropertyName() != "name") {
				return;
			}
			final Component childComponent = (Component) evt.getSource();
			final int index = Components.getIndexOf(NamingRepeater.this.getParent(), childComponent);
			NamingRepeater.this.nameChanged((String) evt.getNewValue(), NamingRepeater.this.getComponentAt(index),
					index, NamingRepeater.this.getElementAt(index));
		}
	};

	public NamingRepeater(final Container parent, final ListModel model) {
		super(parent, model);
	}

	private void attachNameChangeListener(final PropertyChangeListener listener, final C childComponent,
			final int index, final E childValue) {
		final Component source = this.getNameSource(childComponent);
		source.addPropertyChangeListener("name", listener);
		this.nameChanged(source.getName(), childComponent, index, childValue);
	}

	private void detachNameChangeListener(final PropertyChangeListener listener, final C childComponent) {
		this.getNameSource(childComponent).removePropertyChangeListener("name", listener);
	}

	@Override
	protected void addComponent(final C childComponent, final int index, final E childValue) {
		super.addComponent(childComponent, index, childValue);
		this.attachNameChangeListener(this.propertyChangeListener, childComponent, index, childValue);
	}

	/**
	 * Returns the real source of name-changes. This is useful to override when
	 * you wrap the real component.
	 * 
	 * The default implementation simply returns the childComponent
	 * 
	 * @param childComponent
	 *            The component created by the Repeater
	 * @return the Component that emits name-changes revelant to this
	 *         childComponent
	 */
	protected Component getNameSource(final C childComponent) {
		return childComponent;
	}

	protected abstract void nameChanged(String newValue, C childComponent, int index, E childValue);

	@Override
	protected void removeComponent(final C childComponent, final int index) {
		this.detachNameChangeListener(this.propertyChangeListener, childComponent);
		super.removeComponent(childComponent, index);
	}
}
