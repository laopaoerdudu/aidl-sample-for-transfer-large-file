// DataCallback.aidl
package com.dev;

interface DataCallback {
     void onReceiveLargeData(in ParcelFileDescriptor pfd);
}