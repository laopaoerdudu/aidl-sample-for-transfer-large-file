// DataManager.aidl
package com.dev;
import com.dev.BigDataCallback;
import com.dev.SmallDataCallback;

interface DataManager {
    void sendSmallData(in byte[] data);
    void sendBigData(in ParcelFileDescriptor pfd);
    void registerCallback(BigDataCallback callback1, SmallDataCallback callback2);
    void unregisterCallback(BigDataCallback callback1, SmallDataCallback callback2);
}