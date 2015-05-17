package dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Represents an immutable dictionary of words. This is not related to the
 * {@link java.util.Dictionary} class. This class provides methods to query the
 * dictionary, creating sub-dictionaries as a result.
 * <p>
 * This class more formally represents a set of strings; it makes no effort to
 * logically group strings, or remove strings that are mere variations of one
 * another.
 * 
 * @author Aaron Faanes
 * 
 */
public class Dictionary implements Iterable<String> {

	private final Set<String> words;

	private Set<String> filterEqualWords(Iterable<String> unfilteredWords) {
		Set<String> wordSet = new HashSet<String>();
		for (String word : unfilteredWords) {
			wordSet.add(word.toLowerCase());
		}
		return wordSet;
	}

	/**
	 * Easy way to make a small dictionary, useful for tests and
	 * specially-tailored searches.
	 * 
	 * @param words
	 *            words to go into the dictionary
	 */
	public Dictionary(final String... words) {
		this(Arrays.asList(words));
	}

	/**
	 * Constructs a {@link Dictionary} containing the specified words.
	 * 
	 * @param words
	 *            the words that will be contained in the constructed
	 *            {@code Dictionary}
	 */
	public Dictionary(final Iterable<String> words) {
		this.words = Collections.unmodifiableSet(this.filterEqualWords(words));
	}

	/**
	 * Returns an unmodifiable view of the words contained in this dictionary.
	 * 
	 * @return an unmodifiable view of the words contained in this dictionary
	 */
	public Set<String> getWords() {
		/*
		 * No need to wrap this with a Collections#unmodifiableSet() since it
		 * has already been wrapped.
		 */
		return this.words;
	}

	/**
	 * Returns a {@link Dictionary} that contains all words from this dictionary
	 * that match the specified pattern.
	 * 
	 * @param pattern
	 *            the discriminating pattern
	 * @return a sub-dictionary that contains words that matched the specified
	 *         pattern
	 */
	public Dictionary subDictionary(final Pattern pattern) {
		final Set<String> subWords = new HashSet<String>();
		for (final String word : this.words) {
			if (pattern.matcher(word).matches()) {
				subWords.add(word);
			}
		}
		return new Dictionary(subWords);
	}

	/**
	 * Returns the number of words contained in this dictionary. This is not a
	 * sophisticated measure of the words, since the dictionary class does not
	 * make an effort to group logically equivalent words.
	 * 
	 * @return the number of words contained in this dictionary
	 */
	public int size() {
		return this.words.size();
	}

	/**
	 * Returns a {@link Dictionary} that contains words that are strictly longer
	 * than the specified minimum length.
	 * 
	 * @param length
	 *            the minimum length. It must be greater than zero.
	 * @return a {@code Dictionary} that contains all words that are strictly
	 *         longer than the specified minimum length
	 * @throws IllegalArgumentException
	 *             if {@code length <= 0}
	 */
	public Dictionary wordsLongerThan(final int length) {
		if (length <= 0) {
			throw new IllegalArgumentException("length <= 0");
		}
		final Set<String> subWords = new HashSet<String>();
		for (final String word : this.words) {
			if (word.length() > length) {
				subWords.add(word);
			}
		}
		return new Dictionary(subWords);
	}

	/**
	 * Returns a {@link Dictionary} that contains words that are strictly
	 * shorter than the specified maximum length.
	 * 
	 * @param length
	 *            the maximum length. It must be greater than zero.
	 * @return a {@code Dictionary} that contains all words that are strictly
	 *         shorter than the specified minimum length
	 * @throws IllegalArgumentException
	 *             if {@code length <= 0}
	 */
	public Dictionary wordsShorterThan(final int length) {
		if (length <= 0) {
			throw new IllegalArgumentException("length <= 0");
		}
		final Set<String> subWords = new HashSet<String>();
		for (final String word : this.words) {
			if (word.length() < length) {
				subWords.add(word);
			}
		}
		return new Dictionary(subWords);
	}

	/**
	 * Returns whether this dictionary contains the specified word. The search
	 * is <em>not</em> case-sensitive.
	 * 
	 * @param word
	 *            the candidate that is searched for in this dictionary
	 * @return {@code true} if the specified word is contained in this
	 *         dictionary, otherwise {@code false}
	 * @throws NullPointerException
	 *             if {@code word} is null
	 */
	public boolean contains(final String word) {
		if (word == null) {
			throw new NullPointerException("word is null");
		}
		return this.words.contains(word.toLowerCase());
	}

	/**
	 * Returns a {@link Dictionary} that contains all words in this dictionary
	 * that are the same length as the specified word. The returned dictionary
	 * is not guaranteed to contain the specified word, nor is this dictionary
	 * required to contain the specified word.
	 * 
	 * @param word
	 *            the word whose length is used to construct a dictionary
	 * @return a {@code Dictionary} that contains all words that are the same
	 *         length as the specified word.
	 * @throws NullPointerException
	 *             if {@code word} is null
	 */
	public Dictionary wordsOfLength(final String word) {
		if (word == null) {
			throw new NullPointerException("word is null");
		}
		return this.wordsOfLength(word.length());
	}

	private final Map<Integer, Dictionary> cached = new HashMap<Integer, Dictionary>();

	/**
	 * Returns a {@link Dictionary} that contains all words in this dictionary
	 * that are the same length as the specified {@code length}.
	 * 
	 * @param length
	 *            the length that is used to construct a dictionary
	 * @return a {@code Dictionary} that contains all words that have a length
	 *         that is equal to the specified {@code length}
	 * @throws NullPointerException
	 *             if {@code word} is null
	 */
	public Dictionary wordsOfLength(final int length) {
		if (length <= 0) {
			throw new IllegalArgumentException("length <= 0");
		}
		if (!this.cached.containsKey(length)) {
			final Set<String> subWords = new HashSet<String>();
			for (final String word : this.words) {
				if (word.length() == length) {
					subWords.add(word);
				}
			}
			this.cached.put(length, new Dictionary(subWords));
		}
		return this.cached.get(length);
	}

	@Override
	public Iterator<String> iterator() {
		return this.getWords().iterator();
	}

	private static Set<String> getWords(final File file) throws IOException {
		LineNumberReader reader = null;
		try {
			reader = new LineNumberReader(new BufferedReader(new FileReader(file)));
			final Set<String> words = new HashSet<String>();
			while (true) {
				final String line = reader.readLine();
				if (line == null) {
					break;
				}
				words.add(line.toLowerCase());
			}
			return words;
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

	private static Dictionary INSTANCE;

	/**
	 * Returns a default dictionary containing "a lot" of words used in the
	 * English language. If this class becomes more developed, we'll further
	 * specify what "a lot" actually is.
	 * 
	 * @return a default {@link Dictionary}
	 */
	public static Dictionary getDictionary() {
		if (INSTANCE == null) {
			try {
				final Set<String> words = new HashSet<String>();
				words.addAll(Dictionary.getWords(new File("dictionaries/NAMES.DIC")));
				words.addAll(Dictionary.getWords(new File("dictionaries/2of12.txt")));
				words.addAll(Dictionary.getWords(new File("dictionaries/2of12inf.txt")));
				/*
				 * This is commented out since this dictionary contains a lot of
				 * words that are useless. For example, strange phrases and
				 * non-words. We omit them since we're interested in words that
				 * are actually used.
				 */
				// words.addAll(Dictionary.getWords(new File("dic-0294.txt")));
				INSTANCE = new Dictionary(words);
			} catch (final IOException e) {
				throw new IllegalStateException("Dictionary is unavailable", e);
			}
		}
		return INSTANCE;
	}

	/**
	 * Creates a {@link Dictionary} from the specified file. Each word is
	 * expected to be on its own line, with no leading or trailing space.
	 * 
	 * @param fileName
	 *            the name of the file that contains the words
	 * @return a {@link Dictionary} created from the specified file
	 * @throws IOException
	 *             if the underlying IO fails
	 */
	public static Dictionary fromFile(final String fileName) throws IOException {
		final File file = new File(fileName);
		return new Dictionary(Dictionary.getWords(file));
	}
}
