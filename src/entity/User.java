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
import java.util.ArrayList;
import utils.Security;

/**
 *
 * @author thibault
 */
public class User implements UserInterface, Serializable {

    private String userName;
    private String name;
    private String firstName;
    private String password;
    private ArrayList<UserInterface> contacts;
    private ArrayList<ContactRequestInterface> contactRequests;

    public User() {}
    
    public User(String username, String name, String firstName, String plainPassword) {
        this.userName = username;
        this.name = name;
        this.firstName = firstName;
        this.password = Security.encodePassword(plainPassword);
        this.contacts = new ArrayList<>();
        this.contactRequests = new ArrayList<>();
    }
    
    public User(UserInterface user) throws RemoteException {
        this.userName = user.getUsername();
        this.name = user.getName();
        this.firstName = user.getFirstName();
        this.password = user.getPassword();
        this.contacts = user.getContacts();
        this.contactRequests = user.getContactRequest();
    }
    
    @Override
    public String getUsername() throws RemoteException {
        return this.userName;
    }

    @Override
    public String getName() throws RemoteException {
        return this.name;
    }

    @Override
    public String getFirstName() throws RemoteException {
        return this.firstName;
    }

    @Override
    public String getPassword() throws RemoteException {
        return this.password;
    }

    @Override
    public ArrayList<UserInterface> getContacts() throws RemoteException {
        return this.contacts;
    }

    @Override
    public void addContact(UserInterface contact) throws RemoteException {
        if (!this.contacts.contains(contact))
            this.contacts.add(contact);
    }

    @Override
    public void removeContact(UserInterface contact) throws RemoteException {
        if (this.contacts.contains(contact))
            this.contacts.remove(contact);
    }   

    @Override
    public ArrayList<ContactRequestInterface> getContactRequest() throws RemoteException {
        return this.contactRequests;
    }

    @Override
    public void addContactRequest(ContactRequestInterface request) throws RemoteException {
        if (!this.contactRequests.contains(request))
            this.contactRequests.add(request);
    }

    @Override
    public void removeContactRequest(ContactRequestInterface request) throws RemoteException {
        if (this.contactRequests.contains(request))
            this.contactRequests.remove(request);
    }
}
