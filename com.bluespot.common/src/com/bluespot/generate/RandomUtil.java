package com.bluespot.generate;

import java.util.Random;

/**
 * Collection of methods dealing with producing random choices
 * 
 * @author Aaron Faanes
 * 
 */
public final class RandomUtil {

	private RandomUtil() {
		// Suppress default constructor to ensure non-instantiability.
		throw new AssertionError("Instantiation not allowed");
	}

	/**
	 * A thread-local factory of {@link Random} instances.
	 */
	private static final ThreadLocal<Random> THREAD_LOCAL = new ThreadLocal<Random>() {
		@Override
		protected Random initialValue() {
			return new Random();
		}
	};

	/**
	 * Return a thread-local random instance.
	 * 
	 * @return a thread-local random instance
	 */
	public static Random threadLocal() {
		return THREAD_LOCAL.get();
	}

	private static Random GLOBAL = new Random();

	/**
	 * Return a globally-shared random instance.
	 * 
	 * @return a globally-shared random instance
	 */
	public static Random global() {
		return GLOBAL;
	}

	/**
	 * Returns some {@code char} element, chosen at random, from the specified
	 * array.
	 * 
	 * @param choices
	 *            the array that provides the options
	 * @return a randomly chosen {@code char} from the specified array
	 * @throws IllegalArgumentException
	 *             if {@code choices} is empty
	 */
	public static char choice(final char[] choices) {
		if (choices.length == 0) {
			throw new IllegalArgumentException("choices is empty");
		}
		return choices[(int) (Math.random() * choices.length)];
	}

	/**
	 * Returns some element, chosen at random, from the specified array.
	 * 
	 * @param <T>
	 *            type of the elements in the array
	 * @param choices
	 *            the array that provides the options
	 * @return a randomly chosen element
	 * @throws IllegalArgumentException
	 *             if {@code choices} is empty
	 */
	public static <T> T choice(final T[] choices) {
		if (choices.length == 0) {
			throw new IllegalArgumentException("choices is empty");
		}
		return choices[(int) (Math.random() * choices.length)];
	}

	/**
	 * Creates a random name, typically used for game nations.
	 * 
	 * @return a randomly generated name
	 */
	public static String generateName() {
		final StringBuilder builder = new StringBuilder();
		while (builder.length() < 4) {
			builder.delete(0, builder.length());
			builder.append(RandomUtil.choice(RandomUtil.NAME_PREFIXES));
			builder.append(RandomUtil.choice(RandomUtil.NAME_SUFFIXES));
		}
		return builder.toString();
	}

	private static final String[] NAME_PREFIXES = { "A", "Aile", "Ar", "Are", "Ara", "As", "Ba", "Be", "Ber", "Bo",
			"Bra", "Bre", "Bri", "Bru", "Bur", "Ca", "Caie", "Calo", "Cam", "Can", "Car", "Cara", "Care", "Caste",
			"Casto", "Cele", "Cela", "Cha", "Che", "Co", "Col", "Colo", "Com", "Cor", "Da", "Dar", "Dai", "De", "Den",
			"Di", "Die", "Dir", "Do", "Du", "Dy", "Er", "En", "Eve", "Et", "Fal", "Fe", "Fel", "Fen", "Fer", "Fre",
			"Fra", "Fu", "Fy", "Ha", "Hal", "He", "Hele", "Helvese", "Hi", "Hja", "Ho", "Hol", "Hy", "Hyra", "Hydra",
			"Hype", "Il", "In", "Ka", "Kan", "Kem", "Khry", "Khryle", "Kilo", "La", "Lai", "Lare", "Lau", "Le", "Lei",
			"Lei", "Li", "Lit", "Liu", "Ly", "Mai", "Mas", "Mega", "Meta", "Mo", "Na", "Nai", "Ne", "Ni", "Nitre",
			"No", "Pa", "Par", "Pal", "Pasel", "Pra", "Praie", "Pre", "Po", "Pi", "Pri", "Ra", "Ran", "Rann", "Ragase",
			"Re", "Ri", "Ro", "Roma", "Ru", "Ry", "Sa", "Sar", "Saira", "Se", "Sen", "Sever", "Si", "Ste", "Sy", "Te",
			"Tem", "Tere", "Vai", "Vaia", "Val", "Van", "Ve", "Ve", "Vy", "Warna", "We", "Win", "Wind", "Wi", "Xy",
			"Zar", "Ze" };

	private static final String[] NAME_SUFFIXES = { "", "bor", "cca", "car", "co", "den", "don", "ga", "gen", "go",
			"gos", "ho", "ha", "ken", "lau", "len", "lia", "lia", "lin", "lis", "lius", "mar", "mark", "me", "men",
			"mery", "nem", "nia", "nia", "nia", "nis", "num", "ra", "rac", "re", "rec", "ren", "rian", "rion", "ris",
			"ron", "ros", "sa", "san", "sen", "sor", "sse", "tan", "ten", "tenia", "tania", "tia", "tra", "tera",
			"tassus", "tria", "tum", "us", "via", "va" };
}
