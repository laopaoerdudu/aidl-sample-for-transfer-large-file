// DataManager.aidl
package com.dev;
import com.dev.BigDataCallback;

interface DataManager {
    void sendData(in byte[]data);
    void sendBigData(in ParcelFileDescriptor pfd);
    void registerCallback(BigDataCallback callback);
    void unregisterCallback(BigDataCallback callback);
}