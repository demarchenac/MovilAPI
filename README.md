# MovilAPI
Api for the first term of app develompent course on android studio!

## Controllers
* User Controller
* Location Controller
* Message Controller
* Util Controller

### User Controller Endpoints
* [GET METHOD] @ (http://ip:8080/MovilAPI/api/users) - This method would return all the users.
* [POST METHOD] @ (http://ip:8080/MovilAPI/api/users) - This method is used to register users.<br/>
**The body should look like:**
  ```
    {
        "newUser": {
            "username": "test",
            "first_name": "JOHNNY",
            "last_name": "TEST",
            "full_name": "JOHNNY TEST",
            "email": "jtest@uninorte.edu.co",
            "lastLat": 10.963889,
            "lastLon": -74.796387,
            "status": "online",
            "lastSeen": "2019-09-29 06:21:32.54"
        },
        "pwd": "test",
        "pwdConfirmation": "test"
    }
  ```
* [GET METHOD] @ (http://ip:8080/MovilAPI/api/users/:username) - This method would return the information of an especific user.
* [POST METHOD] @ (http://ip:8080/MovilAPI/api/users/:username) - This method would verify if a user exists and if its data corresponds with its password.<br/>
**The body should look like:**
  ```
    {
      "data" : "some password" 
    }
  ```
* [PUT METHOD] @ (http://ip:8080/MovilAPI/api/users/:username) - This method updates the user info.<br/>
**The body should look like:**
  ```
    {
      "username": "test",
      "first_name": "CARLOS",
      "last_name": "TEST",
      "full_name": "JOHNNY TEST",
      "email": "jtest@uninorte.edu.co",
      "lastLat": 10.963889,
      "lastLon": -74.796387,
      "status": "online",
      "lastSeen": "2019-09-29 08:12:32.54"
    }
  ```
* [DELETE METHOD] @ (http://ip:8080/MovilAPI/api/users/:username) - This method deletes an user from the DB.

### Location Controller Endpoints
* [GET METHOD] @ (http://ip:8080/MovilAPI/api/locations) - This method returns all the updated locations of the users.
* [POST METHOD] @ (http://ip:8080/MovilAPI/api/locations) - This method is used to update/registe a new location.<br/>
**The body should look like:**
  ```
    {
      "lat": 11.000158,
      "lon": -74.789525,
      "location_timestamp": "2019-09-29 14:21:47.56",
      "username": "demarchenac"
    }
  ```
* [GET METHOD] @ (http://ip:8080/MovilAPI/api/locations/:username) - This method will return the registered locations of an user.
* [POST METHOD] @ (http://ip:8080/MovilAPI/api/locations) - This method is used to retrieve a group of a user locations within a date range.<br/>
**The body should look like:**
  ```
    {
      "first_value": "2019-09-28 00:00:00.001",
      "last_value": "2019-09-29 16:00:00.002"
    }
  ```

### Message Controller Endpoints
* [GET METHOD] @ (http://ip:8080/MovilAPI/api/messages) - This method returns all messages ever sent.
* [POST METHOD] @ (http://ip:8080/MovilAPI/api/messages) - This method is used to post/send a new message.<br/>
**The body should look like:**
  ```
    {
      "body": "Probando envio de mensajes 001",
      "message_timestamp": "2019-09-29 14:53:10.15",
      "sender": "demarchenac"
    }
  ```
* [POST METHOD] @ (http://ip:8080/MovilAPI/api/messages/withinDate) - This method is used to retrive a group of messages sent within a date range.<br/>
**The body should look like:**
  ```
    {
      "first_value": "2019-09-28 00:00:00.001",
      "last_value": "2019-09-29 16:00:00.002"
    }
  ```
* [POST METHOD] @ (http://ip:8080/MovilAPI/api/messages/withinRangeLimited) - This method is used to retrive a group of messages sent within a date range with a limit of the 100 most recent messages.<br/>
**The body should look like:**
  ```
    {
      "first_value": "2019-09-28 00:00:00.001",
      "last_value": "2019-09-29 16:00:00.002"
    }
  ```

### Utils Controller Endpoints
* [GET METHOD] @ (http://ip:8080/MovilAPI/api/utils) - This method logs the ip of the machine that holds this service.

## Installation
To use this service you would need to install the following dependencies on your machine:
* Java jdk 1.8
* Glassfish Server v 5.1
* SQLite v 3.29.0

*Side Note:* If you're using Netbeans head up to the services tab and add a new GlassFish server.<br/>
*Side Note:* To change the path of the database that is being used yo will need to go to **MovilAPI/src/main/resources/config.properties** and change the **BD_PATH** property.<br/>

## Project Dependencies
* gson@2.8.5 - https://mvnrepository.com/artifact/com.google.code.gson/gson
* sqlite-jdbc@3.27.2.1 - https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc
