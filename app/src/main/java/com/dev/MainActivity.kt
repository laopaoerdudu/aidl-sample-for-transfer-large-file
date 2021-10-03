package com.dev

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.BitmapFactory
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import androidx.appcompat.widget.AppCompatImageView
import com.dev.util.AssetUtils
import com.dev.util.ReflectUtils
import java.io.FileDescriptor
import java.io.FileInputStream
import java.io.IOException
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var ivIcon: AppCompatImageView
    private var dataManager: DataManager? = null

    private val callback = object : ICallback.Stub() {
        override fun serveSendDataToClient(pfd: ParcelFileDescriptor?) {
            Log.i("WWE", "MainActivity #serveSendDataToClient invoked!")
            val fis = FileInputStream(pfd?.fileDescriptor)
            val bytes = fis.readBytes()
            if (bytes.isNotEmpty()) {
                runOnUiThread {
                    ivIcon.setImageBitmap(
                        BitmapFactory.decodeByteArray(
                            bytes,
                            0,
                            bytes.size
                        )
                    )
                }
            }
        }
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            dataManager = DataManager.Stub.asInterface(binder)
            dataManager?.registerCallback(callback)
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            dataManager = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindService(Intent("com.dev.AIDLService").apply {
            setClassName("com.dev", "com.dev.AIDLService")
        }, serviceConnection, Context.BIND_AUTO_CREATE)
        ivIcon = findViewById(R.id.ivIcon)
        findViewById<Button>(R.id.btnSendImageToServer).setOnClickListener {
            try {
                val byteArray = assets.open("large.jpg").readBytes()
                val memoryFile = MemoryFile("client_image", byteArray.size).apply {
                    writeBytes(byteArray, 0, 0, byteArray.size)
                }
                dataManager?.clientSendDataToServer(
                    ParcelFileDescriptor.dup(
                        ReflectUtils.invoke(
                            memoryFile,
                            "android.os.MemoryFile",
                            "getFileDescriptor"
                        ) as? FileDescriptor
                    )
                )
            } catch (ex: IOException) {
                ex.printStackTrace()
            } catch (ex: RemoteException) {
                ex.printStackTrace()
            }
        }
        findViewById<Button>(R.id.btnSendSmallImageToServer).setOnClickListener {
            try {
                dataManager?.sendImage(AssetUtils.openAssets(this, "small.jpg"))
            } catch (ex: Exception) {
                ex.printStackTrace()
            } catch (ex: RemoteException) {
                ex.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        dataManager?.unregisterCallback(callback)
        unbindService(serviceConnection)
        super.onDestroy()
    }
}