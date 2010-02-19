package com.bluespot.physics;

public class Main {
    public static void main(final String[] args) {
        final Physical ball = new MasslessObject();
        final Physical otherBall = new MasslessObject();
        new UnbreakableConnection(ball, otherBall);
    }
}

class MasslessObject implements Physical {
    // Experimental.
}

/**
 * Represents an unbreakable, god-like connection. If we decide instead to
 * implement connections as forces or attractions, this won't make as much
 * sense. Instead, the force would just be another force, and it would be the
 * object that sustains the connection that is of infinite strength.
 * <p>
 * See {@link Connection} for more details on what this class should actually
 * be.
 */
class UnbreakableConnection implements Connection {

    private final Physical object;
    private final Physical otherObject;

    public UnbreakableConnection(final Physical ball, final Physical otherBall) {
        this.object = ball;
        this.otherObject = otherBall;
    }

    public Physical getOtherObject() {
        return this.otherObject;
    }

    public Physical getObject() {
        return this.object;
    }
}
