package ru.stepup.geometry;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Utils extends Object {
    @SuppressWarnings("unchecked")
     public static <T> T cashProxy(T obj, InvocationHandler h) {
        return (T) Proxy.newProxyInstance(
                obj.getClass().getClassLoader(),
                obj.getClass().getInterfaces(),
                h
        );
    }
}
