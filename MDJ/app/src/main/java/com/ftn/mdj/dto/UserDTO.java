package com.ftn.mdj.dto;

/**
 * Created by Jasmina on 17/05/2018.
 */

public class UserDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Long id;
    private Boolean showNotifications;

    public UserDTO(String firstName, String lastName, String email, String password, long id, Boolean showNotifications) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.id = id;
        this.showNotifications = showNotifications;
    }

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getShowNotifications() {
        return showNotifications;
    }

    public void setShowNotifications(Boolean showNotifications) {
        this.showNotifications = showNotifications;
    }
}
