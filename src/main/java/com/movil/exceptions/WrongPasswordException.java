/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movil.exceptions;

/**
 *
 * @author Andres Concha
 * @author Cristhyan De Marchena
 * @author Maximiliam Garcia
 * @author Andres Movilla
 */
public class WrongPasswordException extends Exception{
    private String errormsg;
    public WrongPasswordException(String errormsg) {
        super(errormsg);
        this.errormsg = errormsg;
    }

    public String getErrormsg() {
        return errormsg;
    }
}
