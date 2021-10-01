package com.dev

class ReflectUtils {
    companion object {
        fun invoke(
            className: String,
            instance: Any,
            methodName: String,
            vararg paramValues: Class<*>?
        ): Any? {
            try {
                var typeClass: Class<*>? = Class.forName(className)
                while (typeClass != null) {
                    try {
                        val paramsTypes: Array<Class<*>?> = arrayOfNulls(paramValues?.size)
                        paramValues.forEachIndexed { index, clazz ->
                            paramsTypes[index] = clazz?.javaClass
                        }
                        val method = typeClass?.getDeclaredMethod(methodName, *paramsTypes)?.apply {
                            isAccessible = true
                        }
                        return method?.invoke(instance, paramValues)
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                    typeClass = typeClass.superclass
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return null
        }
    }
}