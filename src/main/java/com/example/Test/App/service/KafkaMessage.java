package com.example.Test.App.service;

/**
 * @author Andrew Yantsen
 */
public class KafkaMessage {
   private String userName;
   private Boolean isActive;

    public KafkaMessage(String userName, Boolean isActive) {
        this.userName = userName;
        this.isActive = isActive;
    }

    public KafkaMessage() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "KafkaMessage{" +
                "userName='" + userName + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
