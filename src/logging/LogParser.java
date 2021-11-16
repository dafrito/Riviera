package logging;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class SenderReference {
	private final Object reference;
	private final Object name;

	public SenderReference(Object reference, Object name) {
		this.reference = reference;
		this.name = name;
	}

	public Object getReference() {
		return reference;
	}

	@Override
	public String toString() {
		return name.toString();
	}

	@Override
	public int hashCode() {
		return reference.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof SenderReference)) {
			return false;
		}
		SenderReference other = (SenderReference) obj;
		return reference.equals(other.getReference());
	}
}

public class LogParser {
	private static final String space = "\\s*";
    // >>> timestamp (category) [sender]@0x123abc message
	private static final Pattern PATTERN = Pattern.compile(
			"^"
					+ space + "(<+|>+|!+|@+)?" // scope
					+ space + "(\\d+)?" // timestamp
					+ space + "(?: \\(+" + "([^)]*)" + "\\)+ )?" // category
					+ space + "(?: \\[+" + "([^\\]]*)" + "\\]+(?:@(?:0x)?([0-9a-fA-F]+))?)?" // sender and sender id
					+ space + "(.+)?" // message
					+ "$",
			Pattern.COMMENTS
			);

	private final int SCOPE = 1;
	private final int TIMESTAMP = 2;
	private final int CATEGORY = 3;
	private final int SENDER = 4;
	private final int SENDER_ID = 5;
	private final int MESSAGE = 6;

	private enum ScopeAction {
		NONE,
		ENTER,
		LEAVE,
		RESET,
	};

	public boolean readLine(TreeLog log, String line) {
		Matcher matcher = PATTERN.matcher(line);
		if (!matcher.matches()) {
            throw new IllegalArgumentException("log line failed to parse");
		}

        if (line.equals("CLOSE")) {
			return false;
        }

		long timestamp = System.currentTimeMillis();
		String category = matcher.group(CATEGORY);
		Object sender = matcher.group(SENDER);
		Object senderId = matcher.group(SENDER_ID);

		String message = matcher.group(MESSAGE);
		ScopeAction action = ScopeAction.NONE;

		if (matcher.group(TIMESTAMP) != null) {
			timestamp = Long.valueOf(matcher.group(TIMESTAMP));
		}

		if (senderId != null) {
			sender = new SenderReference(senderId, sender);
		}

		if (matcher.group(SCOPE) != null) {
			switch (matcher.group(SCOPE).charAt(0)) {
			case '>':
				action = ScopeAction.ENTER;
				break;
			case '<':
				action = ScopeAction.LEAVE;
				break;
			case '!':
				action = ScopeAction.RESET;
				break;
			default:
				throw new AssertionError("Impossible (I probably botched the regex)");
			}
		}

		LogMessage<String> logMessage = new LogMessage<String>(timestamp, sender, category, message);

		switch (action) {
		case ENTER:
			log.enter(logMessage);
			break;
		case NONE:
			log.log(logMessage);
			break;
		case LEAVE:
			if (category != null || message != null) {
				log.log(logMessage);
			}
			log.leave();
			break;
		case RESET:
			if (category != null || message != null) {
				log.log(logMessage);
			}
			log.reset();
			break;
		}

        return true;
	}
}
