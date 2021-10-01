
我们先实现客户端向服务端传输大文件，然后再实现服务端向客户端传输大文件。




#### AIDL service 双向通信的问题

之前一直说的都是 `Client` 向 `Server` 的通信，那如果 `Server` 要调用 `Client` 呢？

为了解决这个问题，`Android` 提供了 `RemoteCallbackList` 这个类来专门管理 `remote` 回调的注册与注销。

- `RemoteCallbackList`

- `DeathRecipient`



