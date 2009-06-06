package com.bluespot.collections;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

/**
 * A collection of methods that deal with collections.
 * <p>
 * None of the methods here are thread-safe without external synchronization.
 * 
 * @author Aaron Faanes
 * 
 */
public class CollectionUtil {

	private CollectionUtil() {
		// Suppresses default constructor, ensuring non-instantiability.
		throw new AssertionError("Instantiation not allowed");
	}

	/**
	 * Ensures that the target deque will have the same values as the reference
	 * deque, but preserves any similar elements in the target deque.
	 * <p>
	 * This will remove all differing elements in the tail of the target deque,
	 * then add the elements in the reference deque that are after the start of
	 * the differing elements. This albeit odd ordering ensures that the deque
	 * is modified using only native deque methods; no middle elements are
	 * removed or added. As a consequence, this will yield proper results when
	 * using the specified deque is an {@code ObservableDeque}.
	 * 
	 * @param <E>
	 *            the common supertype of the collections
	 * @param targetCollection
	 *            the target collection that is modified. At the end of this
	 *            method, this will be equal to the reference collection.
	 * @param referenceCollection
	 *            the reference collection. At the end of this method, this will
	 *            be equal to the target collection
	 * @throws IllegalStateException
	 *             if the values are not allowed to be added to the {@code
	 *             targetCollection}
	 */
	public static <E> void synchronizeDeques(final Deque<E> targetCollection,
			final Deque<? extends E> referenceCollection) {
		CollectionUtil.removeDifferingTail(targetCollection, referenceCollection);
		final int equalElements = targetCollection.size();
		int i = 0;
		for (final Iterator<? extends E> iterator = referenceCollection.iterator(); iterator.hasNext(); i++) {
			final E value = iterator.next();
			if (i > equalElements) {
				targetCollection.addLast(value);
			}
		}

	}

	/**
	 * Removes all elements in the target collection that are after the first
	 * differing element. In other words, given two deques:
	 * <ul>
	 * <li>Target: {@code [A, B, C, D]}</li>
	 * <li>Reference: {@code [A, B, E, F]}</li>
	 * </ul>
	 * Two elements will be removed from the target collection, {@code C} and
	 * {@code D}.
	 * <p>
	 * This method will perform these removals using only native deque methods.
	 * Specifically, no values will be removed from the middle of the target
	 * collection, only from the end. This is useful if you wish to synchronize
	 * two {@code ObservableDeque} objects.
	 * 
	 * @param targetCollection
	 *            the deque to modify
	 * @param referenceCollection
	 *            the reference deque
	 * @return the number of removed elements. This will be at least zero, up to
	 *         the size of {@code targetCollection}.
	 */
	public static <E> int removeDifferingTail(final Deque<? extends E> targetCollection,
			final Deque<? extends E> referenceCollection) {
		int equalElements = 0;
		final Iterator<? extends E> targetIterator = targetCollection.iterator();
		final Iterator<? extends E> referenceIterator = referenceCollection.iterator();
		while (targetIterator.hasNext() && referenceIterator.hasNext()) {
			final E referenceValue = referenceIterator.next();
			final E candidateValue = targetIterator.next();
			if (referenceValue == null ? candidateValue == null : referenceValue.equals(candidateValue)) {
				// They're the same, so continue
				equalElements++;
			} else {
				// They're different, remove elements from the target
				// collection.
				break;
			}
		}
		/*
		 * Remove all differing elements. Note that if collections are equal and
		 * of equal size, no elements are removed.
		 */
		final int removedElements = targetCollection.size() - equalElements;
		CollectionUtil.removeFromTail(targetCollection, removedElements);
		return removedElements;
	}

	public static void removeFromTail(final Deque<?> deque, final int numRemoved) {
		for (int i = 0; i < numRemoved; i++) {
			deque.removeLast();
		}
	}

	/**
	 * Removes up to {@code numRemoved} elements from the specified collection.
	 * 
	 * @param collection
	 *            the collection that is modified in this method
	 * @param numRemoved
	 *            the number of elements to remove
	 */
	public static void removeFromStart(final Collection<?> collection, final int numRemoved) {
		int i = 0;
		for (final Iterator<?> iterator = collection.iterator(); iterator.hasNext(); i++) {
			if (i >= numRemoved) {
				// We've removed enough, return
				return;
			}
			iterator.remove();
		}
	}

	/**
	 * Removes all elements in the specified collection that are after the
	 * specified index.
	 * 
	 * @param collection
	 *            the collection that is modified in this method
	 * @param removeIndex
	 *            the first index that should be removed, inclusively
	 * @throws IllegalArgumentException
	 *             if {@code removeIndex} is less than zero; this method should
	 *             not be used to implicitly clear collections
	 */
	public static void removeAfter(final Collection<?> collection, final int removeIndex) {
		if (removeIndex < 0) {
			throw new IllegalArgumentException("removeIndex is less than zero");
		}
		int i = 0;
		for (final Iterator<?> iterator = collection.iterator(); iterator.hasNext(); i++) {
			iterator.next();
			if (i >= removeIndex) {
				iterator.remove();
			}
		}
	}

	/**
	 * Removes all elements in the specified collection that are after the first
	 * occurrence of the specified element.
	 * <p>
	 * If the element is not contained in this deque, no elements are removed.
	 * 
	 * @param <E>
	 *            the type of element in this deque
	 * @param collection
	 *            the deque that is modified in this method
	 * @param element
	 *            the element that marks the new end of the collection. All
	 *            subsequent elements will be removed.
	 */
	public static <E> void removeAfter(final Collection<? extends E> collection, final E element) {
		boolean isRemoving = false;
		for (final Iterator<? extends E> iterator = collection.iterator(); iterator.hasNext();) {
			final E candidate = iterator.next();
			if (isRemoving) {
				iterator.remove();
				continue;
			}
			if (candidate == null ? element == null : candidate.equals(element)) {
				// They're equal, so begin removing
				isRemoving = true;
				// Remove this element as well.
				iterator.remove();
			}
		}
	}

	/**
	 * Removes all elements that are in front of the specified element. If the
	 * deque does not contain the specified element, all elements are removed.
	 * <p>
	 * This method will not perform spurious removes or additions. The only
	 * elements removed are ones that are before, but not equal to, the
	 * specified element.
	 * 
	 * @param deque
	 *            the deque from which elements are removed
	 * @param element
	 *            the element that serves as the "poison pill" for this method.
	 *            All elements in front of it will be removed, and removal will
	 *            stop when this element is the first element in the deque.
	 * @param <E>
	 *            the type of element contained in the deque
	 * 
	 * @see Deque#removeFirst()
	 */
	public static <E> void removeToFirstOccurrence(final Deque<E> deque, final E element) {
		while (!deque.isEmpty()) {
			final E value = deque.getFirst();
			if (value == null ? element == null : value.equals(element)) {
				// It's a match, so break.
				return;
			}
			deque.removeFirst();
		}
	}

	/**
	 * Removes all elements that are behind the specified element. If the deque
	 * does not contain the specified element, all elements are removed.
	 * <p>
	 * This method will not perform spurious removes or additions. The only
	 * elements removed are ones that are after, but not equal to, the specified
	 * element.
	 * 
	 * @param deque
	 *            the deque from which elements are removed
	 * @param element
	 *            the element that serves as the "poison pill" for this method.
	 *            All methods behind this element will be removed. Removal will
	 *            stop when this element is the last element in the deque, or
	 *            when the deque is empty.
	 * @param <E>
	 *            the type of element contained in the deque
	 * 
	 * @see Deque#removeFirst()
	 */
	public static <E> void removeToLastOccurrence(final Deque<E> deque, final E element) {
		while (!deque.isEmpty()) {
			final E value = deque.getLast();
			if (value == null ? element == null : value.equals(element)) {
				// It's a match, so break.
				return;
			}
			deque.removeLast();
		}
	}
}