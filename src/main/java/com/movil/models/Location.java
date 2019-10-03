/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movil.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        return Double.toString(lon);
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
    
    public double getTime(){
        return Long.valueOf(this.location_timestamp.getTime()).doubleValue();
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
    
    public static double distance(double lat1, double lat2, double lon1,
        double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }
    
    public static double getTotalDistance(ArrayList<Location> locations){
        double distance = 0;
        // skip first location
        for(int index = 0; index < locations.size() -1; index++){
            distance += Location.distance(
                locations.get(index).getLat(), 
                locations.get(index +1).getLat(), 
                locations.get(index).getLon(),  
                locations.get(index +1).getLon(), 
                1, 
            1);
        }
        return distance;
    }
    
    public static double getPromSpeed(ArrayList<Location> locations){
        double promSpeed = 0;
        ArrayList<Double> distances = new ArrayList<>();
        ArrayList<Double> times = new ArrayList<>();
        ArrayList<Double> speeds = new ArrayList<>();
        
        // skip first location
        for(int index = 0; index < locations.size() -1; index++){
            distances.add(Location.distance(
                locations.get(index).getLat(), 
                locations.get(index +1).getLat(), 
                locations.get(index).getLon(),  
                locations.get(index +1).getLon(), 
                1, 
                1
            ));
            times.add((locations.get(index +1).getTime() -locations.get(index).getTime()) / 1000);
            speeds.add(distances.get(index)/times.get(index));
            promSpeed += speeds.get(index);
        }
        return promSpeed / speeds.size();
    }
}
