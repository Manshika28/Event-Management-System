package com.example.userservice.model;

public class Booking {
    private Long id;
    private Long userId;
    private Long eventId;
    private String status;

    public Booking() {}

    public Booking(Long id, Long userId, Long eventId, String status) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
