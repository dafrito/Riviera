package com.bluespot.conversation;

import java.awt.event.WindowListener;

/**
 * A listener that can receive notifications from a given {@link Conversation}.
 * 
 * @author Aaron Faanes
 * 
 * @param <E>
 *            the type of messages in the conversation
 * @see Conversation
 */
public interface ConversationListener<E extends Message<E>> extends WindowListener {

	/**
	 * Invoked when a {@link Speaker} enters a conversation. Note that the terms
	 * of when a {@code Speaker} enters are dependent on the type of
	 * conversation.
	 * 
	 * @param speaker
	 *            the speaker that entered this conversation
	 */
	public void speakerEntered(Speaker<E> speaker);

	/**
	 * Invoked when a conversation-wide message is received.
	 * 
	 * @param message
	 *            the message that was received
	 */
	public void receiveMessage(E message);

	/**
	 * Invoked when a message with an explicit destination is received.
	 * 
	 * @param message
	 *            the message that was received
	 */
	public void receiveTargetedMessage(E message);

	/**
	 * Invoked when a {@link Speaker} leaves some conversation. Note that the
	 * terms of when a {@code Speaker} leaves are dependent on the type of
	 * conversation.
	 * 
	 * @param speaker
	 *            the speaker that left this conversation
	 */
	public void speakerLeft(Speaker<E> speaker);
}
