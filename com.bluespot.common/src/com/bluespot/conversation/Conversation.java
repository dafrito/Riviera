package com.bluespot.conversation;

import java.util.Set;

/**
 * Represents a logical grouping of {@link Speaker} objects. Conversations may
 * be derived from some lower level of conversation. For example, network
 * communications represent a conversation, with packets being the messages.
 * This global "conversation" is isolated to a network interface. Listeners to
 * this conversation may, however, recognize sub-conversations taking place, and
 * are encouraged to create their own {@code Conversation} implementations to
 * reflect this. Through the derivations of conversations, more and more
 * sophisticated translations of messages can occur.
 * 
 * @author Aaron Faanes
 * 
 * @param <E>
 *            the type of message expected by this conversation
 */
public interface Conversation<E extends Message<E>> {

	/**
	 * Adds a listener to this conversation.
	 * 
	 * @param listener
	 *            the listener to add
	 */
	public void addConversationListener(ConversationListener<? super E> listener);

	/**
	 * Removes a listener from this conversation.
	 * 
	 * @param listener
	 *            the listener to remove
	 */
	public void removeConversationListener(ConversationListener<? super E> listener);

	/**
	 * Returns all speakers that are currently involved in this conversation.
	 * <p>
	 * What defines being "involved" depends on the implementation. Network
	 * conversations may simply use timeouts to periodically delete inactive
	 * speakers.
	 * 
	 * @return the group of active speakers
	 */
	public Set<Speaker<? extends E>> getSpeakers();
}
