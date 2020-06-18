package com.example.chatroom;

public class UserPojo {
    String id;
    String passCode;

    public UserPojo() {


    }

    public String getPassCode() {
        return passCode;
    }

    public void setPassCode(String passCode) {
        this.passCode = passCode;
    }

    public UserPojo(String id, String userName) {
        this.id = id;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    String userName;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
