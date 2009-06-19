package com.bluespot.collections;

import static org.junit.Assert.assertTrue;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;

import org.junit.Test;

public class CollectionUtilTest {

    private boolean testEqualDeques(final Deque<?> a, final Deque<?> b) {
        final Iterator<?> aIter = a.iterator();
        final Iterator<?> bIter = b.iterator();
        while (aIter.hasNext() && bIter.hasNext()) {
            final Object aValue = aIter.next();
            final Object bValue = bIter.next();
            if (aValue == null ? bValue != null : !aValue.equals(bValue)) {
                // Not equal, so return early.
                return false;
            }
        }
        return !aIter.hasNext() && !bIter.hasNext();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveFromTailThrowsOnWayOffSize() {
        // Should throw an exception since we're removing too many elements
        CollectionUtil.removeFromTail(new ArrayDeque<Object>(), 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveFromTailThrowsOnOneOffSize() {
        final Deque<Integer> reference = new ArrayDeque<Integer>();
        Collections.addAll(reference, 1);

        // Should throw an exception since we're trying to remove one too many
        CollectionUtil.removeFromTail(reference, 2);
    }

    @Test
    public void testRemoveToLastOccurrence() {
        final Deque<Integer> deque = new ArrayDeque<Integer>();
        Collections.addAll(deque, 1, 2, 3, 2, 1);

        final Deque<Integer> reference = new ArrayDeque<Integer>();
        Collections.addAll(reference, 1, 2, 3);

        CollectionUtil.removeToLastOccurrence(deque, 3);

        assertTrue(String.format("%s is [1,2,3]", deque), this.testEqualDeques(deque, reference));
    }

    @Test
    public void testRemoveFromStart() {
        final Deque<Integer> deque = new ArrayDeque<Integer>();
        Collections.addAll(deque, 1, 2, 3, 2, 1);
        CollectionUtil.removeFromStart(deque, 2);

        final Deque<Integer> reference = new ArrayDeque<Integer>();
        Collections.addAll(reference, 3, 2, 1);

        assertTrue(String.format("%s is %s", deque, reference), this.testEqualDeques(deque, reference));
    }

    @Test
    public void testRemoveFromStartWithAllRemoved() {
        final Deque<Integer> deque = new ArrayDeque<Integer>();
        Collections.addAll(deque, 1, 2, 3);
        CollectionUtil.removeFromStart(deque, 3);

        assertTrue("Deque is empty", deque.isEmpty());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveFromStartThrowsExceptionIfTooManyElementsAreRemoved() {
        final Deque<Integer> deque = new ArrayDeque<Integer>();
        Collections.addAll(deque, 1, 2, 3);

        // Throws an IAE since we're many more than what we should be
        CollectionUtil.removeFromStart(deque, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveFromStartThrowsExceptionIfJustOneTooManyElementsAreRemoved() {
        final Deque<Integer> deque = new ArrayDeque<Integer>();
        Collections.addAll(deque, 1, 2, 3);

        // Throws an IAE since we're only one more than what we should be
        CollectionUtil.removeFromStart(deque, 4);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveFromStartThrowsExceptionIfRequestedRemovalsIsNegative() {
        final Deque<Integer> deque = new ArrayDeque<Integer>();
        Collections.addAll(deque, 1, 2, 3);

        // Throws an IAE since we're requesting a negative removal
        CollectionUtil.removeFromStart(deque, -1);
    }

    @Test
    public void testRemoveAfter() {
        final Deque<Integer> deque = new ArrayDeque<Integer>();
        Collections.addAll(deque, 0, 1, 2, 3, 4);

        final Deque<Integer> reference = new ArrayDeque<Integer>();
        Collections.addAll(reference, 0, 1);

        CollectionUtil.removeStartingFrom(deque, 2);

        assertTrue(String.format("%s is %s", deque, reference), this.testEqualDeques(deque, reference));
    }

    @Test
    public void testRemoveAfterWithZero() {
        final Deque<Integer> deque = new ArrayDeque<Integer>();
        Collections.addAll(deque, 0, 1, 2, 3, 4);

        CollectionUtil.removeStartingFrom(deque, 0);

        assertTrue("Resulting deque is empty", deque.isEmpty());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAfterThrowsExceptionWithNegative() {
        final Deque<Integer> deque = new ArrayDeque<Integer>();
        Collections.addAll(deque, 0, 1, 2, 3, 4);

        // Throws because the starting index is negative
        CollectionUtil.removeStartingFrom(deque, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAfterThrowsExceptionWithVeryNegative() {
        final Deque<Integer> deque = new ArrayDeque<Integer>();
        Collections.addAll(deque, 0, 1, 2, 3, 4);

        // Throws because the starting index is very negative
        CollectionUtil.removeStartingFrom(deque, -10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAfterThrowsExceptionWithVeryPositive() {
        final Deque<Integer> deque = new ArrayDeque<Integer>();
        Collections.addAll(deque, 0);

        // Throws because the starting index is very positive
        CollectionUtil.removeStartingFrom(deque, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAfterThrowsExceptionWithOneOffPositive() {
        final Deque<Integer> deque = new ArrayDeque<Integer>();
        Collections.addAll(deque, 0);

        // Throws because the starting index is very positive
        CollectionUtil.removeStartingFrom(deque, 2);
    }

    @Test
    public void testRemoveToFirstOccurrenceClearsListWhenGivenNonPresentElement() {
        final Deque<Integer> deque = new ArrayDeque<Integer>();
        Collections.addAll(deque, 1, 1, 1);

        CollectionUtil.removeToFirstOccurrence(deque, 2);

        assertTrue("deque is empty", deque.isEmpty());
    }

    @Test
    public void testRemoveToLastOccurrenceClearsListWhenGivenNonPresentElement() {
        final Deque<Integer> deque = new ArrayDeque<Integer>();
        Collections.addAll(deque, 1, 1, 1);

        CollectionUtil.removeToLastOccurrence(deque, 2);

        assertTrue("deque is empty", deque.isEmpty());
    }

    @Test
    public void testRemoveToFirstOccurrence() {
        final Deque<Integer> deque = new ArrayDeque<Integer>();
        Collections.addAll(deque, 1, 2, 3, 2, 1);

        final Deque<Integer> reference = new ArrayDeque<Integer>();
        Collections.addAll(reference, 3, 2, 1);

        CollectionUtil.removeToFirstOccurrence(deque, 3);

        assertTrue(String.format("%s is %s", deque, reference), this.testEqualDeques(deque, reference));
    }

    @Test
    public void testSynchronizeDequesWithEmptyDeque() {
        final Deque<Integer> reference = new ArrayDeque<Integer>();
        Collections.addAll(reference, 1, 2, 3);

        final Deque<Integer> target = new ArrayDeque<Integer>();

        CollectionUtil.synchronizeDeques(reference, target);

        assertTrue(String.format("%s is %s", target, reference), this.testEqualDeques(target, reference));
    }

    @Test
    public void testSynchronizeDequesWithPartialDeque() {
        final Deque<Integer> reference = new ArrayDeque<Integer>();
        Collections.addAll(reference, 1, 2, 3, 4, 5);

        final Deque<Integer> target = new ArrayDeque<Integer>();
        Collections.addAll(target, 1, 2, 3, 7, 8);

        CollectionUtil.synchronizeDeques(reference, target);

        assertTrue(String.format("%s is %s", target, reference), this.testEqualDeques(target, reference));
    }

    @Test
    public void testSynchronizeDequesWithDisjointDeque() {
        final Deque<Integer> reference = new ArrayDeque<Integer>();
        Collections.addAll(reference, 1, 2, 3, 4, 5);

        final Deque<Integer> target = new ArrayDeque<Integer>();
        Collections.addAll(target, 6, 7, 8, 9);

        CollectionUtil.synchronizeDeques(reference, target);

        assertTrue(String.format("%s is %s", target, reference), this.testEqualDeques(target, reference));
    }

    @Test
    public void testSynchronizeDequesWithDifferentStart() {
        final Deque<Integer> reference = new ArrayDeque<Integer>();
        Collections.addAll(reference, 1, 2, 3, 4, 5);

        final Deque<Integer> target = new ArrayDeque<Integer>();
        Collections.addAll(target, 2, 2, 3, 4, 5);

        CollectionUtil.synchronizeDeques(reference, target);

        assertTrue(String.format("%s is %s", target, reference), this.testEqualDeques(target, reference));
    }

    @Test
    public void testRemoveStartingFrom() {
        final Deque<String> target = new ArrayDeque<String>();
        Collections.addAll(target, "A", "B", "C");
        CollectionUtil.removeStartingFrom(target, "B");

        final Deque<String> reference = new ArrayDeque<String>();
        Collections.addAll(reference, "A");

        assertTrue(String.format("%s is %s", target, reference), this.testEqualDeques(target, reference));
    }

    @Test
    public void testDequesAreEqual() {
        final Deque<Integer> reference = new ArrayDeque<Integer>();
        Collections.addAll(reference, 1, 2, 3);

        final Deque<Integer> target = new ArrayDeque<Integer>();
        Collections.addAll(target, 1, 2, 3);

        assertTrue("Equal deques are equal", this.testEqualDeques(reference, target));
    }

    @Test
    public void testRemoveDifferingTail() {
        final Deque<Integer> reference = new ArrayDeque<Integer>();
        Collections.addAll(reference, 1, 2, 3);

        final Deque<Integer> target = new ArrayDeque<Integer>();
        Collections.addAll(target, 1, 2, 4, 5);

        CollectionUtil.removeDifferingTail(reference, target);
        assertTrue("Reference is at least as big as target", reference.size() >= target.size());
        while (reference.size() > target.size()) {
            reference.removeLast();
        }
        assertTrue("Trimmed reference is equal to target", this.testEqualDeques(reference, target));
    }
}
