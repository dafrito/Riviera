package com.bluespot.logic.functions;

import com.bluespot.geom.algebra.NumericOperations;
import com.bluespot.logic.functions.curryable.Curryable;

public class NumericFunction implements Function<Number, Number>, Curryable<Number, Function<? super Number, ? extends Number>> {

	private final NumericOperations operation;
	private final double constant;

	public NumericFunction(NumericOperations operation, Number constant) {
		if (operation == null) {
			throw new NullPointerException("operation must not be null");
		}
		if (constant == null) {
			throw new NullPointerException("constant must not be null");
		}
		this.operation = operation;
		this.constant = constant.doubleValue();
		if (!this.operation.allowOperationByZero() && this.constant == 0.0d) {
			throw new ArithmeticException("constant cannot be zero for the provided operation");
		}
	}

	@Override
	public Number apply(Number input) {
		if (input == null) {
			return null;
		}
		return operation.operate(input.doubleValue(), constant);
	}

	@Override
	public NumericFunction curry(Number value) {
		if (value == null) {
			return null;
		}
		return new NumericFunction(this.operation, this.operation.operate(value.doubleValue(), this.constant));
	}

	public NumericOperations getOperation() {
		return this.operation;
	}

	public Double getConstant() {
		return this.constant;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof NumericFunction)) {
			return false;
		}
		final NumericFunction other = (NumericFunction) obj;
		return this.getOperation().equals(other.getOperation()) &&
				this.getConstant().equals(other.getConstant());
	}

	@Override
	public int hashCode() {
		int result = 11;
		result *= 31 * result + getOperation().hashCode();
		result *= 31 * result + getConstant().hashCode();
		return result;
	}

	@Override
	public String toString() {
		return String.format("NumericFunction[%s %s]", getOperation(), getConstant());
	}

}
