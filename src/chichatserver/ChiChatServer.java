/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chichatserver;

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
            System.out.println("[TEST AUTHENTICATE] " + userServiceInterface.authenticateUser("root", "root"));
        } catch (NotBoundException | AccessException ex) {
            Logger.getLogger(ChiChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
