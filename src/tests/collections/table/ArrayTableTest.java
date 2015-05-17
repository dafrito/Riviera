package collections.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayTableTest extends AbstractTableTest<Integer> {

	public ArrayTableTest() {
		super(0);
	}

	@Override
	public Table<Integer> newTable(final int width, final int height, final Integer defaultValue) {
		return new ArrayTable<Integer>(width, height, defaultValue);
	}

	@Override
	protected boolean allowNullValues() {
		return true;
	}

	@Override
	protected Integer getOtherValue() {
		return 2;
	}

	@Override
	protected Integer getValue() {
		return 1;
	}

	@Override
	protected List<Integer> listOfValues() {
		return new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4));
	}

	@Override
	protected List<Integer> otherListOfValues() {
		return new ArrayList<Integer>(Arrays.asList(10, 11, 12, 13));
	}

}
