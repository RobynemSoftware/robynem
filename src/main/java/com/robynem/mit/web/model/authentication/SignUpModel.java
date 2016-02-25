package com.robynem.mit.web.model.authentication;

import java.io.Serializable;

/**
 * Created by robyn_000 on 17/12/2015.
 */
public class SignUpModel implements Serializable {

    protected String firstName;

    protected String lastName;

    protected String emailAddress;

    protected String password;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
