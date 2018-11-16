package medigram.medigram;

import java.io.Serializable;

import io.searchbox.annotations.JestId;

public abstract class User implements Serializable {
    String emailAddress;
    String phoneNumber;
    String userID;


    public String toString(){
        return userID;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public abstract String checkUserType();

}
