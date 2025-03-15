package nl.mtvehicles.core.infrastructure.utils;

/**
 * Functional interface to represent a function with three arguments
 * @param <T> First argument
 * @param <U> Second argument
 * @param <V> Third argument
 * @param <R> Return type
 */
@FunctionalInterface
public interface TriFunction<T, U, V, R> {
    R apply(T t, U u, V v);
}
