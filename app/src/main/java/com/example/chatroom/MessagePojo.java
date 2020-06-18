package com.example.chatroom;

import java.util.Date;

public class MessagePojo {

    public MessagePojo(String message, String senderId, String receiverId, String messageId, String time) {
        this.message = message;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageId = messageId;
        this.time = time;

        // Initialize to current time
        time = String.valueOf(new Date().getTime());
    }

    public MessagePojo(){

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    String message;
    String senderId;
    String receiverId;
    String messageId;
    String time;
}
