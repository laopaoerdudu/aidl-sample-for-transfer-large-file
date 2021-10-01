// ImageManager.aidl
package com.dev;
import com.dev.ICallback;

interface ImageManager {
    void sendImage(in byte[]data);
    void clientToServer(in ParcelFileDescriptor pfd);
    void registerCallback(ICallback callback);
    void unregisterCallback(ICallback callback);
}