package com.bluespot.collections.table;

import com.bluespot.collections.table.iteration.ColumnarTableIteration;
import com.bluespot.collections.table.iteration.TableIteration;
import com.bluespot.geom.vectors.Vector3i;

public class ColumnarTableIterationTest extends AbstractTableIterationTest<Integer> {

	@Override
	protected Vector3i getExtraColumn() {
		return Vector3i.origin();
	}

	@Override
	protected Vector3i getExtraRow() {
		return Vector3i.frozen(1, 0);
	}

	@Override
	protected Vector3i getOneColumnBeforeOrigin() {
		return Vector3i.frozen(1, 0);
	}

	@Override
	protected Vector3i getOneRowBeforeOrigin() {
		return Vector3i.frozen(1, 1);
	}

	@Override
	protected Vector3i getThreeColumnsBeforeOrigin() {
		return Vector3i.frozen(1, 0);
	}

	@Override
	protected Vector3i getThreeRowsBeforeOrigin() {
		return Vector3i.frozen(0, 1);
	}

	@Override
	protected Vector3i getTwoColumnsAfterEnd() {
		return Vector3i.frozen(1, 0);
	}

	@Override
	protected Vector3i getTwoRowsAfterEnd() {
		return Vector3i.frozen(1, 1);
	}

	@Override
	protected TableIteration newTableIteration() {
		return ColumnarTableIteration.getInstance();
	}
}
