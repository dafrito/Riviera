package com.bluespot.logic.functions;

public enum NumericOperations implements NumericOperation {
	ADD {
		@Override
		public double operate(double a, double b) {
			return a + b;
		}
	},
	SUBTRACT(false, true) {
		@Override
		public double operate(double a, double b) {
			return a - b;
		}
	},
	MULTIPLY {
		@Override
		public double operate(double a, double b) {
			return a * b;
		}
	},
	DIVIDE(false, false) {
		@Override
		public double operate(double a, double b) {
			return a / b;
		}
	},
	MODULUS(false, false) {
		@Override
		public double operate(double a, double b) {
			return a % b;
		}
	};

	private final boolean allowOperationByZero;
	private final boolean isCommutative;

	private NumericOperations() {
		this(true, true);
	}

	private NumericOperations(boolean isCommutative, boolean allowOperationByZero) {
		this.allowOperationByZero = allowOperationByZero;
		this.isCommutative = isCommutative;
	}

	@Override
	public Function<? super Number, ? extends Number> curry(Number value) {
		if (value == null) {
			return null;
		}
		if (!this.allowOperationByZero() && value.doubleValue() == 0.0d) {
			return null;
		}
		return new NumericFunction(this, value);
	}

	@Override
	public abstract double operate(double a, double b);

	@Override
	public boolean allowOperationByZero() {
		return allowOperationByZero;
	}

	@Override
	public boolean isCommutative() {
		return isCommutative;
	}

}
