package com.bluespot.util;

public class Counter {
	private int count;

	public Counter() {
		this(0);
	}

	public Counter(final int start) {
		this.count = start;
	}

	public int getCount() {
		return this.count;
	}

	public int increment() {
		return this.count++;
	}
}
