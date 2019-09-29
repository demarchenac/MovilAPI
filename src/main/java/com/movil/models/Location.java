/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movil.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Andres Concha
 * @author Cristhyan De Marchena
 * @author Maximiliam Garcia
 * @author Andres Movilla
 */
public class Location {
    private double lat;
    private double lon;
    private Date location_timestamp;
    private String username;

    public Location(
        String lat, String lon, String location_timestamp, String username
    ) throws ParseException {
        this.lat = Double.parseDouble(lat);
        this.lon = Double.parseDouble(lon);
        this.location_timestamp 
            = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
                .parse(location_timestamp);
        this.username = username;
    }

    public double getLat() {
        return lat;
    }

    public String getLatString() {
        return Double.toString(lat);
    }
    
    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public String getLonString() {
        return Double.toString(lat);
    }
    
    public void setLon(double lon) {
        this.lon = lon;
    }

    public Date getLocation_timestamp() {
        return location_timestamp;
    }

    public String getLocation_timestampISOFormatted(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(location_timestamp);
    }
    
    public void setLocation_timestamp(Date location_timestamp) {
        this.location_timestamp = location_timestamp;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
