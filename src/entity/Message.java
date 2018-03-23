/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import interfaces.MessageInterface;
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
public class Message implements MessageInterface, Serializable {

    private UserInterface sender;
    private Date date;
    private String content;

    public Message(UserInterface sender, Date date, String content) {
        this.sender = sender;
        this.date = date;
        this.content = content;
    }

    @Override
    public UserInterface getSender() throws RemoteException {
        return sender;
    }

    @Override
    public void setSender(UserInterface sender) throws RemoteException {
        this.sender = sender;
    }

    @Override
    public Date getDate() throws RemoteException {
        return date;
    }

    @Override
    public void setDate(Date date) throws RemoteException {
        this.date = date;
    }

    @Override
    public String getContent() throws RemoteException {
        return content;
    }

    @Override
    public void setContent(String content) throws RemoteException {
        this.content = content;
    }
    
    @Override
    public String toString() {
        String str = "";
        try {
            str = "[" + this.getDate().toString() + "] " + this.getSender().getUsername() + " : " + this.getContent();
        } catch (RemoteException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return str;
    }    
}
