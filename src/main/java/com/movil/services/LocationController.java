/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movil.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.movil.models.Location;
import com.movil.models.RTL;
import com.movil.models.Response;
import com.movil.models.User;
import com.movil.utils.DBQueries;
import com.movil.utils.DualPropertyRequest;
import java.text.ParseException;
import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Andres Concha
 * @author Cristhyan De Marchena
 * @author Maximiliam Garcia
 * @author Andres Movilla
 */
@Path("locations")
public class LocationController {
    private final Gson gson;

    public LocationController() {
        this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .setDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
            .create();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getUsers() throws ParseException{
        ArrayList<RTL> response = DBQueries.getCurrentLocations();
        return gson.toJson(new Response(true, "", gson.toJson(response), 200));
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String writeLocation(String body)
        throws ParseException{
        Location location = gson.fromJson(body, Location.class);
        if(DBQueries.writeLocation(location)){
            System.out.println("[API] Location written!");
            User oldUser = DBQueries.selectUser(location.getUsername());
            User user = DBQueries.selectUser(location.getUsername());
            user.setLastLat(location.getLat());
            user.setLastLon(location.getLon());
            user.setLastSeen(location.getLocation_timestamp());
            if(DBQueries.modifyUser(user.getUsername(), User.compare(oldUser, user))){
                return gson.toJson(new Response(true, "", "Location successfully updated!", 200));
            }else{
                return gson.toJson(new Response(false, "It seems to be a connection issue, please try again later.", "", 200)); 
            }
        }else{
            return gson.toJson(new Response(false, "It seems to be a connection issue, please try again later.", "", 200)); 
        }
    }
    
    @GET
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUserLocations(@PathParam("username") String username) throws ParseException{
        ArrayList<Location> response = DBQueries.getUserLocations(username);
        return gson.toJson(new Response(true, "", gson.toJson(response), 200));
    }
    
    @POST
    @Path("/{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getUserLocationsWithinDate(@PathParam("username") String username, String body)
        throws ParseException{
        DualPropertyRequest dpr = gson.fromJson(body, DualPropertyRequest.class);
        ArrayList<Location> response 
            = DBQueries.getUserLocationsWithinDate(username, dpr.getFirst_value(), dpr.getLast_value());
        return gson.toJson(new Response(true, "", gson.toJson(response), 200));
    }
}
