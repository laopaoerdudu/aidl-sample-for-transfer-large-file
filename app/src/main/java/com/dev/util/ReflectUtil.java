package com.dev.util;

import java.lang.reflect.Method;

public class ReflectUtil {
    public static Object invoke(Object instance, String className, String methodName, Object... params) {
        try {
            Class<?> c = Class.forName(className);
            if (params != null) {
                int plength = params.length;
                Class[] paramsTypes = new Class[plength];
                for (int i = 0; i < plength; i++) {
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
}
