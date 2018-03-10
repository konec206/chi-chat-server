/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chichatserver;

import entity.ContactRequest;
import entity.User;
import interfaces.ContactRequestInterface;
import interfaces.UserInterface;
import interfaces.UserServiceInterface;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thibault
 */
public class ChiChatServer {

    /**
     * @param args the command line arguments
     * @throws java.rmi.RemoteException
     */
    public static void main(String[] args) throws RemoteException, Exception {
        //Server host
        String host = "127.0.0.1";
        //BD Server port
        int bdPort = 3000;

        //BD Registry
        Registry bdRegistry = LocateRegistry.getRegistry(host, bdPort);

        UserServiceInterface userServiceInterface;

        try {
            userServiceInterface = (UserServiceInterface) bdRegistry.lookup("userService");
            System.out.println("[SERVER] UserService registered...");
            
            //Just for the tests
            ArrayList<UserInterface> users = new ArrayList();
            users.add(new User("root1", "fake", "user", "root"));
            users.add(new User("root2", "fake", "user", "root"));
            users.add(new User("root3", "fake", "user", "root"));
            users.add(new User("root4", "fake", "user", "root"));
            users.add(new User("root5", "fake", "user", "root"));
            users.add(new User("root6", "fake", "user", "root"));
            
            userServiceInterface.initUserRepository(users);

            Scanner sc = new Scanner(System.in);
            System.out.println("[TEST] Please authenticate (authentication gonna be client side) : ");

            System.out.print("User name :\n> ");
            String username = sc.nextLine();

            System.out.print("Password :\n> ");
            String password = sc.nextLine();

            if (!userServiceInterface.authenticateUser(username, password)) {
                System.out.println("Authentication refused " + username);
            } else {
                System.out.println("Authentication OK " + username);
                UserInterface userConnected = userServiceInterface.getUser(username);

                try {
                    System.out.print("Username to request :\n> ");
                    String usernameToRequest = sc.nextLine();
                    
                    UserInterface userToRequest = userServiceInterface.getUser(usernameToRequest);
                    
                    System.out.println("[TEST CONTACT REQUEST] " + userConnected.getUsername() + " --> " + userToRequest.getUsername());

                    ContactRequest requestToSend = new ContactRequest(userConnected, userToRequest, new Date());
                    
                    userToRequest = userServiceInterface.sendContactRequest(requestToSend);
                    
                    if (userToRequest != null) {
                        System.out.println("[TEST CONTACT REQUEST] A contactRequest has been created");                        
                    }                  

                    System.out.println("[TEST GET USER] Display of " + userToRequest.getUsername() + " --> contactRequests : " + userToRequest.getContactRequest().size());
                    for (ContactRequestInterface request : userToRequest.getContactRequest()) {
                        System.out.println(request.toString());
                    }

                    Thread.sleep(1000);

                    System.out.println("[TEST ACCEPT CONTACT REQUEST]");

                    userToRequest = userServiceInterface.answerToContactRequest(true, userToRequest.getContactRequest().get(0));

                    System.out.println("[TEST ACCEPT CONTACT REQUEST] Contact requests : " + userToRequest.getContactRequest().size());
                    System.out.println("[TEST ACCEPT CONTACT REQUEST] Contact : " + userToRequest.getContacts().size());

                    for (UserInterface contact : userToRequest.getContacts()) {
                        System.out.println("Contact : " + contact.getFirstName() + " " + contact.getName());
                    }

                } catch (Exception ex) {
                    Logger.getLogger(ChiChatServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        } catch (NotBoundException | AccessException ex) {
            Logger.getLogger(ChiChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
