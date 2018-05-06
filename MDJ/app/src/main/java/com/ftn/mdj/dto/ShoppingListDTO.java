package com.ftn.mdj.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jasmina on 17/04/2018.
 */
public class ShoppingListDTO implements Serializable {

    private String name;
    private boolean isSecret;
    private boolean isActive;
    private String accessPassword;

    public ShoppingListDTO(String name) {
        this.name = name;
        this.isActive = true;
        this.isSecret = false;
        this.accessPassword = "";
    }

    public ShoppingListDTO(String name, boolean isSecret, boolean isActive, String accessPassword) {
        this.name = name;
        this.isSecret = isSecret;
        this.isActive = isActive;
        this.accessPassword = accessPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSecret() {
        return isSecret;
    }

    public void setSecret(boolean secret) {
        isSecret = secret;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getAccessPassword() {
        return accessPassword;
    }

    public void setAccessPassword(String accessPassword) {
        this.accessPassword = accessPassword;
    }
}
