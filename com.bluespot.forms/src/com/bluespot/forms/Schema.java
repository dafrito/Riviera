package com.bluespot.forms;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.bluespot.logic.Visitors;
import com.bluespot.logic.adapters.AbstractHandledAdapter;
import com.bluespot.logic.predicates.AdaptingPredicate;
import com.bluespot.logic.predicates.Predicate;
import com.bluespot.logic.visitors.Sentinel;
import com.bluespot.logic.visitors.Visitor;

public final class Schema<K> {

    private final Map<K, Class<?>> types;
    private final Predicate<Submission<? super K>> predicate;

    private final Predicate<Submission<? super K>> typePredicate = new SchemaTypePredicate<K>(this);

    public Schema(final Map<K, Class<?>> types, final Predicate<Submission<? super K>> predicate) {
        if (types == null) {
            throw new NullPointerException("types is null");
        }
        if (predicate == null) {
            throw new NullPointerException("predicate is null");
        }
        this.types = Collections.unmodifiableMap(new HashMap<K, Class<?>>(types));
        if (this.types.isEmpty()) {
            throw new IllegalArgumentException("types is empty");
        }
        if (this.types.containsKey(null)) {
            throw new NullPointerException("types contains null key");
        }
        for (final Entry<K, Class<?>> entry : this.types.entrySet()) {
            if (entry.getValue() == null) {
                throw new NullPointerException("types contains null value for key '" + entry.getKey() + "'");
            }
        }
        this.predicate = predicate;
    }

    public Map<K, Class<?>> getTypes() {
        // Types is already unmodifiable, so there's no need to wrap it again
        // here.
        return this.types;
    }

    public Predicate<? super Submission<? super K>> getTypePredicate() {
        return this.typePredicate;
    }

    public Predicate<? super Submission<? super K>> getPredicate() {
        return this.predicate;
    }

    public Sentinel<? super Submission<? super K>> newSentinel(final Visitor<Submission<? super K>> visitor) {
        return new Sentinel<Submission<? super K>>(this.getPredicate(), visitor);
    }

    public Sentinel<? super Submission<? super K>> newCheckedSentinel(final Visitor<Submission<? super K>> visitor) {
        return this.newCheckedSentinel(visitor, Visitors.throwException());
    }

    public Sentinel<? super Submission<? super K>> newCheckedSentinel(final Visitor<Submission<? super K>> visitor,
            final Visitor<? super SubmissionClassCastException> handler) {
        final SubmissionTypeChecker<K> checker = new SubmissionTypeChecker<K>(this);
        checker.setHandler(handler);
        final Predicate<Submission<? super K>> checkedPredicate = new AdaptingPredicate<Submission<? super K>, Submission<? super K>>(
                checker, this.getPredicate());
        return new Sentinel<Submission<? super K>>(checkedPredicate, visitor);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Schema<?>)) {
            return false;
        }
        final Schema<?> other = (Schema<?>) obj;
        if (!this.getPredicate().equals(other.getPredicate())) {
            return false;
        }
        if (!this.getTypes().equals(other.getTypes())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.getPredicate().hashCode();
        result = 31 * result + this.getTypes().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("Schema[%s]", this.getPredicate());
    }

    private static final class SubmissionTypeChecker<T> extends
            AbstractHandledAdapter<Submission<? super T>, Submission<? super T>, SubmissionClassCastException> {

        private final Schema<T> schema;

        public SubmissionTypeChecker(final Schema<T> schema) {
            assert schema != null : "schema is null";
            this.schema = schema;
        }

        public Schema<T> getSchema() {
            return this.schema;
        }

        @Override
        public Submission<? super T> adapt(final Submission<? super T> submission) {
            if (submission == null) {
                return null;
            }
            boolean acceptable = true;
            for (final Entry<T, Class<?>> entry : this.getSchema().getTypes().entrySet()) {
                final Class<?> requiredType = entry.getValue();
                final Class<?> candidate = submission.getType(entry.getKey());
                if (candidate == null || !requiredType.isAssignableFrom(candidate)) {
                    acceptable = false;
                    this.getHandler().accept(new SubmissionClassCastException(entry.getKey(), requiredType, candidate));
                }
            }
            /*
             * If we failed our validation, return null.
             */
            return acceptable ? submission : null;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof SubmissionTypeChecker<?>)) {
                return false;
            }
            final SubmissionTypeChecker<?> other = (SubmissionTypeChecker<?>) obj;
            if (!this.getSchema().equals(other.getSchema())) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int result = 7;
            result = 31 * result + this.getSchema().hashCode();
            return result;
        }

        @Override
        public String toString() {
            return String.format("has valid types using schema '%s'", this.getSchema());
        }

    }

    public static final class SubmissionClassCastException extends ClassCastException {

        private final Class<?> actualType;
        private final Class<?> expectedType;
        private final Object key;

        public SubmissionClassCastException(final Object key, final Class<?> expectedType, final Class<?> actualType) {
            if (key == null) {
                throw new NullPointerException("key is null");
            }
            if (expectedType == null) {
                throw new NullPointerException("expectedType is null");
            }
            this.key = key;
            this.expectedType = expectedType;
            this.actualType = actualType;
        }

        public Object getKey() {
            return this.key;
        }

        public Class<?> getExpectedType() {
            return this.expectedType;
        }

        public Class<?> getActualType() {
            return this.actualType;
        }

        @Override
        public String toString() {
            if (this.getActualType() == null) {
                return String.format("For %s, expected type was '%s', but received a null argument.", this.getKey(),
                        this.getExpectedType());
            }
            return String.format("For %s, expected type was '%s', but received '%s'", this.getKey(),
                    this.getExpectedType(), this.getActualType());
        }
    }

    /**
     * A {@link Predicate} that tests a given submission for the correct types.
     * 
     * @author Aaron Faanes
     * 
     * @param <T>
     */
    private static final class SchemaTypePredicate<T> implements Predicate<Submission<? super T>> {

        private final Schema<T> schema;

        public SchemaTypePredicate(final Schema<T> schema) {
            assert schema != null : "schema is null";
            this.schema = schema;
        }

        public Schema<T> getSchema() {
            return this.schema;
        }

        public boolean test(final Submission<? super T> submission) {
            if (submission == null) {
                return false;
            }
            for (final Entry<T, Class<?>> entry : this.getSchema().getTypes().entrySet()) {
                final Class<?> requiredType = entry.getValue();
                final Class<?> candidate = submission.getType(entry.getKey());
                if (candidate == null) {
                    return false;
                }
                if (!requiredType.isAssignableFrom(candidate)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof SchemaTypePredicate<?>)) {
                return false;
            }
            final SchemaTypePredicate<?> other = (SchemaTypePredicate<?>) obj;
            if (!this.getSchema().equals(other.getSchema())) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int result = 3;
            result = 31 * result + this.getSchema().hashCode();
            return result;
        }

        @Override
        public String toString() {
            return String.format("SchemaTypePredicate[%s]", this.getSchema());
        }

    }

}