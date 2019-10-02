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
import com.movil.utils.DualPropertyRequest;
import com.movil.utils.SHA256;
import com.movil.utils.SinglePropertyRequest;
import com.movil.utils.UserRegistrationRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
        System.out.println("------------------------------------------------");
        System.out.println("[API] GET USERS");
        ArrayList<User> response = DBQueries.selectAllUsers();
        return gson.toJson(new Response(true, "", gson.toJson(response), 200));
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String register(String body) 
    throws NoSuchAlgorithmException, UnsupportedEncodingException{
        System.out.println("------------------------------------------------");
        System.out.println("[API] REGISTER");
        UserRegistrationRequest urr 
            = gson.fromJson(body, UserRegistrationRequest.class);
        if(urr.getPwd().length() > 3){
            if(urr.getPwd().equals(urr.getPwdConfirmation())){
               if(DBQueries.writeUser(urr.getNewUser(), SHA256.hash(urr.getPwd()))){
                    return gson.toJson(new Response(true, "", "User registered successfully!", 200)); 
               }else{
                    return gson.toJson(new Response(false, "It seems to be a connection issue, please try again later.", "", 200)); 
               }
            }else{
                return gson.toJson(new Response(false, "The submitted passwords does not match.", "", 200));  
            } 
        }else{
            return gson.toJson(new Response(false, "The submitted password is invalid.", "", 200));
        }
    }
    
    @POST
    @Path("/{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String login(@PathParam("username") String username, String body) 
    throws ParseException, NoSuchAlgorithmException, UnsupportedEncodingException {
        System.out.println("------------------------------------------------");
        System.out.println("[API] LOGIN");
        DualPropertyRequest spr 
            = gson.fromJson(body, DualPropertyRequest.class);
        User query;
        Response response;
        try {
            query = DBQueries.selectUserWithPwd(username, spr.getFirst_value());
            HashMap<String,String> hm = new HashMap<>();
            hm.put("ip", spr.getLast_value());
            hm.put("lastSeen", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
            hm.put("status", "online");
            if (DBQueries.modifyUser(username, hm)) {
                response = new Response(true, "", gson.toJson(query), 200);
            } else {
                response = new Response(false, "Could not update IP.","",418);
            }
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
        System.out.println("------------------------------------------------");
        System.out.println("[API] GET USER");
        User query;
        Response response;
        query = DBQueries.selectUser(username);
        response = new Response(true, "", gson.toJson(query), 200);
        return gson.toJson(response);
    }
    
    @PUT
    @Path("/{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateUser(@PathParam("username") String username, String body) 
    throws ParseException{
        System.out.println("------------------------------------------------");
        System.out.println("[API] UPDATE USER");
        User oldOne = DBQueries.selectUser(username);
        User newOne = gson.fromJson(body, User.class);
        HashMap<String, String> changes = User.compare(oldOne, newOne);
        if(changes.size() > 0){
            if(DBQueries.modifyUser(username, changes)){
                return gson.toJson(new Response(true, "", "The changes have been successfully applied!", 200));
            }else{
                return gson.toJson(new Response(false, "It seems to be a connection issue, please try again later.", "", 200));
            }
        }else{
            return gson.toJson(new Response(false, "There is no changes to apply.", "", 200));
        }
    }
    
    @DELETE
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteUser(@PathParam("username") String username) {
        System.out.println("------------------------------------------------");
        System.out.println("[API] DELETE USER");
        if(DBQueries.deleteUser(username))
            return gson.toJson(new Response(true, "", "The user has been successfully removed!", 200));
        else
            return gson.toJson(new Response(false, "It seems to be a connection issue, please try again later.", "", 200));
    }
    
    @PUT
    @Path("/updateIp/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateUserIp(@PathParam("username") String username, String body) {
        System.out.println("------------------------------------------------");
        System.out.println("[API] UPDATE USER IP");
        SinglePropertyRequest spr 
            = gson.fromJson(body, SinglePropertyRequest.class);
        if(DBQueries.modifyUserIp(username, spr.getData()))
            return gson.toJson(new Response(true, "", "The user ip has been successfully changed!", 200));
        else
            return gson.toJson(new Response(false, "It seems to be a connection issue, please try again later.", "", 200));
    }
    
    @PUT
    @Path("/toggleStatus/{ip}")
    @Produces(MediaType.APPLICATION_JSON)
    public String toggleUserStatus(@PathParam("ip") String ip, String body) {
        System.out.println("------------------------------------------------");
        System.out.println("[API] UPDATE USER STATUS");
        SinglePropertyRequest spr 
            = gson.fromJson(body, SinglePropertyRequest.class);
        if(DBQueries.modifyUserIp(ip, spr.getData()))
            return gson.toJson(new Response(true, "", "The user status has been successfully changed!", 200));
        else
            return gson.toJson(new Response(false, "It seems to be a connection issue, please try again later.", "", 200));
    }
    
    @GET
    @Path("/logout/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public String logout(@PathParam("username") String username) {
        System.out.println("------------------------------------------------");
        System.out.println("[API] LOGOUT");
        HashMap<String,String> hm = new HashMap<>();
        hm.put("ip", "0.0.0.0");
        hm.put("lastSeen", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
        hm.put("status", "offline");
        if(DBQueries.modifyUser(username, hm))
            return gson.toJson(new Response(true, "", "The user has logged out successfully!", 200));
        else
            return gson.toJson(new Response(false, "It seems to be a connection issue, please try again later.", "", 200));
    }
}
