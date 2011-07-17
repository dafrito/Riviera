package com.bluespot.geom.algebra;

import com.bluespot.logic.functions.Function;
import com.bluespot.logic.functions.curryable.Curryable;

public interface NumericOperation extends Curryable<Number, Function<Number, ? extends Number>> {
	public double operate(double a, double b);

	public boolean allowOperationByZero();

	public boolean isCommutative();
}
