/**
 * 
 */
package geom.algebra;

/**
 * The set of common algebraic operations.
 * 
 * @author Aaron Faanes
 */
public final class AlgebraicOperations {

	@SuppressWarnings("rawtypes")
	private static final AlgebraicOperation ADDITION = new AlgebraicOperation() {
		@SuppressWarnings("unchecked")
		@Override
		public void operate(Algebraic a, Algebraic b) {
			a.add(b);
		}

		@Override
		public String toString() {
			return "Addition";
		}
	};

	@SuppressWarnings("unchecked")
	public static <V extends Algebraic<V>> AlgebraicOperation<V> addition() {
		return ADDITION;
	}

	@SuppressWarnings("rawtypes")
	private static final AlgebraicOperation SUBTRACTION = new AlgebraicOperation() {
		@SuppressWarnings("unchecked")
		@Override
		public void operate(Algebraic a, Algebraic b) {
			a.subtract(b);
		}

		@Override
		public String toString() {
			return "Subtraction";
		}
	};

	@SuppressWarnings("unchecked")
	public static <V extends Algebraic<V>> AlgebraicOperation<V> subtraction() {
		return SUBTRACTION;
	}

	@SuppressWarnings("rawtypes")
	private static final AlgebraicOperation MULTIPLCATION = new AlgebraicOperation() {
		@SuppressWarnings("unchecked")
		@Override
		public void operate(Algebraic a, Algebraic b) {
			a.multiply(b);
		}

		@Override
		public String toString() {
			return "Multiplication";
		}
	};

	@SuppressWarnings("unchecked")
	public static <V extends Algebraic<V>> AlgebraicOperation<V> multiplication() {
		return MULTIPLCATION;
	}

	@SuppressWarnings("rawtypes")
	private static final AlgebraicOperation DIVISION = new AlgebraicOperation() {
		@SuppressWarnings("unchecked")
		@Override
		public void operate(Algebraic a, Algebraic b) {
			a.divide(b);
		}

		@Override
		public String toString() {
			return "Division";
		}
	};

	@SuppressWarnings("unchecked")
	public static <V extends Algebraic<V>> AlgebraicOperation<V> division() {
		return DIVISION;
	}

	@SuppressWarnings("rawtypes")
	private static final AlgebraicOperation SETTER = new AlgebraicOperation() {
		@SuppressWarnings("unchecked")
		@Override
		public void operate(Algebraic a, Algebraic b) {
			a.set(b);
		}

		@Override
		public String toString() {
			return "Set";
		}
	};

	@SuppressWarnings("unchecked")
	public static <V extends Algebraic<V>> AlgebraicOperation<V> setter() {
		return SETTER;
	}
}
