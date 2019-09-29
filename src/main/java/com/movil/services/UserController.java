/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movil.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.movil.exceptions.InvalidPasswordException;
import com.movil.exceptions.WrongPasswordException;
import com.movil.models.Response;
import com.movil.models.User;
import com.movil.utils.DBQueries;
import com.movil.utils.SinglePropertyRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
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
@Path("users")
public class UserController {
    private final Gson gson;

    public UserController() {
        this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .setDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
            .create();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getUsers() throws ParseException{
        ArrayList<User> response = DBQueries.selectAllUsers();
        return gson.toJson(new Response(true, "", gson.toJson(response), 200));
    }
    
    //post user registration is missing!
    
    @POST
    @Path("/{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String login(@PathParam("username") String username, String body) 
    throws ParseException, NoSuchAlgorithmException, UnsupportedEncodingException {
        SinglePropertyRequest spr 
            = gson.fromJson(body, SinglePropertyRequest.class);
        User query;
        Response response;
        try {
            query = DBQueries.selectUserWithPwd(username, spr.getData());
            response = new Response(true, "", gson.toJson(query), 200);
        } catch (WrongPasswordException ex) {
            response = new Response(false, ex.getErrormsg(), "", 200);
        } catch (InvalidPasswordException ex) {
            response = new Response(false, ex.getErrormsg(), "", 200);
        }
        return gson.toJson(response);
    }
    
    @GET
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUser(@PathParam("username") String username) 
    throws ParseException, NoSuchAlgorithmException, UnsupportedEncodingException {
        User query;
        Response response;
        query = DBQueries.selectUser(username);
        response = new Response(true, "", gson.toJson(query), 200);
        return gson.toJson(response);
    }
    
    //put info from logout is missing
}
