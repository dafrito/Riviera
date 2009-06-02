package com.bluespot.forms.model.commit;

public class Commit<E> {
    private final Committable<E> source;
    private final E oldValue;
    private final E newValue;
    
    public Commit(Committable<E> source, E oldValue, E newValue) {
        this.source = source;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
    
    public Committable<E> getSource() {
        return this.source;
    }

    public E getOldValue() {
        return this.oldValue;
    }

    public E getNewValue() {
        return this.newValue;
    }

}
