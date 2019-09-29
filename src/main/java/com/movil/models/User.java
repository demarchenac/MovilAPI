/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movil.models;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 *
 * @author Andres Concha
 * @author Cristhyan De Marchena
 * @author Maximiliam Garcia
 * @author Andres Movilla
 */
public class User {
    private String username;
    private String first_name;
    private String last_name;
    private String full_name;
    private String email;
    private double lastLat;
    private double lastLon;
    private String status;
    private Date lastSeen;

    public User() throws ParseException {
    }
    
    public User(
        String username, String first_name, String last_name, String name, 
        String email, String lastLat, String lastLon, String status, 
        String lastSeen
    ) throws ParseException {
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.full_name = name;
        this.email = email;
        this.lastLat = Double.parseDouble(lastLat);
        this.lastLon = Double.parseDouble(lastLon);
        this.status = status;
        this.lastSeen = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(lastSeen);
    }

    public static HashMap<String, String> compare(User oldOne, User newOne){
        HashMap<String, String> differences = new HashMap<>();
        if(oldOne.username.equals(newOne.username)){
            if(!oldOne.first_name.equals(newOne.first_name)){
                differences.put("first_name", newOne.first_name);
            }else if(!oldOne.last_name.equals(newOne.last_name)){
                differences.put("last_name", newOne.last_name);
            }else if(!oldOne.full_name.equals(newOne.full_name)){
                differences.put("full_name", newOne.full_name);
            }else if(!oldOne.email.equals(newOne.email)){
                differences.put("email", newOne.email);
            }else if(!oldOne.getLastLatString().equals(newOne.getLastLatString())){
                differences.put("lastLat", newOne.getLastLatString());
            }else if(!oldOne.getLastLonString().equals(newOne.getLastLonString())){
                differences.put("lastLon", newOne.getLastLonString());
            }else if(!oldOne.status.equals(newOne.status)){
                differences.put("status", newOne.status);
            }else if(!oldOne.getLastSeenISOFormatted().equals(newOne.getLastSeenISOFormatted())){
                differences.put("lastSeen", newOne.getLastSeenISOFormatted());
            }
        }
        return differences;
    }
    
    public boolean isOnline(){
        return this.status.toLowerCase().trim().equals("online");
    }
    
    public void setLastLat(String lastLat) {
        this.lastLat = Double.parseDouble(lastLat);;
    }
    
    public void setLastLon(String lastLon) {
        this.lastLon = Double.parseDouble(lastLon);;
    }
    
    public void setLastSeen(String lastSeen) throws ParseException {
        this.lastSeen = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(lastSeen);
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getLastLat() {
        return lastLat;
    }
    
    public String getLastLatString() {
        return Double.toString(lastLat);
    }

    public void setLastLat(double lastLat) {
        this.lastLat = lastLat;
    }

    public double getLastLon() {
        return lastLon;
    }

    public String getLastLonString() {
        return Double.toString(lastLon);
    }
    
    public void setLastLon(double lastLon) {
        this.lastLon = lastLon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLastSeen() {
        return lastSeen;
    }

    public String getLastSeenISOFormatted(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(lastSeen);
    }
    
    public void setLastSeen(Date lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }
    
    
}
