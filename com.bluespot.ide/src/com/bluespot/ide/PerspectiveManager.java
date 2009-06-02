package com.bluespot.ide;

import com.bluespot.swing.list.ProxiedListModel;
import com.bluespot.swing.state.StateModel;

/**
 * A container for {@link Perspective}'s, notifying listeners of changes through
 * a {@link StateModel}.
 * 
 * @author Aaron Faanes
 */
public class PerspectiveManager {

	protected final ProxiedListModel<Perspective> perspectives = new ProxiedListModel<Perspective>();
	protected final StateModel<Perspective> stateModel = new StateModel<Perspective>(this.perspectives);

	/**
	 * A newly created manager will assume the 'currentManager' position by
	 * default, if there isn't one already registered.
	 */
	public PerspectiveManager() {
		if (PerspectiveManager.getCurrentManager() == null) {
			PerspectiveManager.setCurrentManager(this);
		}
	}

	private static PerspectiveManager currentManager;

	public static PerspectiveManager getCurrentManager() {
		return currentManager;
	}

	public static void setCurrentManager(PerspectiveManager manager) {
		if (manager == null)
			throw new NullPointerException();
		PerspectiveManager.currentManager = manager;
	}

	public ProxiedListModel<Perspective> getPerspectives() {
		return this.perspectives;
	}

	public StateModel<Perspective> getStateModel() {
		return this.stateModel;
	}

	public void addPerspective(Perspective perspective) {
		this.perspectives.add(perspective);
	}

	public void removePerspective(Perspective perspective) {
		this.perspectives.remove(perspective);
	}

	public void showPerspective(Perspective perspective) {
		this.stateModel.setState(perspective);
	}

	public Perspective getCurrentPerspective() {
		return this.stateModel.getState();
	}

	public boolean closeAll() {
		for (Perspective perspective : this.perspectives) {
			if (!perspective.isReadyForClose())
				return false;
		}
		for (Perspective perspective : this.perspectives) {
			perspective.close();
		}
		return true;
	}

}
