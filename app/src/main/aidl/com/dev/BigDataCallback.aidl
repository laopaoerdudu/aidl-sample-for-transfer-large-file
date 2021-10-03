// BigDataCallback.aidl
package com.dev;

interface BigDataCallback {
     void onReceiveBigData(in ParcelFileDescriptor pfd);
}

