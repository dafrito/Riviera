package solver;

public interface SolverListener<T> {

	void onSolution(String result);

	void finished();

}
