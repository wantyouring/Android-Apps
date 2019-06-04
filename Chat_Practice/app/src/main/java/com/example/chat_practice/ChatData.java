package com.example.chat_practice;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatData {
    private String userName;
    private String message;
    private String time;

    public ChatData() { }

    public ChatData(String userName, String message, long time) {
        this.userName = userName;
        this.message = message;
        this.time = new SimpleDateFormat("HH:mm").format(new Date(time));
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }
}
