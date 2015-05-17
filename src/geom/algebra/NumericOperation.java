package geom.algebra;

import logic.functions.Function;
import logic.functions.curryable.Curryable;

public interface NumericOperation extends Curryable<Number, Function<Number, ? extends Number>> {
	public double operate(double a, double b);

	public boolean allowOperationByZero();

	public boolean isCommutative();
}
