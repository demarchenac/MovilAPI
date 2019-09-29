# MovilAPI
Api for the first term of app develompent course on android studio!

## Controllers
* User Controller
* Location Controller
* Message Controller
* Util Controller

### User Controller Endpoints
* [GET METHOD] @ (http://ip:8080/MovilAPI/api/users) - This method would return all the users.
* [POST METHOD] @ (http://ip:8080/MovilAPI/api/users) - **Under development**
* [GET METHOD] @ (http://ip:8080/MovilAPI/api/users/:username) - This method would return the information of an especific user.
* [POST METHOD] @ (http://ip:8080/MovilAPI/api/users/:username) - This method would verify if a user exists and if its data corresponds with its password.<br/>
**The body should look like:**
  ```
    {
      "data" : "some password" 
    }
  ```
* [PUT METHOD] @ (http://ip:8080/MovilAPI/api/users/:username) - **Under development**
* [DELETE METHOD] @ (http://ip:8080/MovilAPI/api/users/:username) - **Under development**

### Location Controller Endpoints
- **Under development**

### Message Controller Endpoints
- **Under development**

### Message Controller Endpoints
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
