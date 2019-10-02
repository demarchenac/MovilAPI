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
import com.prog.distribuida.tcp.ClientSocketManager;
import com.prog.distribuida.tcp.TCPServiceManagerCallerInterface;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class MessageController implements TCPServiceManagerCallerInterface{
    private final Gson gson;
    ClientSocketManager clientSocketManager;
    Properties props;

    public MessageController() {
        this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .setDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
            .create();
        props = new Properties();
        try {
            System.out.println("[API] Loading props");
            InputStream is = LocationController.class.getClassLoader().getResourceAsStream("config.properties");
            props.load(is);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            props = null;
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getMessages() throws ParseException{System.out.println("------------------------------------------------");
        System.out.println("[API] GET MESSAGES");
        ArrayList<Message> response = DBQueries.getMessages();
        return gson.toJson(new Response(true, "", gson.toJson(response), 200));
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String writeMessage(String body) throws ParseException{
        System.out.println("------------------------------------------------");
        System.out.println("[API] WRITE MESSAGE");
        Message message = gson.fromJson(body, Message.class);
        if(DBQueries.writeMessage(message)){
            User oldUser = DBQueries.selectUser(message.getSender());
            User user = DBQueries.selectUser(message.getSender());
            user.setLastSeen(message.getMessage_timestamp());
            HashMap<String, String> changes = User.compare(oldUser, user);
            if (changes.size() > 0) {
                if(DBQueries.modifyUser(user.getUsername(), User.compare(oldUser, user))){
                    if (props != null) {
                        System.out.println("[API] CREATING SOCKET");
                        clientSocketManager = new ClientSocketManager(
                                props.getProperty("SOCKET_CONNECTION_IP"),
                                Integer.parseInt(props.getProperty("SOCKET_CONNECTION_PORT")),
                                this);
                        if (clientSocketManager != null) {
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(LocationController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            System.out.println("[API] NOTIFY PUSH/PULL NOTIFICATION SERVICE");
                            clientSocketManager.SendMessage("update@messages");
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(LocationController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            clientSocketManager.clearLastSocket();
                        }
                    }
                    return gson.toJson(new Response(true, "", "The message has been successfully sent!", 200));
                }else{
                    return gson.toJson(new Response(false, "It seems to be a connection issue, please try again later.", "", 200)); 
                }
            }else{
                return gson.toJson(new Response(false, "There are no actual changes to be made...", "", 200));
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
        System.out.println("------------------------------------------------");
        System.out.println("[API] GET MESSAGES WITHIN RANGE");
        DualPropertyRequest dpr = gson.fromJson(body, DualPropertyRequest.class);
        ArrayList<Message> response = DBQueries.getMessagesWithinDate(dpr.getFirst_value(), dpr.getLast_value());
        return gson.toJson(new Response(true, "", gson.toJson(response), 200));
    }
    
    @POST
    @Path("/withinRangeLimited")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getMessagesWithinDateLimited(String body) throws ParseException{
        System.out.println("------------------------------------------------");
        System.out.println("[API] GET MESSAGES WITHIN RANGE LIMITED BY 100");
        DualPropertyRequest dpr = gson.fromJson(body, DualPropertyRequest.class);
        ArrayList<Message> response 
            = DBQueries.getMessagesWithinDateLimited(dpr.getFirst_value(), dpr.getLast_value());
        return gson.toJson(new Response(true, "", gson.toJson(response), 200));
    }

    @Override
    public void MessageReceiveFromClient(Socket clientSocket, byte[] data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void ErrorHasBeenThrown(Exception error) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
