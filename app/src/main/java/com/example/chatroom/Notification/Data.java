package com.example.chatroom.Notification;

public class Data {

    private String user;
    private int icon;
    private String body;
    private String title;

    public Data(String user, int icon, String body, String title) {
        this.user = user;
        this.icon = icon;
        this.body = body;
        this.title = title;
    }

    public Data() {
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser() {
        return user;
    }

    public String getBody() {
        return body;
    }

    public String getTitle() {
        return title;
    }
}
