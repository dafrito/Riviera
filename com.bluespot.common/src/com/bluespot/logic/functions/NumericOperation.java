package com.bluespot.logic.functions;

public interface NumericOperation {
	public double operate(double a, double b);

	public boolean allowOperationByZero();

	public boolean isCommutative();
}
