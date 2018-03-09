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
     * @param request
     * @return
     * @throws RemoteException
     * @throws Exception 
     */
    public UserInterface sendContactRequest(ContactRequestInterface request) throws RemoteException, Exception;
    
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
     * @return 
     * @throws RemoteException 
     */
    public UserInterface answerToContactRequest(boolean answer, ContactRequestInterface request) throws RemoteException;
    
    /**
     * 
     * @param username
     * @param name
     * @param firstName
     * @param plainPassword
     * @return
     * @throws RemoteException 
     */
    public UserInterface createUser(String username, String name, String firstName, String plainPassword) throws RemoteException;
    
    
}
