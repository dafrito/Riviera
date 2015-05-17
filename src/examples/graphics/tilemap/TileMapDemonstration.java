/**
 * 
 */
package examples.graphics.tilemap;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import collections.table.ArrayTable;
import collections.table.Table;
import collections.table.iteration.TableIterator;
import demonstration.Demonstration;
import generate.Randomness;

/**
 * @author Aaron Faanes
 * 
 */
public class TileMapDemonstration extends Demonstration {

	public static void main(String[] args) {
		Demonstration.launch(TileMapDemonstration.class);
	}

	private final Table<Color> grid = new ArrayTable<Color>(100, 100);

	public TileMapDemonstration() {
		super();
		TableIterator<Color> iter = grid.tableIterator();
		while (iter.hasNext()) {
			iter.next();
			iter.put(Randomness.swatch());
		}
	}

	@Override
	protected Component newContentPane() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(800, 600));
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.setLayout(new CardLayout());
		TileMapPanel view = new TileMapPanel(grid);
		view.setFocusable(true);
		panel.add(view);
		return panel;
	}
}