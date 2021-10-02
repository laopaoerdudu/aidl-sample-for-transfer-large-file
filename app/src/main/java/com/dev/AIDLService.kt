package com.dev

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Message
import android.os.ParcelFileDescriptor
import android.os.RemoteCallbackList
import android.util.Log
import java.io.FileInputStream

class AIDLService : Service() {
    private var sendDataCallback: ((ParcelFileDescriptor) -> Unit)? = null

    private val callbackList = RemoteCallbackList<ICallback>()
    private val binder = object : DataManager.Stub() {
        override fun sendImage(data: ByteArray?) {
            Message.obtain().apply {
                what = 1
                obj = data
            }
        }

        override fun clientSendDataToServer(pfd: ParcelFileDescriptor?) {
            // 从 ParcelFileDescriptor 中获取 FileDescriptor
            val fileDescriptor = pfd?.fileDescriptor
            val data = FileInputStream(fileDescriptor).readBytes()
            Message.obtain().apply {
                what = 1
                obj = data
            }
        }

        override fun registerCallback(callback: ICallback?) {
            callbackList.register(callback)
        }

        override fun unregisterCallback(callback: ICallback?) {
            callbackList.unregister(callback)
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("WWE", "AIDLService #onCreate invoked")
        sendDataCallback = {
            sendDataToClient(it)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("WWE", "AIDLService #onStartCommand invoked")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.i("WWE", "AIDLService #onBind invoked")
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.i("WWE", "AIDLService #onUnbind invoked")
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        Log.i("WWE", "AIDLService #onDestroy invoked")
        super.onDestroy()
    }

    // 发送数据到客户端
    private fun sendDataToClient(pfd: ParcelFileDescriptor) {
        for (i in 0 until callbackList.beginBroadcast()) {
            try {
                callbackList.getBroadcastItem(i)?.serveSendDataToClient(pfd)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        callbackList.finishBroadcast()
    }
}