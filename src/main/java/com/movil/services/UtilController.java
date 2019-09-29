/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movil.services;

import com.google.gson.Gson;
import com.movil.models.Response;
import com.movil.utils.SinglePropertyRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.ws.rs.GET;
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
@Path("utils")
public class UtilController {
    
    private final Gson gson;

    public UtilController() {
        this.gson = new Gson();
    }   
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getIp() throws UnknownHostException{
        SinglePropertyRequest spr 
                = new SinglePropertyRequest(InetAddress.getLocalHost().getHostAddress());
        return gson.toJson(new Response(true, "", this.gson.toJson(spr), 200));
    }
}
