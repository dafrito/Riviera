package com.bluespot.logic.values;

import com.bluespot.logic.functions.Function;
import com.bluespot.logic.functions.SafeFunction;

public class FunctionValue<I, T> implements Value<T>, SafeFunction<T> {

	private final Function<? super I, ? extends T> function;
	private final I constant;

	public FunctionValue(final Function<? super I, ? extends T> function, final I constant) {
		if (function == null) {
			throw new NullPointerException("function must not be null");
		}
		this.function = function;
		this.constant = constant;
	}

	public Function<? super I, ? extends T> getFunction() {
		return this.function;
	}

	public I getConstant() {
		return this.constant;
	}

	@Override
	public T get() {
		return this.getFunction().apply(this.constant);
	}

	@Override
	public T apply(Object input) {
		return this.get();
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof FunctionValue<?, ?>)) {
			return false;
		}
		final FunctionValue<?, ?> other = (FunctionValue<?, ?>) obj;
		if (!this.getFunction().equals(other.getFunction())) {
			return false;
		}
		if (!this.getConstant().equals(other.getConstant())) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int result = 29;
		result = 31 * result + this.getFunction().hashCode();
		result = 31 * result + this.getConstant().hashCode();
		return result;
	}

	@Override
	public String toString() {
		return String.format("FunctionValue[function: %s, constant: %s]", this.getFunction(), this.getConstant());
	}
}
