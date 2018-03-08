/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chichatserver;

import interfaces.ContactRequestInterface;
import interfaces.UserInterface;
import interfaces.UserServiceInterface;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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
    public static void main(String[] args) throws RemoteException {
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
            System.out.println("[TEST AUTHENTICATE] " + userServiceInterface.authenticateUser("root1", "root"));
            try {
                System.out.println("[TEST CONTACT REQUEST] root1 -> root2");
                if (userServiceInterface.sendContactRequest("root1", "root2"))
                    System.out.println("[TEST CONTACT REQUEST] A contactRequest has been created");
                
                System.out.println("[TEST GET USER]");
                UserInterface user = userServiceInterface.getUser("root2");
                System.out.println("Fetched user : " + user.getFirstName() + " " + user.getName());
                
                System.out.println("[TEST GET USER] Display of contactRequests : " + user.getContactRequest().size());  
                for(ContactRequestInterface request : user.getContactRequest()) {
                    System.out.println(request.toString());
                }
                
                Thread.sleep(1000);
                
                System.out.println("[TEST ACCEPT CONTACT REQUEST]");
                
                user = userServiceInterface.answerToContactRequest(true, user.getContactRequest().get(0));
                
                System.out.println("[TEST ACCEPT CONTACT REQUEST] Contact requests : " + user.getContactRequest().size());
                System.out.println("[TEST ACCEPT CONTACT REQUEST] Contact : " + user.getContacts().size());
                
                for(UserInterface contact : user.getContacts()) {
                    System.out.println("Contact : " + contact.getFirstName() + " " + contact.getName());
                }
                
            } catch (Exception ex) {
                Logger.getLogger(ChiChatServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NotBoundException | AccessException ex) {
            Logger.getLogger(ChiChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
