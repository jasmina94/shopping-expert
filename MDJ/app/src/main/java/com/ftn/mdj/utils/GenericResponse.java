package com.ftn.mdj.utils;

import android.os.Message;

/**
 * Created by Jasmina on 17/05/2018.
 */

public class GenericResponse<T> {

    private boolean successfulOperation;
    private T entity;
    private String errorMessage;

    public GenericResponse(){}

    public void success(T entity){
        this.entity = entity;
        this.successfulOperation = true;
    }

    public void error(String errorMessage){
        this.errorMessage = errorMessage;
        this.successfulOperation = false;
    }

    public boolean isSuccessfulOperation() {
        return successfulOperation;
    }

    public void setSuccessfulOperation(boolean successfulOperation) {
        this.successfulOperation = successfulOperation;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


    public static Message getGenericServerErrorResponseMessage() {
        Message m = Message.obtain();
        GenericResponse<Object> response = new GenericResponse<Object>();
        response.error("Server error occurred!");
        m.obj = response;
        return m;
    }
}
