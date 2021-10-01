package com.dev.server

import android.content.Context
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class AssetUtils {
    companion object {
        fun openAssets(context: Context, fileName: String): ByteArray? {
            var ins: InputStream? = null
            var bas: ByteArrayOutputStream? = null
            try {
                ins = context.assets.open(fileName)
                bas = ByteArrayOutputStream()
                val buffer = ByteArray(1024)
                var len: Int
                while (ins.read(buffer).also { len = it } >= 0) {
                    bas.write(buffer, 0, len)
                }
                return bas.toByteArray()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    bas?.apply {
                        flush()
                        close()
                    }
                    ins?.close()
                } catch (ex: IOException) {
                    ex.printStackTrace()
                }
            }
            return null
        }
    }
}