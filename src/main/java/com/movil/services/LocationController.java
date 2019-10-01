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
public class LocationController implements TCPServiceManagerCallerInterface {

    private final Gson gson;
    ClientSocketManager clientSocketManager;
    Properties props;

    public LocationController() {
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
    public String getUsers() throws ParseException {
        ArrayList<RTL> response = DBQueries.getCurrentLocations();
        return gson.toJson(new Response(true, "", gson.toJson(response), 200));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String writeLocation(String body)
    throws ParseException, InterruptedException {
        Location location = gson.fromJson(body, Location.class);
        if (DBQueries.writeLocation(location)) {
            System.out.println("[API] Location written!");
            User oldUser = DBQueries.selectUser(location.getUsername());
            User user = DBQueries.selectUser(location.getUsername());
            user.setLastLat(location.getLat());
            user.setLastLon(location.getLon());
            user.setLastSeen(location.getLocation_timestamp());
            HashMap<String, String> changes = User.compare(oldUser, user);
            if (changes.size() > 0) {
                if (DBQueries.modifyUser(user.getUsername(), changes)) {
                    if (props != null) {
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
                            clientSocketManager.SendMessage("update@locations");
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(LocationController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            clientSocketManager.clearLastSocket();
                        }
                    }
                    return gson.toJson(new Response(true, "", "Location successfully updated!", 200));
                } else {
                    return gson.toJson(new Response(false, "It seems to be a connection issue, please try again later.", "", 200));
                }
            } else {
                return gson.toJson(new Response(false, "There are no actual changes to be made...", "", 200));
            }
        } else {
            return gson.toJson(new Response(false, "It seems to be a connection issue, please try again later.", "", 200));
        }
    }

    @GET
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUserLocations(@PathParam("username") String username) throws ParseException {
        ArrayList<Location> response = DBQueries.getUserLocations(username);
        return gson.toJson(new Response(true, "", gson.toJson(response), 200));
    }

    @POST
    @Path("/{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getUserLocationsWithinDate(@PathParam("username") String username, String body)
            throws ParseException {
        DualPropertyRequest dpr = gson.fromJson(body, DualPropertyRequest.class);
        ArrayList<Location> response
                = DBQueries.getUserLocationsWithinDate(username, dpr.getFirst_value(), dpr.getLast_value());
        return gson.toJson(new Response(true, "", gson.toJson(response), 200));
    }

    @Override
    public void MessageReceiveFromClient(Socket clientSocket, byte[] data) {

    }

    @Override
    public void ErrorHasBeenThrown(Exception error) {

    }
}
