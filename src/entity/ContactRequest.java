/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import interfaces.ContactRequestInterface;
import interfaces.UserInterface;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thibault
 */
public class ContactRequest implements ContactRequestInterface, Serializable {
    
    //Test
    private static final long serialVersionUID = 198144564964678012L;
    
    private User sender;
    private User receiver;
    private Date dateCreated;
    private int status;

    /**
     * 
     * @param sender
     * @param receiver
     * @param dateCreated 
     * @throws java.rmi.RemoteException 
     */
    public ContactRequest(UserInterface sender, UserInterface receiver, Date dateCreated) throws RemoteException {
        this.sender = new User(sender);
        this.receiver = new User(receiver);
        this.dateCreated = dateCreated;
        this.status = CONTACT_REQUEST_STATUS_SENT;
    }

    /**
     * 
     * @return 
     */
    @Override
    public Date getDateCreated() throws RemoteException {
        return this.dateCreated;
    }

    /**
     * 
     * @param dateCreated 
     */
    @Override
    public void setDateCreated(Date dateCreated) throws RemoteException {
        this.dateCreated = dateCreated;
    }

    /**
     * 
     * @return 
     */
    @Override
    public UserInterface getSender() throws RemoteException {
        return this.sender;
    }

    /**
     * 
     * @return 
     */
    @Override
    public UserInterface getReceiver() throws RemoteException {
        return this.receiver;
    }

    /**
     * 
     * @param sender 
     */
    @Override
    public void setSender(User sender) throws RemoteException {
        this.sender = sender;
    }

    /**
     * 
     * @param receiver 
     */
    @Override
    public void setReceiver(User receiver) throws RemoteException {
        this.receiver = receiver;
    }

    /**
     * 
     * @return 
     */
    @Override
    public int getStatus() {
        return status;
    }

    /**
     * 
     * @param status 
     */
    @Override
    public void setStatus(int status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        String str = "";
        
        try {
            str += this.getSender().getFirstName() + " " + this.getSender().getName() 
                    + " --> " + this.getReceiver().getFirstName() + " " + this.getReceiver().getName() + "["+ this.getDateCreated().toString() +"]";
        } catch (RemoteException ex) {
            Logger.getLogger(ContactRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return str;
    }
}
