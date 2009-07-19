package com.bluespot.forms;

public interface Submission<K> {

    public Class<?> getType(final K keyValue);

    public <T> T get(final K keyValue, final Class<T> type);
}
