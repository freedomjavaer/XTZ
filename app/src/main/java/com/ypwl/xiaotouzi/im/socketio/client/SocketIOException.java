package com.ypwl.xiaotouzi.im.socketio.client;

@SuppressWarnings("unused")
public class SocketIOException extends Exception {

    public SocketIOException() {
        super();
    }

    public SocketIOException(String message) {
        super(message);
    }

    public SocketIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public SocketIOException(Throwable cause) {
        super(cause);
    }
}
