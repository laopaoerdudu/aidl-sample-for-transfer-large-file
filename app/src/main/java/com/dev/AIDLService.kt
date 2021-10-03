package com.dev

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import java.io.FileInputStream

class AIDLService : Service() {
    private var sendDataCallback: ((ParcelFileDescriptor?) -> Unit)? = null
    private val callbackList = RemoteCallbackList<BigDataCallback>()

    private val binder = object : DataManager.Stub() {
        override fun sendData(data: ByteArray?) {
            Log.i("WWE", "AIDLService #sendData data -> $data")
        }

        override fun sendBigData(pfd: ParcelFileDescriptor?) {
            sendDataCallback?.invoke(pfd)
            Log.i("WWE", "AIDLService #sendLargeData -> data -> ${FileInputStream(pfd?.fileDescriptor).readBytes()}")
        }

        override fun registerCallback(callback: BigDataCallback?) {
            callbackList.register(callback)
        }

        override fun unregisterCallback(callback: BigDataCallback?) {
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

    private fun sendDataToClient(pfd: ParcelFileDescriptor?) {
        for (i in 0 until callbackList.beginBroadcast()) {
            try {
                callbackList.getBroadcastItem(i)?.onReceiveBigData(pfd)
            } catch (ex: RemoteException) {
                ex.printStackTrace()
            }
        }
        callbackList.finishBroadcast()
    }
}