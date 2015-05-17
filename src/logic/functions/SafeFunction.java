package logic.functions;

/**
 * A {@link Function} that will take an input of any type. Typically, this means
 * there is a dynamic cast involved before a value is used, or the input value
 * is not used.
 * <p>
 * Don't use this interface if you need a specific type; let clients wrap your
 * typed function themselves. Only functions that are naturally designed to
 * accept any object should use this interface.
 * 
 * @author Aaron Faanes
 * 
 * @param <R>
 *            the type of the returned value
 * @see Functions#protect(logic.adapters.Adapter, Function)
 * @see AdaptingSafeFunction
 */
public interface SafeFunction<R> extends Function<Object, R> {

}
