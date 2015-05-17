package conversation;

/**
 * Represents a unique endpoint of information. Examples of speakers would be
 * individuals in a IRC conversation, IP addresses in a network conversation,
 * and so forth.
 * 
 * @author Aaron Faanes
 * 
 * @param <E>
 *            the type of message with which this speaker communicates
 */
public interface Speaker<E extends Message<E>> {

	/**
	 * Returns the logical name of this speaker.
	 * <p>
	 * This is required to be unique for a given {@code Conversation}, but the
	 * format is unique to that type of conversation.
	 * 
	 * @return the unique identifier for this {@code Speaker} object
	 */
	public String getId();
}
