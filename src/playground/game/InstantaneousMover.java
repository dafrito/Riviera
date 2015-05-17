/**
 * 
 */
package playground.game;

import geom.vectors.Vector3;

/**
 * A trivial position delegate that instantaneously moves its position. It does
 * not collide with other positions.
 * 
 * @author Aaron Faanes
 * 
 */
public class InstantaneousMover<V extends Vector3<V>> {
	private final V position;

	public InstantaneousMover(final V position) {
		if (position == null) {
			throw new NullPointerException("position must not be null");
		}
		this.position = position.toMutable();
	}

	public V getPosition() {
		return this.position.toMutable();
	}

	public void setPosition(final V position) {
		this.position.set(position);
	}

}
