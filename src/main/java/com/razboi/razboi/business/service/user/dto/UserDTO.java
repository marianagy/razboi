package com.razboi.razboi.business.service.user.dto;

import java.io.Serializable;

public class UserDTO implements Serializable {

    private Integer ID;
    private String username;

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
