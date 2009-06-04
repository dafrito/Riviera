package com.bluespot.collections.table;

import java.awt.Point;

import com.bluespot.collections.table.iteration.ColumnarTableIteration;
import com.bluespot.collections.table.iteration.TableIteration;

public class ColumnarTableIterationTest extends TableIterationTest<Integer> {

	@Override
	protected Point getExtraColumn() {
		return new Point(0, 0);
	}

	@Override
	protected Point getExtraRow() {
		return new Point(1, 0);
	}

	@Override
	protected Point getOneColumnBeforeOrigin() {
		return new Point(1, 0);
	}

	@Override
	protected Point getOneRowBeforeOrigin() {
		return new Point(1, 1);
	}

	@Override
	protected Point getThreeColumnsBeforeOrigin() {
		return new Point(1, 0);
	}

	@Override
	protected Point getThreeRowsBeforeOrigin() {
		return new Point(0, 1);
	}

	@Override
	protected Point getTwoColumnsAfterEnd() {
		return new Point(1, 0);
	}

	@Override
	protected Point getTwoRowsAfterEnd() {
		return new Point(1, 1);
	}

	@Override
	protected TableIteration newTableIteration() {
		return ColumnarTableIteration.getInstance();
	}
}
