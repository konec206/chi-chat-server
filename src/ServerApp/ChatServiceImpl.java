/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerApp;

import entity.Chat;
import entity.Message;
import entity.User;
import interfaces.MessageInterface;
import interfaces.UserInterface;
import interfaces.UserServiceInterface;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.omg.CORBA.ORB;

/**
 *
 * @author thibault
 */
public class ChatServiceImpl extends ChatServicePOA {

    private ArrayList<Chat> chats;
    private UserServiceInterface userServiceInterface;
    private HashMap<Chat, ArrayList<Listener>> chatsListeners;

    private ORB orb;

    public ChatServiceImpl(UserServiceInterface userServiceInterface) {
        this.chats = new ArrayList<>();
        this.userServiceInterface = userServiceInterface;
        this.chatsListeners = new HashMap<>();
    }

    public void setORB(ORB orb_val) {
        orb = orb_val;
    }

    @Override
    public boolean sendMessage(String chatId, String usernameSender, String message) {
        Message messageObj = new Message(new User(usernameSender, "", "", ""), new Date(), message);

        Chat chat = null;
        for (Chat obj : this.chats) {
            if (obj.getId().equals(chatId)) {
                chat = obj;
            }
        }

        if (chat == null) {
            try {
                throw new Exception("Chat not found");
            } catch (Exception ex) {
                Logger.getLogger(ChatServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {
            chat.addMessage(messageObj);

            for (Listener listener : this.chatsListeners.get(chat)) {
                listener.onMessageReceived(messageObj.toString());
            }

            System.out.println("[SERVER] New Message : " + messageObj.toString() + " \n\tTO chat number : " + chatId);

        } catch (RemoteException ex) {
            Logger.getLogger(ChatServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ChatServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    @Override
    public String newChat(String username1, String username2, boolean isPublic, Listener listener) {
        Chat chat = null;

        Chat publicChat = null;
        Chat privateChat = null;

        try {
            UserInterface user1 = this.userServiceInterface.getUser(username1);

            boolean isContact = false;

            for (UserInterface contact : user1.getContacts()) {
                if (contact.getUsername().equals(username2)) {
                    isContact = true;
                }
            }

            if (!isContact && !isPublic) {
                System.out.println("[SERVER] User " + username2 + " not found in the contacts of " + username1 + " the chat wasn't created");
                return "UserNotFound";
            }

        } catch (RemoteException ex) {
            Logger.getLogger(ChatServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            if (!username2.equals("")) {
                listener.onMessageReceived("The user cannot be found");
            }
        }

        try {
            for (Chat chatObj : this.chats) {
                if (!isPublic && chatObj.getId().equals(username1 + "_TO_" + username2) || chatObj.getId().equals(username2 + "_TO_" + username1)) {
                    privateChat = chatObj;
                } else if (isPublic && chatObj.getId().equals(username2)) {
                    publicChat = chatObj;
                }
            }

            if (!isPublic && privateChat != null) {
                chat = privateChat;
            } else {
                chat = publicChat;
            }

            ArrayList<Listener> listeners = null;
            if (chat == null) {
                chat = new Chat(this.userServiceInterface.getUser(username1), isPublic, username2);

                if (!username2.equals("")) {
                    chat.addUser(this.userServiceInterface.getUser(username2));
                }

                this.chats.add(chat);

                listeners = new ArrayList<>();
                listeners.add(listener);
                System.out.println("[SERVER] Created a new chat : " + chat.getId());
            } else {
                listeners = this.chatsListeners.get(chat);
                if (!listeners.contains(listener)) {
                    listeners.add(listener);
                }

                for (MessageInterface message : chat.getMessages()) {
                    listener.onMessageReceived("[MESSAGE RECEIVED]" + message.toString());
                }

                System.out.println("[SERVER] Added a user to the chat : " + chat.getId());
            }

            this.chatsListeners.put(chat, listeners);

        } catch (RemoteException ex) {
            Logger.getLogger(ChatServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            System.out.println("[SERVER] User not found");
            return "";
        }

        return chat != null ? chat.getId() : "";
    }

    @Override
    public String disconnect(String username1, String chatId, Listener listener) {
        if (!chatId.equals(username1)) {
            Chat chat = null;
            for (Chat chatObj : this.chats) {
                if (chatObj.getId().equals(chatId)) {
                    chat = chatObj;
                }
            }

            if (chat == null) {
                return "";
            }

            ArrayList<Listener> listeners = this.chatsListeners.get(chat);

            if (listeners.contains(listener)) {
                listeners.remove(listener);
            }

            try {
                chat.removeUser(this.userServiceInterface.getUser(username1));
            } catch (RemoteException ex) {
                Logger.getLogger(ChatServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(ChatServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

            for (Listener listenerObj : listeners) {
                listenerObj.onUserDisconnected(username1 + " just disconnected!");
            }
        } else {
            listener.onMessageReceived("You are no longer writting on your public chat");
        }

        return chatId;
    }
}
