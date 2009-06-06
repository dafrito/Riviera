package com.bluespot.ide;

import com.bluespot.collections.observable.list.ObservableList;
import com.bluespot.collections.observable.state.ObservableListStateModel;
import com.bluespot.collections.observable.state.StateModel;

/**
 * A container for {@link Perspective}'s, notifying listeners of changes through
 * a {@link StateModel}.
 * 
 * @author Aaron Faanes
 */
public class PerspectiveManager {

	protected final ObservableList<Perspective> perspectives = new ObservableList<Perspective>();

	protected final StateModel<Perspective> stateModel = new ObservableListStateModel<Perspective>(this.perspectives);

	/**
	 * A newly created manager will assume the 'currentManager' position by
	 * default, if there isn't one already registered.
	 */
	public PerspectiveManager() {
		if (PerspectiveManager.getCurrentManager() == null) {
			PerspectiveManager.setCurrentManager(this);
		}
	}

	public void addPerspective(final Perspective perspective) {
		this.perspectives.add(perspective);
	}

	public boolean closeAll() {
		for (final Perspective perspective : this.perspectives) {
			if (!perspective.isReadyForClose()) {
				return false;
			}
		}
		for (final Perspective perspective : this.perspectives) {
			perspective.close();
		}
		return true;
	}

	public Perspective getCurrentPerspective() {
		return this.stateModel.getState();
	}

	public ObservableList<Perspective> getPerspectives() {
		return this.perspectives;
	}

	public StateModel<Perspective> getStateModel() {
		return this.stateModel;
	}

	public void removePerspective(final Perspective perspective) {
		this.perspectives.remove(perspective);
	}

	public void showPerspective(final Perspective perspective) {
		this.stateModel.changeState(perspective);
	}

	public static PerspectiveManager getCurrentManager() {
		return PerspectiveManager.currentManager;
	}

	public static void setCurrentManager(final PerspectiveManager manager) {
		if (manager == null) {
			throw new NullPointerException();
		}
		PerspectiveManager.currentManager = manager;
	}

	private static PerspectiveManager currentManager;

}
