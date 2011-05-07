package com.bluespot.logic.adapters;

import java.util.Map;

/**
 * A {@link Adapter} that retrieves a value using the specified key from a given
 * {@link Map}.
 * 
 * @author Aaron Faanes
 * 
 * @param <K>
 *            the type of keys maintained by maps given to this adapter
 * @param <V>
 *            the type of values mapped in maps given to this adapter
 */
public final class MapEntryAdapter<K, V> implements Adapter<Map<? super K, ? extends V>, V> {

	private final K key;

	/**
	 * Constructs a {@link MapEntryAdapter} that uses the specified key.
	 * 
	 * @param key
	 *            the key that is used to retrieve a value from a given
	 *            {@link Map}
	 */
	public MapEntryAdapter(final K key) {
		if (key == null) {
			throw new NullPointerException("key is null");
		}
		this.key = key;
	}

	@Override
	public V adapt(final Map<? super K, ? extends V> source) {
		if (source == null) {
			return null;
		}
		return source.get(this.getKey());
	}

	/**
	 * Returns the key used by this adapter for conversion.
	 * 
	 * @return the key used by this adapter for conversion
	 */
	public K getKey() {
		return this.key;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof MapEntryAdapter<?, ?>)) {
			return false;
		}
		final MapEntryAdapter<?, ?> other = (MapEntryAdapter<?, ?>) obj;
		if (!this.getKey().equals(other.getKey())) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + this.getKey().hashCode();
		return result;
	}

	@Override
	public String toString() {
		return String.format("get entry for '%s'", this.getKey());
	}

}
