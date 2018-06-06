package com.ftn.mdj.dto;


/**
 * Created by Jasmina on 17/04/2018.
 */
public class LoginDTO {

    private String email;
    private String password;
    private String deviceInstance;

    public LoginDTO(String email, String password, String deviceInstance) {
        this.email = email;
        this.password = password;
        this.deviceInstance = deviceInstance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceInstance() {
        return deviceInstance;
    }

    public void setDeviceInstance(String deviceInstance) {
        this.deviceInstance = deviceInstance;
    }
}
