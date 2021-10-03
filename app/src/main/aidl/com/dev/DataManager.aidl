// DataManager.aidl
package com.dev;
import com.dev.ICallback;

interface DataManager {
    void sendData(in byte[]data);
    void clientSendDataToServer(in ParcelFileDescriptor pfd);
    void registerCallback(ICallback callback);
    void unregisterCallback(ICallback callback);
}