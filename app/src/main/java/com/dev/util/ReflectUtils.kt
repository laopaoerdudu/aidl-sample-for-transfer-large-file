package com.dev.util

class ReflectUtils {
    companion object {
        fun invoke(
            instance: Any,
            className: String,
            methodName: String,
            vararg paramValues: Class<*>?
        ): Any? {
            try {
                var typeClass: Class<*>? = Class.forName(className)
                while (typeClass != null) {
                    val paramsTypes: Array<Class<*>?> = arrayOfNulls(paramValues?.size)
                    paramValues.forEachIndexed { index, clazz ->
                        paramsTypes[index] = clazz?.javaClass
                    }
                    val method = typeClass?.getDeclaredMethod(methodName, *paramsTypes)?.apply {
                        isAccessible = true
                    }
                    return method?.invoke(instance, paramValues)
                    typeClass = typeClass.superclass
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return null
        }
    }
}