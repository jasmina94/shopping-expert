package com.ftn.mdj.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Jasmina on 17/04/2018.
 */
public class ShoppingListDTO implements Serializable {

    private String name;
    private boolean isSecret;
    private boolean isActive;
    private String accessPassword;

    public ShoppingListDTO(String name){
        this.name = name;
        this.isActive = true;
        this.isSecret = false;
        this.accessPassword = "";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {

        return name;
    }
}
