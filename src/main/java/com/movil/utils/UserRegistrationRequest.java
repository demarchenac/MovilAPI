/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movil.utils;

import com.movil.models.User;

/**
 *
 * @author Andres Concha
 * @author Cristhyan De Marchena
 * @author Maximiliam Garcia
 * @author Andres Movilla
 */
public class UserRegistrationRequest {
    private User newUser;
    private String pwd;
    private String pwdConfirmation;

    public UserRegistrationRequest(User newUser, String pwd, String pwdConfirmation) {
        this.newUser = newUser;
        this.pwd = pwd;
        this.pwdConfirmation = pwdConfirmation;
    }

    public User getNewUser() {
        return newUser;
    }

    public void setNewUser(User newUser) {
        this.newUser = newUser;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPwdConfirmation() {
        return pwdConfirmation;
    }

    public void setPwdConfirmation(String pwdConfirmation) {
        this.pwdConfirmation = pwdConfirmation;
    }
}
