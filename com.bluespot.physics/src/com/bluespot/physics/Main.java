package com.bluespot.physics;

public class Main {
    public static void main(final String[] args) {
        final Ball ball = new MasslessObject();
        final Ball otherBall = new MasslessObject();
        final Connection connection = new UnbreakableConnection(ball, otherBall);

    }
}

class MasslessObject implements Ball {

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

    private final Ball object;
    private final Ball otherObject;

    public UnbreakableConnection(final Ball ball, final Ball otherBall) {
        this.object = ball;
        this.otherObject = otherBall;
    }

    public Ball getOtherObject() {
        return this.otherObject;
    }

    public Ball getObject() {
        return this.object;
    }
}
