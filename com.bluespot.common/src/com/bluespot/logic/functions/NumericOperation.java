package com.bluespot.logic.functions;

public interface NumericOperation extends Curryable<Number, Function<? super Number, ? extends Number>> {
	public double operate(double a, double b);

	public boolean allowOperationByZero();

	public boolean isCommutative();
}
