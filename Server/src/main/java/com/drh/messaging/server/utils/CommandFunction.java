package com.drh.messaging.server.utils;

@FunctionalInterface
public interface CommandFunction<T1, T2, T3, R> {

  R apply(T1 var1, T2 var2, T3 var3);
}
