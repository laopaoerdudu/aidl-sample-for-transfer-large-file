package com.dev.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ReflectUtil {
    public static Object invoke(Object instance, String className, String methodName, Object... params) {
        try {
            Class<?> c = Class.forName(className);
            if (params != null) {
                int length = params.length;
                Class[] paramsTypes = new Class[length];
                for (int i = 0; i < length; i++) {
                    paramsTypes[i] = params[i].getClass();
                }
                Method method = c.getDeclaredMethod(methodName, paramsTypes);
                method.setAccessible(true);
                return method.invoke(instance, params);
            }
            Method method = c.getDeclaredMethod(methodName);
            method.setAccessible(true);
            return method.invoke(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getInstance(String className, Object... params) {
        try {
            Class<?> c = Class.forName(className);
            if (params != null) {
                int length = params.length;
                Class[] paramsTypes = new Class[length];
                for (int i = 0; i < length; i++) {
                    paramsTypes[i] = params[i].getClass();
                }
                Constructor constructor = c.getDeclaredConstructor(paramsTypes);
                constructor.setAccessible(true);
                return constructor.newInstance(params);
            }
            Constructor constructor = c.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
