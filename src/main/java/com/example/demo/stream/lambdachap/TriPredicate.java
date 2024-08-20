package com.example.demo.stream.lambdachap;

@FunctionalInterface
public interface TriPredicate<T, U, V> {
    boolean test(T y, U u, V v);
}
