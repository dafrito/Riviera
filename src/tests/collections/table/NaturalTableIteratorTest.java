/**
 * 
 */
package collections.table;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.util.ArrayDeque;
import java.util.Deque;

import org.junit.Before;
import org.junit.Test;

import collections.table.iteration.NaturalTableIterator;
import collections.table.iteration.TableIterator;

/**
 * @author Aaron Faanes
 * 
 */
public class NaturalTableIteratorTest extends AbstractTableIteratorTest {

	private final Deque<Color> colors = new ArrayDeque<Color>();

	@Before
	public void setUpDeque() {
		super.setUp();
		colors.clear();
		colors.addLast(Color.white);
		colors.addLast(Color.lightGray);
		colors.addLast(Color.darkGray);
		colors.addLast(Color.black);
	}

	@Override
	public TableIterator<Integer> newIterator(Table<Integer> targetTable) {
		return new NaturalTableIterator<Integer>(targetTable);
	}

	@Test
	public void naturalIteratorIteratesInReadingOrder() throws Exception {
		Table<Color> table = new ArrayTable<Color>(2, 2);
		TableIterator<Color> iter = new NaturalTableIterator<Color>(table);
		while (iter.hasNext()) {
			iter.next();
			iter.put(colors.removeFirst());
		}
		assertEquals(Color.white, table.get(0, 0));
		assertEquals(Color.lightGray, table.get(1, 0));
		assertEquals(Color.darkGray, table.get(0, 1));
		assertEquals(Color.black, table.get(1, 1));
	}
}
