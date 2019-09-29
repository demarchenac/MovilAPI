/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movil.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andres Concha
 * @author Cristhyan De Marchena
 * @author Maximiliam Garcia
 * @author Andres Movilla
 */
public class DBConnection {
    public static Connection create(){
        Properties props = new Properties();
            
        try {
            System.out.println("[API] Loading props");
            InputStream is = DBConnection.class.getClassLoader().getResourceAsStream("config.properties");
            props.load(is);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            props = null;
        }
        
        if(props != null){ 
            String url = "jdbc:sqlite:" +props.getProperty("DB_PATH");
            System.out.println("[API] Attempting to connect to db...");
            Connection dbConnection = null;
            try {
                Class.forName("org.sqlite.JDBC");
                dbConnection = DriverManager.getConnection(url);
                System.out.println("[API] Connection successfully established!");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
            return dbConnection;
        }else{
            return null;
        }
    }
}
