/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import interfaces.ChatInterface;
import interfaces.MessageInterface;
import interfaces.UserInterface;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author thibault
 */
public class Chat implements ChatInterface, Serializable {
    
    private ArrayList<MessageInterface> messages;
    private ArrayList<UserInterface> users;

    public Chat() {
        this.users = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    @Override
    public ArrayList<UserInterface> getUsers() throws RemoteException {
        return this.users;
    }

    @Override
    public void addUser(UserInterface user) throws RemoteException {
        if (!this.users.contains(user))
            this.users.add(user);
    }

    @Override
    public void removeUser(UserInterface user) throws RemoteException {
        if (this.users.contains(user))
            this.users.remove(user);
    }

    @Override
    public ArrayList<MessageInterface> getMessages() throws RemoteException {
        return this.messages;
    }

    @Override
    public void addMessage(MessageInterface message) throws RemoteException {
        this.messages.add(message);
    }

    @Override
    public boolean join(UserInterface user) throws RemoteException {
        this.addUser(user);
        
        //TODO get previous version
        
        return true;
    }

    @Override
    public boolean quit(UserInterface user) throws RemoteException {
        this.removeUser(user);
        
        //TODO save this version
        
        return true;
    }
    
}
