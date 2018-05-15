package com.ftn.util;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Jasmina on 15/05/2018.
 */
@Data
@NoArgsConstructor
public class GenericResponse<T> {

    private boolean successfulOperation;
    private T entity;
    private String errorMessage;


    public void success(T entity){
        this.entity = entity;
        this.successfulOperation = true;
    }

    public void error(String errorMessage){
        this.errorMessage = errorMessage;
        this.successfulOperation = false;
    }
}
