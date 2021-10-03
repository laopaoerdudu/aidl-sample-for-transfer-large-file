// ICallback.aidl
package com.dev;

interface ICallback {
     void serveSendDataToClient(in ParcelFileDescriptor pfd);
}