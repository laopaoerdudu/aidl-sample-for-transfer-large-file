package com.dev

import android.os.MemoryFile
import java.io.FileDescriptor
import java.io.IOException

object MemoryFileUtils {
    fun createMemoryFile(name: String?, length: Int): MemoryFile? {
        try {
            return MemoryFile(name, length)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun getFileDescriptor(instance: MemoryFile): FileDescriptor {
        return ReflectUtils.invoke(
            "android.os.MemoryFile",
            instance,
            "getFileDescriptor"
        ) as FileDescriptor
    }
}