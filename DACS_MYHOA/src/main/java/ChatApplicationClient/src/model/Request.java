package model;

import java.net.Socket;

public class Request {
    private String event;
    private Object data;
    private Socket senderSocket;

    public Request(String event, Object data) {
        this.event = event;
        this.data = data;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Socket getSenderSocket() {
        return senderSocket;
    }

    public void setSenderSocket(Socket senderSocket) {
        this.senderSocket = senderSocket;
    }

    public Request(String event, Object data, Socket senderSocket) {
        this.event = event;
        this.data = data;
        this.senderSocket = senderSocket;
    }
}