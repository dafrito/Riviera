package playground.game;

/**
 * Represents a team for which {@link Actor} objects may affiliate.
 * 
 * @author Aaron Faanes
 * 
 */
public interface Team {

	/**
	 * The possible levels of animosity between two teams.
	 * 
	 * @author Aaron Faanes
	 * 
	 */
	public static enum Relation {

		/**
		 * A degenerate relationship that says a comparison was made with a
		 * logically equal team.
		 * 
		 */
		SELF,

		/**
		 * Friendly relations. Teams in this state are friendly towards one
		 * another and computer-controlled defenses will attempt to protect both
		 * teams without prejudice.
		 */
		FRIENDLY,
		/**
		 * Hostile relations. Teams in this state are openly aggressive towards
		 * one another.
		 */
		HOSTILE
	}

	/**
	 * Returns the relationship between this team and the other team. Teams are
	 * required for this relationship to share the same strictness as
	 * {@link Object#equals(Object)}. Specifically, it must be transitive,
	 * symmetric, and consistent.
	 * 
	 * @param other
	 *            the other team that is tested by this method
	 * @return the relationship between this team and the specified other team.
	 *         If {@code other} is equal to this team, {@link Relation#SELF} is
	 *         returned.
	 * @throws NullPointerException
	 *             if {@code other} is null
	 */
	public Relation getRelations(Team other);
}
