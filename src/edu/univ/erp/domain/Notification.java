package edu.univ.erp.domain;

public class Notification {
    private int id;
    private int receiverId;
    private String message;

    public Notification(int id, int receiverId, String message) {
        this.id = id;
        this.receiverId = receiverId;
        this.message = message;
    }

    public String getMessage() { return message; }
}