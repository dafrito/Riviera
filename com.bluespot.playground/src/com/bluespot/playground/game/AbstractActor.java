package com.bluespot.playground.game;

/**
 * A skeletal {@link Actor} implementation.
 * 
 * @author Aaron Faanes
 * 
 */
public abstract class AbstractActor implements Actor {
	private Team team;

	/**
	 * Constructs an {@link AbstractActor} that is housed in the specified
	 * environment and is affiliated with the specified team.
	 * 
	 * @param team
	 *            the team that is affiliated with this actor. It must not be
	 *            null.
	 * @throws NullPointerException
	 *             if either argument is null
	 */
	public AbstractActor(final Team team) {
		this.setTeam(team);
	}

	@Override
	public Team getTeam() {
		return this.team;
	}

	/**
	 * Sets the team for this actor. The team is immediately changed.
	 * <p>
	 * As an implementation detail, this is currently protected, but subclasses
	 * may elevate its scope if they see fit.
	 * 
	 * @param team
	 *            the new team. It must not be null.
	 * @throws NullPointerException
	 *             if {@code team} is null
	 */
	protected void setTeam(final Team team) {
		if (team == null) {
			throw new NullPointerException("team is null");
		}
		this.team = team;
	}

}
