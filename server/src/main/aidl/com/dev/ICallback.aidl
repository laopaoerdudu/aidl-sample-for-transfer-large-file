// ICallback.aidl
package com.dev;

// 服务端主动给客户端发送数据，客户端监听
interface ICallback {
     void serveSendDataToClient(in ParcelFileDescriptor pfd);
}