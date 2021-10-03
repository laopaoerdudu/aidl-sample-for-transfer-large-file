package com.dev

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import com.dev.util.DataConstant.BIG_DATA
import com.dev.util.DataConstant.SMALL_DATA
import java.io.FileInputStream

class AIDLService : Service() {
    private var largeDataHandle: ((ParcelFileDescriptor?) -> Unit)? = null
    private var smallDataHandle: ((ByteArray?) -> Unit)? = null
    private val remoteCallbackList = RemoteCallbackList<IInterface>()
    private val binder = object : DataManager.Stub() {
        override fun sendSmallData(data: ByteArray?) {
            smallDataHandle?.invoke(data)
            Log.i("WWE", "AIDLService #sendSmallData data -> $data")
        }

        override fun sendBigData(pfd: ParcelFileDescriptor?) {
            largeDataHandle?.invoke(pfd)
            Log.i(
                "WWE",
                "AIDLService #sendBigData -> data -> ${FileInputStream(pfd?.fileDescriptor).readBytes()}"
            )
        }

        override fun registerCallback(callback1: BigDataCallback?, callback2: SmallDataCallback?) {
            remoteCallbackList.register(callback1, BIG_DATA)
            remoteCallbackList.register(callback2, SMALL_DATA)
        }

        override fun unregisterCallback(callback1: BigDataCallback?, callback2: SmallDataCallback?) {
            remoteCallbackList.unregister(callback1)
            remoteCallbackList.unregister(callback2)
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("WWE", "AIDLService #onCreate invoked")
        largeDataHandle = {
            sendDataToClient(it, null)
        }
        smallDataHandle = {
            sendDataToClient(null, it)
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

    private fun sendDataToClient(pfd: ParcelFileDescriptor?, data: ByteArray?) {
        for (i in 0 until remoteCallbackList.beginBroadcast()) {
            try {
                when (remoteCallbackList.getBroadcastCookie(i)) {
                    BIG_DATA -> (remoteCallbackList.getBroadcastItem(i) as? BigDataCallback)?.onReceiveBigData(
                        pfd
                    )
                    SMALL_DATA -> (remoteCallbackList.getBroadcastItem(i) as? SmallDataCallback)?.onReceiveSmallData(
                        data
                    )
                    else -> {
                    }
                }
            } catch (ex: RemoteException) {
                ex.printStackTrace()
            }
        }
        remoteCallbackList.finishBroadcast()
    }
}