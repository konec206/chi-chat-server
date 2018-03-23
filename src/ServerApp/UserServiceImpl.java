/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerApp;

import entity.ContactRequest;
import entity.User;
import interfaces.ContactRequestInterface;
import interfaces.UserInterface;
import interfaces.UserServiceInterface;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.omg.CORBA.ORB;

/**
 *
 * @author thibault
 */
public class UserServiceImpl extends UserServicePOA {

    private UserServiceInterface userService;

    private ORB orb;

    public UserServiceImpl(UserServiceInterface userService) {
        this.userService = userService;
    }

    public void setORB(ORB orb_val) {
        orb = orb_val;
    }

    @Override
    public boolean authenticate(String username, String password) {
        try {
            return this.userService.authenticateUser(username, password);
        } catch (RemoteException ex) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    @Override
    public String register(String username, String password, String name, String firstname) {
        try {
            this.userService.createUser(new User(username, name, firstname, password));
        } catch (Exception ex) {
            System.out.println("[EXCEPTION] " + ex.getMessage());
            username = "";
        }

        return username;
    }

    @Override
    public void newContactRequest(String username1, String username2) {
        try {
            UserInterface user1 = this.userService.getUser(username1);
            UserInterface user2 = this.userService.getUser(username2);

            if (user1 == null || user2 == null) {
                System.out.println("[SERVER] ContactRequest not sent");
                return;
            }

            ContactRequest request = new ContactRequest(user1, user2, new Date());

            this.userService.sendContactRequest(request);
        } catch (Exception ex) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getContactsDisplay(String username) {
        String res = "";
        try {
            UserInterface user = this.userService.getUser(username);

            ArrayList<UserInterface> contacts = user.getContacts();

            for (UserInterface userObj : contacts) {
                res += userObj.getUsername() + "\n";
            }
        } catch (Exception ex) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }

    @Override
    public String getLastContactRequests(String username) {
        try {
            UserInterface user = this.userService.getUser(username);

            ArrayList<ContactRequestInterface> requests = user.getContactRequest();
            
            System.out.println("[SERVER] Fetched contactsRequest of " + username + " -> " + requests.size());
            
            if (!requests.isEmpty()) {
                ContactRequestInterface lastRequest = requests.get(0);
                return "[REQUEST] " + lastRequest.getDateCreated().toString() + " From " + lastRequest.getSender().getUsername();
            }
                
        } catch (Exception ex) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "";
    }

    @Override
    public void answerToContactRequest(String username, boolean answer) {
        try {
            UserInterface user = this.userService.getUser(username);
            
            this.userService.answerToContactRequest(answer, user.getContactRequest().get(0));
        } catch (Exception ex) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public UserServiceInterface getUserService() {
        return userService;
    }

    public void setUserService(UserServiceInterface userService) {
        this.userService = userService;
    }
}
