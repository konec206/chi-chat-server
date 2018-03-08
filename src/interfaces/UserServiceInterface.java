/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author thibault
 */
public interface UserServiceInterface extends Remote {
    /**
     * 
     * @param username
     * @param plainPassword
     * @return
     * @throws RemoteException 
     */
    public boolean authenticateUser(String username, String plainPassword) throws RemoteException;
    
    /**
     * 
     * @param senderUserName
     * @param receiverUserName
     * @return
     * @throws RemoteException
     * @throws Exception 
     */
    public boolean sendContactRequest(String senderUserName, String receiverUserName) throws RemoteException, Exception;
    
    /**
     * 
     * @param userName
     * @return 
     * @throws java.rmi.RemoteException 
     */
    public UserInterface getUser(String userName) throws RemoteException, Exception;
    
    /**
     * 
     * @param answer
     * @param request
     * @throws RemoteException 
     */
    public UserInterface answerToContactRequest(boolean answer, ContactRequestInterface request) throws RemoteException;
}
