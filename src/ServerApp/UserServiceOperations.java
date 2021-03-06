package ServerApp;


/**
* ServerApp/UserServiceOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Server.idl
* jeudi 22 mars 2018 09 h 04 CET
*/

public interface UserServiceOperations 
{
  boolean authenticate (String username, String password);
  String register (String username, String password, String name, String firstname);
  void newContactRequest (String username1, String username2);
  String getContactsDisplay (String username);
  String getLastContactRequests (String username);
  void answerToContactRequest (String username, boolean answer);
} // interface UserServiceOperations
