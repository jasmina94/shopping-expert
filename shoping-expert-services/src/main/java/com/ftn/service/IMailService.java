package com.ftn.service;

/**
 * Created by Jasmina on 22/05/2018.
 */
public interface IMailService {

    boolean sendForgottenPassword(String emailTo, String password, String firstname, String lastname);
}
