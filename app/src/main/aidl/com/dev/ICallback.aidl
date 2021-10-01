// ICallback.aidl
package com.dev;

interface ICallback {
     void serverToClient(in ParcelFileDescriptor pfd);
}