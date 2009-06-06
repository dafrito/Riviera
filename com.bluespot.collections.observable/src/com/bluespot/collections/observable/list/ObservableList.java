package com.bluespot.collections.observable.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.bluespot.collections.proxies.ListProxy;

/**
 * Adapts a {@link List} for use with a {@link ListModel}.
 * 
 * @author Aaron Faanes
 * 
 * @param <E>
 *            the type of elements in this list model
 */
public class ObservableList<E> extends ListProxy<E> implements ListModel {

	private final List<ListDataListener> listeners = new CopyOnWriteArrayList<ListDataListener>();

	private final List<E> targetList;

	/**
	 * Constructs a proxied list model that proxies an empty list.
	 */
	public ObservableList() {
		this.targetList = new ArrayList<E>();
	}

	/**
	 * Constructs a proxied list model using the values from the specified
	 * collection.
	 * 
	 * @param collection
	 *            the collection that will populate this list model's list
	 */
	public ObservableList(final Collection<E> collection) {
		if (collection == null) {
			throw new NullPointerException("collection cannot be null");
		}
		this.targetList = new ArrayList<E>(collection);
	}

	@Override
	public boolean add(final E e) {
		final boolean collectionChanged = super.add(e);
		if (collectionChanged) {
			this.fireIntervalAdded(this.size() - 1, this.size() - 1);
		}
		return collectionChanged;
	}

	@Override
	public void add(final int index, final E element) {
		super.add(index, element);
		this.fireIntervalAdded(index, index);
	}

	@Override
	public boolean addAll(final Collection<? extends E> c) {
		final boolean collectionChanged = super.addAll(c);
		if (collectionChanged) {
			this.fireIntervalAdded(this.size() - c.size(), this.size() - 1);
		}
		return collectionChanged;
	}

	@Override
	public boolean addAll(final int index, final Collection<? extends E> c) {
		final boolean collectionChanged = super.addAll(index, c);
		if (collectionChanged) {
			this.fireIntervalAdded(index, index + c.size() - 1);
		}
		return collectionChanged;
	}

	public void addListDataListener(final ListDataListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public void clear() {
		final int oldSize = this.size();
		super.clear();
		if (oldSize > 0) {
			this.fireIntervalRemoved(0, oldSize - 1);
		}
	}

	public E getElementAt(final int index) {
		return this.get(index);
	}

	public int getSize() {
		return this.size();
	}

	@Override
	public E remove(final int index) {
		final E oldElement = super.remove(index);
		this.fireIntervalRemoved(index, index);
		return oldElement;
	}

	@Override
	public boolean remove(final Object o) {
		final int index = this.indexOf(o);
		if (index < 0) {
			// The object isn't in our list; return silently.
			return false;
		}
		// We rely on remove(index) to emit our event.
		this.remove(index);
		return true;
	}

	@Override
	public boolean removeAll(final Collection<?> c) {
		int minimumBounds = -1;
		int maximumBounds = -1;
		for (final Object o : c) {
			int index = this.indexOf(o);
			if (index >= 0) {
				minimumBounds = minimumBounds == -1 ? index : Math.min(minimumBounds, index);
			}
			index = this.lastIndexOf(o);
			if (index >= 0) {
				maximumBounds = maximumBounds == -1 ? index : Math.max(maximumBounds, index);
			}
		}
		if (minimumBounds != -1 || maximumBounds != -1) {
			// The list will be affected.
			super.removeAll(c);
			this.fireContentsChanged(minimumBounds, maximumBounds);
			return true;
		}
		return false;
	}

	public void removeListDataListener(final ListDataListener listener) {
		this.listeners.remove(listener);
	}

	@Override
	public boolean retainAll(final Collection<?> c) {
		int index = 0;
		int minimumBounds = -1;
		int maximumBounds = -1;
		for (final E element : this) {
			if (c.contains(element) == false) {
				// It is not contained in the given collection, therefore
				// it will be removed.
				if (minimumBounds == -1) {
					minimumBounds = index;
				}
				if (maximumBounds == -1 || maximumBounds < index) {
					maximumBounds = index;
				}
			}
			index++;
		}
		if (minimumBounds != -1 || maximumBounds != -1) {
			// The list will be affected.
			super.retainAll(c);
			this.fireContentsChanged(minimumBounds, maximumBounds);
			return true;
		}
		return false;
	}

	@Override
	public E set(final int index, final E element) {
		final E oldElement = super.set(index, element);
		if (!element.equals(oldElement)) {
			this.fireContentsChanged(index, index);
		}
		return oldElement;
	}

	@Override
	public List<E> subList(final int offset, final int toIndex) {
		final ObservableList<E> sublist = new ObservableList<E>(super.subList(offset, toIndex));

		final List<E> sourceList = this.getSourceList();

		sublist.addListDataListener(new ListDataListener() {

			public void contentsChanged(final ListDataEvent e) {
				final int lowerBound = offset + e.getIndex0();
				final int upperBound = offset + e.getIndex1();
				for (int index = e.getIndex0(); index <= e.getIndex1(); index++) {
					sourceList.set(offset + index, sublist.get(index));
				}
				ObservableList.this.fireContentsChanged(lowerBound, upperBound);
			}

			public void intervalAdded(final ListDataEvent e) {
				final int lowerBound = offset + e.getIndex0();
				final int upperBound = offset + e.getIndex1();
				sourceList.addAll(lowerBound, sublist.subList(e.getIndex0(), e.getIndex1() + 1));
				ObservableList.this.fireIntervalAdded(lowerBound, upperBound);
			}

			public void intervalRemoved(final ListDataEvent e) {
				final int lowerBound = offset + e.getIndex0();
				final int upperBound = offset + e.getIndex1();
				sourceList.subList(lowerBound, upperBound + 1).clear();
				ObservableList.this.fireIntervalRemoved(lowerBound, upperBound);
			}

		});

		this.addListDataListener(new ListDataListener() {

			public void contentsChanged(final ListDataEvent e) {
				for (int index = e.getIndex0(); index <= e.getIndex1(); index++) {
					if (!this.withinRange(index)) {
						continue;
					}
					sublist.set(index - offset, ObservableList.this.get(index));
				}
			}

			public int endIndex() {
				return offset + sublist.size();
			}

			public void intervalAdded(final ListDataEvent e) {
				// Do nothing!

			}

			public void intervalRemoved(final ListDataEvent e) {
				// Do nothing!
			}

			public boolean withinRange(final int index) {
				return offset <= index && index < this.endIndex();
			}
		});

		return sublist;
	}

	/**
	 * Subclasses must invoke this method after a section of elements has
	 * changed.
	 * 
	 * @param startIndex
	 *            the index of the first changed element
	 * @param endIndex
	 *            the index of the last changed element
	 */
	protected void fireContentsChanged(final int startIndex, final int endIndex) {
		final ListDataEvent event = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, startIndex, endIndex);
		for (final ListDataListener listener : this.listeners) {
			listener.contentsChanged(event);
		}
	}

	/**
	 * Subclasses must invoke this method after elements are added to this list
	 * model.
	 * 
	 * @param startIndex
	 *            the index of the first added element
	 * @param endIndex
	 *            the index of the last added element
	 */
	protected void fireIntervalAdded(final int startIndex, final int endIndex) {
		final ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, startIndex, endIndex);
		for (final ListDataListener listener : this.listeners) {
			listener.contentsChanged(event);
		}
	}

	/**
	 * Subclasses must invoke this method after elements are removed from this
	 * list model.
	 * 
	 * @param startIndex
	 *            the index of the first removed element
	 * @param endIndex
	 *            the index of the last removed element
	 * @see ListDataListener#intervalRemoved(ListDataEvent)
	 */
	protected void fireIntervalRemoved(final int startIndex, final int endIndex) {
		final ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, startIndex, endIndex);
		for (final ListDataListener listener : this.listeners) {
			listener.contentsChanged(event);
		}
	}

	@Override
	protected List<E> getSourceList() {
		return this.targetList;
	}

}
