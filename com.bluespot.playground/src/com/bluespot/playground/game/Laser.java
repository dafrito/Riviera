package com.bluespot.playground.game;

import com.bluespot.playground.game.Team.Relation;

/**
 * A {@link Actor} that guards a position. By default, it shoots as often as
 * possible. It will, however, deactivate if friendly actors are nearby. If both
 * friendly and hostile targets are nearby, it will deactivate. The natural
 * consequence is that a laser may not totally deny hostile targets access to
 * this laser's guarded area.
 * 
 * @author Aaron Faanes
 * 
 */
public class Laser extends AbstractActor {

	private boolean shooting = true;

	/**
	 * Constructs a {@link Laser} using the specified environment and team. The
	 * laser is initially being shot.
	 * 
	 * @param environment
	 *            the environment that houses this laser. It must not be null.
	 * @param team
	 *            the team that this laser is affiliated with. It must not be
	 *            null.
	 * @throws NullPointerException
	 *             if either argument is null
	 */
	public Laser(final Environment environment, final Team team) {
		super(environment, team);
	}

	/**
	 * Returns whether this laser is currently firing. This may be in an
	 * unreliable state if this method is used while the laser itself is
	 * iterating.
	 * 
	 * @return {@code true} if this laser is currently shooting
	 */
	public boolean isShooting() {
		return this.shooting;
	}

	/**
	 * Sets whether the laser is currently firing. This may be set many times
	 * during an iteration, but only the most recent change is used to actually
	 * fire or not fire the laser.
	 * 
	 * @param shooting
	 *            whether the laser is being fired
	 */
	protected void setShooting(final boolean shooting) {
		this.shooting = shooting;
	}

	@Override
	public void update() {
		// By default, we shoot the laser.
		this.setShooting(true);
		for (final Actor neighbor : this.getEnvironment().getNeighbors(this)) {
			final Relation relation = this.getTeam().getRelations(neighbor.getTeam());
			if (relation == Team.Relation.FRIENDLY) {
				/*
				 * Don't shoot friendly actors; as a consequence, hostile actors
				 * may be allowed through.
				 */
				this.setShooting(false);
				break;
			}
		}
	}
}
