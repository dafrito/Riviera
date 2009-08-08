package com.bluespot.playground.game;

/**
 * A single tangible object that occupies space in an {@link Environment}. The
 * location of an actor is intentionally left vague: it is intended that actors
 * only house logic that is concerned with their behavior, and leave specifics
 * of their position to the environment in which they occur.
 * <p>
 * An actor may be a short-lived object, like a shot from a laser, or a much
 * longer-lasting object, such as a player, whose behavior is arbitrarily
 * complex.
 * 
 * @author Aaron Faanes
 * @see Environment
 */
public interface Actor {

    /**
     * Returns the environment that contains this actor. This should never
     * return null.
     * <p>
     * Note that this is not actually enforced by the actor; the environment is
     * solely responsible for whom it contains. Methods called on the
     * environment using this actor are validated first by the environment, and
     * illegal calls will result in a {@link IllegalStateException} being
     * thrown.
     * 
     * @return the environment that contains this actor
     */
    public Environment getEnvironment();

    /**
     * Perform one atomic iteration for this actor. At the end of this method,
     * all iteration-specific state should be set. It is assumed that during
     * this method, the environment is "locked" for this actor.
     */
    public void update();

    /**
     * Returns the team with which this actor affiliates. This should never
     * return null.
     * 
     * @return the team with which this actor affiliates
     */
    public Team getTeam();
}
