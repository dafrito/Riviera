package com.bluespot.table.tests;

import java.awt.Point;

import com.bluespot.table.iteration.NaturalTableIteration;
import com.bluespot.table.iteration.TableIteration;

public class NaturalTableIterationTest extends TableIterationTest<Integer> {

    @Override
    protected TableIteration newTableIteration() {
        return new NaturalTableIteration();
    }
    
    @Override
    protected Point getExtraColumn() {
        return new Point(0, 1);
    }

    @Override
    protected Point getExtraRow() {
        return new Point(0, 0);
    }

    @Override
    protected Point getOneColumnBeforeOrigin() {
        return new Point(1, 1);
    }

    @Override
    protected Point getOneRowBeforeOrigin() {
        return new Point(0, 1);
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
        return new Point(1, 1);
    }

    @Override
    protected Point getTwoRowsAfterEnd() {
        return new Point(0, 1);
    }

}
