package reflection;

import java.util.HashMap;
import java.util.Map;

/**
 * The enumeration of primitives in the Java languages. This class also provides
 * static methods for conversion between boxed and primitive types.
 * 
 * @author Aaron Faanes
 */
public enum Primitive {

	/**
	 * Represents the {@link Void} type.
	 */
	VOID(Void.TYPE, Void.class),
	/**
	 * Represents the {@link Boolean} type.
	 */
	BOOLEAN(Boolean.TYPE, Boolean.class),

	/**
	 * Represents the {@link Byte} type.
	 */
	BYTE(Byte.TYPE, Byte.class),

	/**
	 * Represents the {@link Character} type.
	 */
	CHARACTER(Character.TYPE, Character.class),

	/**
	 * Represents the {@link Character} type.
	 */
	SHORT(Short.TYPE, Short.class),

	/**
	 * Represents the {@link Integer} type.
	 */
	INTEGER(Integer.TYPE, Integer.class),

	/**
	 * Represents the {@link Long} type.
	 */
	LONG(Long.TYPE, Long.class),

	/**
	 * Represents the {@link Float} type.
	 */
	FLOAT(Float.TYPE, Float.class),

	/**
	 * Represents the {@link Double} type.
	 */
	DOUBLE(Double.TYPE, Double.class);

	private final Class<?> primitiveType;
	private final Class<?> boxedType;

	private Primitive(final Class<?> primitiveType, final Class<?> boxedType) {
		this.primitiveType = primitiveType;
		this.boxedType = boxedType;
	}

	/**
	 * Returns the primitive type of this primitive. Specifically, this returns
	 * the class that represents the actual primitive type, such as the type of
	 * a {@code int} or {@code boolean}.
	 * 
	 * @return the class that represents the true primitive type
	 */
	public Class<?> getPrimitiveType() {
		return this.primitiveType;
	}

	/**
	 * Returns the boxed type for this primitive.
	 * 
	 * @return the class that represents the boxed type
	 */
	public Class<?> getBoxedType() {
		return this.boxedType;
	}

	private static final Map<Class<?>, Class<?>> primitiveToBoxed = new HashMap<Class<?>, Class<?>>();
	private static final Map<Class<?>, Class<?>> boxedToPrimitive = new HashMap<Class<?>, Class<?>>();
	static {
		for (final Primitive primitive : Primitive.values()) {
			primitiveToBoxed.put(primitive.getPrimitiveType(), primitive.getBoxedType());
			boxedToPrimitive.put(primitive.getBoxedType(), primitive.getPrimitiveType());
		}
	}

	/**
	 * Converts the specified primitive type to its boxed type counterpart.
	 * 
	 * @param primitiveType
	 *            the primitive type that will be converted
	 * @return the boxed type that corresponds to the specified primitive type
	 * @throws NullPointerException
	 *             if {@code primitiveType} is null
	 * @throws IllegalArgumentException
	 *             if {@code primitiveType} is not a primitive type, according
	 *             to {@link Class#isPrimitive()}
	 */
	public static Class<?> asBoxed(final Class<?> primitiveType) {
		if (primitiveType == null) {
			throw new NullPointerException("primitiveType is null");
		}
		if (!primitiveType.isPrimitive()) {
			throw new IllegalArgumentException("primitiveType is not a primitive type");
		}
		final Class<?> boxedType = Primitive.primitiveToBoxed.get(primitiveType);
		assert boxedType != null : "boxedType is null: it was not in the conversion map";
		return boxedType;
	}

	/**
	 * Converts the specified boxed type to its primitive type counterpart.
	 * 
	 * @param boxedType
	 *            the boxed type that will be converted
	 * @return the primitive type that corresponds to the specified boxed type.
	 * @throws NullPointerException
	 *             if {@code boxedType} is null
	 * @throws IllegalArgumentException
	 *             if {@code boxedType} is not a boxed primitive type
	 */
	public static Class<?> asPrimitive(final Class<?> boxedType) {
		if (boxedType == null) {
			throw new NullPointerException("boxedType is null");
		}
		if (boxedType.isPrimitive()) {
			throw new IllegalArgumentException("boxedType is already a primitive type: " + boxedType);
		}
		final Class<?> primitiveType = Primitive.boxedToPrimitive.get(boxedType);
		if (primitiveType == null) {
			throw new IllegalArgumentException("boxedType does not correspond to a primitive: " + boxedType);
		}
		return primitiveType;
	}
}
