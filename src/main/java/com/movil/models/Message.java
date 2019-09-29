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
public class Message {
    private String body;
    private Date message_timestamp;
    private String sender;

    public Message(
        String body, String message_timestamp, String sender
    ) throws ParseException {
        this.body = body;
        this.message_timestamp
            = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
                .parse(message_timestamp);
        this.sender = sender;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getMessage_timestamp() {
        return message_timestamp;
    }
    
    public String getMessage_timestampISOFormatted(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(message_timestamp);
    }

    public void setMessage_timestamp(Date message_timestamp) {
        this.message_timestamp = message_timestamp;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
