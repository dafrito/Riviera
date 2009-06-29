package com.bluespot.physics;
/**
 * Represents an attraction or connection between some objects.
 * 
 * <ul>
 * <li>Is it a direct connection between objects. If note, gravity could be
 * represented as a connection. If so, does this make more sense to call this an
 * attraction?
 * <li>Does it have to be between two objects. Again, gravity seems like it's a
 * one-to-many thing, where many objects are attracted to some source. What
 * about two objects that are pulling against one another, like tug o' war. Is
 * that a composite connection?
 * <li>In the tug o' war idea, it seems like there are two competing, composite
 * forces.
 * <li>How do we deal with broken connections? Imagine if a connection suddenly
 * breaks, like a rope unable to hold its weight. In this case, what is the
 * connection? It seems like we're more interested in forces and objects than
 * connections, which represent a combination of the two. In the broken-rope
 * example, the object is broken, but the forces essentially remain.
 * </ul>
 */
public interface Connection {
    /*
     * TODO Implement this as necessary. This should likely be Force or
     * Attraction.
     */
}
