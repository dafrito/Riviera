package com.bluespot.playground.game;

import java.util.Set;

/**
 * Represents an environment of {@link Actor} objects.
 * 
 * @author Aaron Faanes
 * 
 */
public interface Environment {

    /**
     * Returns a set of neighbors that are near the specified actor. The
     * returned set may not be modified.
     * 
     * @param actor
     *            the actor that is tested.
     * @return a set of neighbors that are close to the specified actor. If no
     *         actors are nearby, an empty set is returned.
     * @throws IllegalStateException
     *             if the specified actor is not contained within this
     *             environment
     */
    Set<Actor> getNeighbors(Actor actor);

}
