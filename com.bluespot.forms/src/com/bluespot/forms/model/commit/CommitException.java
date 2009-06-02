package com.bluespot.forms.model.commit;

public class CommitException extends RuntimeException {
    
    public static class NoCommittedValueException extends CommitException {
        public static final String MESSAGE = "The committed value is invalid or missing";
        
        public NoCommittedValueException(Committable<?> source, Throwable cause) {
            super(source, MESSAGE, cause);
        }
        
        public NoCommittedValueException(Committable<?> source) {
            this(source, null);
        }
    }
    
    public static class PendingValueException extends CommitException {
        public static final String MESSAGE = "The pending value is invalid or missing"; 

        public PendingValueException(Committable<?> source, Throwable cause) {
            super(source, MESSAGE, cause);
        }
        
        public PendingValueException(Committable<?> source) {
            this(source, null);
        }
    }

    private final Committable<?> source;
    
    public CommitException(Committable<?> source, String message, Throwable cause) {
        super(message, cause);
        this.source = source;
    }

    public CommitException(Committable<?> source, String message) {
        this(source, message, null);
    }

    public Committable<?> getSource() {
        return this.source;
    }
}