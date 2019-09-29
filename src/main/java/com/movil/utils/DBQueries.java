/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movil.utils;

import com.movil.exceptions.InvalidPasswordException;
import com.movil.exceptions.WrongPasswordException;
import com.movil.models.User;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;

/**
 *
 * @author Andres Concha
 * @author Cristhyan De Marchena
 * @author Maximiliam Garcia
 * @author Andres Movilla
 */
public class DBQueries {
    public static final ArrayList<User> selectAllUsers() throws ParseException{
        ArrayList<User> queryResult = new ArrayList<>();
        String query = "SELECT * FROM " +Constants.USERS_TABLE +";";
        Connection dbConnection = DBConnection.create();
        try(
            Statement stmt = dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
        ){
            System.out.println("[API] Fetching results...");
            while (rs.next()) {
                queryResult.add(
                    new User(
                        rs.getString("username"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("lastLat"),
                        rs.getString("lastLon"),
                        rs.getString("status"),
                        rs.getString("lastSeen")
                    )
                );
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }finally{
            try {
                if (dbConnection != null) {
                    System.out.println("[API] Closing connection...");
                    dbConnection.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return queryResult;
    }

    public static final User selectUserWithPwd(String username, String pwd) 
    throws ParseException, WrongPasswordException, NoSuchAlgorithmException, 
    UnsupportedEncodingException, InvalidPasswordException {
        User queryResult = new User();
        String query = "SELECT * FROM " +Constants.USERS_TABLE +" WHERE username == ? ;";
        Connection dbConnection = DBConnection.create();
        try{
            PreparedStatement pstmt = dbConnection.prepareStatement(query);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("[API] Fetching results...");
            while (rs.next()) {
                if(pwd.length() > 0){
                    if(rs.getString("pwd").length() > 0){
                        if(SHA256.hash(pwd).equals(rs.getString("pwd"))){
                            queryResult.setUsername(rs.getString("username"));
                            queryResult.setFirst_name(rs.getString("first_name"));
                            queryResult.setLast_name(rs.getString("last_name"));
                            queryResult.setFull_name(rs.getString("full_name"));
                            queryResult.setEmail(rs.getString("email"));
                            queryResult.setLastLat(rs.getString("lastLat"));
                            queryResult.setLastLon(rs.getString("lastLon"));
                            queryResult.setStatus(rs.getString("status"));
                            queryResult.setLastSeen(rs.getString("lastSeen"));
                        }else{
                            throw new WrongPasswordException("Wrong password");
                        }
                    }else{
                        throw new InvalidPasswordException("The stored password in the system is invalid.");
                    }
                }else{
                    throw new InvalidPasswordException("The submitted password is invalid.");
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try {
                if (dbConnection != null) {
                    System.out.println("[API] Closing connection...");
                    dbConnection.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        
        return queryResult;
    }
    
    public static final User selectUser(String username) throws ParseException {
        User queryResult = new User();
        String query = "SELECT * FROM " +Constants.USERS_TABLE +" WHERE username == ? ;";
        Connection dbConnection = DBConnection.create();
        try{
            PreparedStatement pstmt = dbConnection.prepareStatement(query);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("[API] Fetching results...");
            while (rs.next()) {
                queryResult.setUsername(rs.getString("username"));
                queryResult.setFirst_name(rs.getString("first_name"));
                queryResult.setLast_name(rs.getString("last_name"));
                queryResult.setFull_name(rs.getString("full_name"));
                queryResult.setEmail(rs.getString("email"));
                queryResult.setLastLat(rs.getString("lastLat"));
                queryResult.setLastLon(rs.getString("lastLon"));
                queryResult.setStatus(rs.getString("status"));
                queryResult.setLastSeen(rs.getString("lastSeen"));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try {
                if (dbConnection != null) {
                    System.out.println("[API] Closing connection...");
                    dbConnection.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return queryResult;
    }
}
