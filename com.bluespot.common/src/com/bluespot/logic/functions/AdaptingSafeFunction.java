package com.bluespot.logic.functions;

import com.bluespot.logic.adapters.Adapter;

public class AdaptingSafeFunction<I, V> extends AdaptingFunction<Object, I, V> implements SafeFunction<V> {

	public AdaptingSafeFunction(Adapter<Object, ? extends I> adapter, Function<? super I, ? extends V> function) {
		super(adapter, function);
	}

}
