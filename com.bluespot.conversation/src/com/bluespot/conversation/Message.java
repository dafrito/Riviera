package com.bluespot.conversation;

/**
 * Represents a single logical unit of information. Subclasses typically adapt
 * some more primitive form of {@code Message} type to a more high-level one.
 * For example, protocol translators would expect network messages, and
 * translate them into protocol-specific messages.
 * <p>
 * Currently, we handle service messages through
 * {@link #hasExplicitDestination()}. This is something of a compromise since
 * we'd ideally have a TargetedMessage interface for these cases but my fear is
 * that every protocol would have this split. As protocols are implemented, this
 * may turn out not to be a problem.
 * 
 * 
 * @author Aaron Faanes
 * 
 * @param <E>
 *            the type of message. This should be a recursive generic type. For
 *            example, {@code FooMessage<FooMessage>} would be the signature for
 *            {@code FooMessage} objects.
 */
public interface Message<E extends Message<E>> {

    /**
     * Returns the originating source of this message.
     * 
     * @return the source of this message
     */
    public Speaker<? super E> getSource();

    /**
     * Returns whether this message has an explicit destination. Specifically,
     * this occurs if the message was intended for a group of people or simply a
     * notification of some activity that's intended to be observable by others.
     * <p>
     * IRC clients typically send messages to a room, instead of a person. In
     * this case, the message has no explicit destination; its logical target is
     * everyone in the room.
     * <p>
     * Service, or meta-messages, which indicate activity but do not fit into
     * the same level as the conversation, also have no explicit destination.
     * 
     * @return whether this message has a destination. If {@code true}, then
     *         {@link #getDestination()} has no meaningful value. If {@code
     *         false}, then the message was explicitly directed at that
     *         destination.
     */
    public boolean hasExplicitDestination();

    /**
     * Returns the target of this message.
     * <p>
     * If {@link #hasExplicitDestination()} returns {@code false}, then this
     * must return {@code null} since the message is intended to be received by
     * all participants of that conversation.
     * 
     * @return the target of this message.
     */
    public Speaker<? super E> getDestination();

    /**
     * @return the {@code Conversation} in which this message took place
     */
    public Conversation<E> getConversation();

    /**
     * @return this message, properly cast to its generic type. This allows
     *         clients to use this message at a higher level without having to
     *         use explicit casts.
     */
    public E getThis();
}
