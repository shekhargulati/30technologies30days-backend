package com.shekhar.challenge.aerogear;

public class Notification {

    private Message message;

    public Notification() {
    }

    public Notification(Message message) {
        super();
        this.message = message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

}
