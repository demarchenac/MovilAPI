/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movil.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.movil.models.Message;
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
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Andres Concha
 * @author Cristhyan De Marchena
 * @author Maximiliam Garcia
 * @author Andres Movilla
 */
@Path("messages")
public class MessageController {
    private final Gson gson;

    public MessageController() {
        this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .setDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
            .create();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getMessages() throws ParseException{
        ArrayList<Message> response = DBQueries.getMessages();
        return gson.toJson(new Response(true, "", gson.toJson(response), 200));
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String writeMessage(String body) throws ParseException{
        Message message = gson.fromJson(body, Message.class);
        if(DBQueries.writeMessage(message)){
            User oldUser = DBQueries.selectUser(message.getSender());
            User user = DBQueries.selectUser(message.getSender());
            user.setLastSeen(message.getMessage_timestamp());
            if(DBQueries.modifyUser(user.getUsername(), User.compare(oldUser, user))){
                return gson.toJson(new Response(true, "", "The message has been successfully sent!", 200));
            }else{
                return gson.toJson(new Response(false, "It seems to be a connection issue, please try again later.", "", 200)); 
            }
        }else{
           return gson.toJson(new Response(false, "It seems to be a connection issue, please try again later.", "", 200)); 
        }
    }
    
    @POST
    @Path("/withinDate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getMessagesWithinRange(String body) throws ParseException{
        DualPropertyRequest dpr = gson.fromJson(body, DualPropertyRequest.class);
        ArrayList<Message> response = DBQueries.getMessagesWithinDate(dpr.getFirst_value(), dpr.getLast_value());
        return gson.toJson(new Response(true, "", gson.toJson(response), 200));
    }
    
    @POST
    @Path("/withinRangeLimited")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getMessagesWithinDateLimited(String body) throws ParseException{
        DualPropertyRequest dpr = gson.fromJson(body, DualPropertyRequest.class);
        ArrayList<Message> response 
            = DBQueries.getMessagesWithinDateLimited(dpr.getFirst_value(), dpr.getLast_value());
        return gson.toJson(new Response(true, "", gson.toJson(response), 200));
    }
}
