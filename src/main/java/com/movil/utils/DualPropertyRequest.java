/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movil.utils;

/**
 *
 * @author Andres Concha
 * @author Cristhyan De Marchena
 * @author Maximiliam Garcia
 * @author Andres Movilla
 */
public class DualPropertyRequest {
    private String first_value;
    private String last_value;

    public DualPropertyRequest(String first_value, String last_value) {
        this.first_value = first_value;
        this.last_value = last_value;
    }

    public String getFirst_value() {
        return first_value;
    }

    public void setFirst_value(String first_value) {
        this.first_value = first_value;
    }

    public String getLast_value() {
        return last_value;
    }

    public void setLast_value(String last_value) {
        this.last_value = last_value;
    }
}
