package com.ypwl.xiaotouzi.im.socketio.parser;


@SuppressWarnings("unused")
public class Packet<T> {

    public int type = -1;
    public int id = -1;
    public String nsp;
    public T data;
    public int attachments;

    public Packet() {
    }

    public Packet(int type) {
        this.type = type;
    }

    public Packet(int type, T data) {
        this.type = type;
        this.data = data;
    }

    @Override
    public String toString() {
        return "Packet{" +
                "type=" + type +
                ", id=" + id +
                ", nsp='" + nsp + '\'' +
                ", data=" + data +
                ", attachments=" + attachments +
                '}';
    }
}
