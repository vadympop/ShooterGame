package com.game.core.utils;

@FunctionalInterface
public interface QuatroConsumer<T, U, V, W> {
    void accept(T t, U u, V v, W w);
}

