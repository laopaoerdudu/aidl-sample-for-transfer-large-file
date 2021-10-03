// DataManager.aidl
package com.dev;
import com.dev.DataCallback;

interface DataManager {
    void sendData(in byte[]data);
    void sendLargeData(in ParcelFileDescriptor pfd);
    void registerCallback(DataCallback callback);
    void unregisterCallback(DataCallback callback);
}