/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chichatserver;

import entity.Chat;
import entity.ContactRequest;
import entity.Message;
import entity.User;
import interfaces.ContactRequestInterface;
import interfaces.MessageInterface;
import interfaces.UserInterface;
import interfaces.UserServiceInterface;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import ServerApp.UserServiceImpl;
import ServerApp.UserService;
import ServerApp.UserServiceHelper;
import ServerApp.ChatServiceImpl;
import ServerApp.ChatService;
import ServerApp.ChatServiceHelper;
import java.util.Properties;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

/**
 *
 * @author thibault
 */
public class ChiChatServer {

    /**
     * @param args the command line arguments
     * @throws java.rmi.RemoteException
     */
    public static void main(String[] args) throws RemoteException, Exception {
        //Server host
        String host = "127.0.0.1";
        //BD Server port
        int bdPort = 3000;

        //BD Registry
        Registry bdRegistry = LocateRegistry.getRegistry(host, bdPort);

        UserServiceInterface userServiceInterface = null;

        try {
            userServiceInterface = (UserServiceInterface) bdRegistry.lookup("userService");
            
            ArrayList<UserInterface> users = new ArrayList<>();
            users.add(new User("konec", "konec", "konec", "root"));
            users.add(new User("konecny", "konec", "konec", "root"));
            users.add(new User("root", "root", "root", "root"));            
            userServiceInterface.initUserRepository(users);
            
            System.out.println("[SERVER] UserService registered...");
        } catch (NotBoundException | AccessException ex) {
            Logger.getLogger(ChiChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (userServiceInterface == null) {
            System.out.println("Bd unreachiable");
            System.exit(0);
        }

        try {
            // Paramétrage pour la création de la couche ORB :
            // localisation de l'annuaire d'objet (service nommage)
            Properties props = new Properties();
            props.put("org.omg.CORBA.ORBInitialPort", "2000");
            props.put("org.omg.CORBA.ORBInitialHost", "127.0.0.1");
            
            ORB orb = ORB.init((String []) null, props);
           
            // Recherche d'une reference au service "RootPOA"
            // (Portable Object Adaptator)
            org.omg.CORBA.Object poaRef = orb.resolve_initial_references("RootPOA");
            // Instance du service RootPOA ("cast" sauce CORBA)
            POA rootpoa = POAHelper.narrow(poaRef);
            // Activation du service RootPOA
            rootpoa.the_POAManager().activate();

            org.omg.CORBA.Object serviceNommageReference;
            serviceNommageReference = orb.resolve_initial_references("NameService");
            
            NamingContextExt serviceNommage = NamingContextExtHelper.narrow(serviceNommageReference);
            
            // Création du service et de son enveloppe (par héritage)
            UserServiceImpl userServiceImpl = new UserServiceImpl(userServiceInterface);
            // Creation de la référence CORBA du service auprès du POA
            org.omg.CORBA.Object userServiceRef;
            userServiceRef = rootpoa.servant_to_reference(userServiceImpl);
            // Création de l'objet CORBA du service
            UserService userService = UserServiceHelper.narrow(userServiceRef);
            
            // Création du service et de son enveloppe (par héritage)
            ChatServiceImpl chatServiceImpl = new ChatServiceImpl(userServiceInterface);
            // Creation de la référence CORBA du service auprès du POA
            org.omg.CORBA.Object chatServiceRef;
            chatServiceRef = rootpoa.servant_to_reference(chatServiceImpl);
            // Création de l'objet CORBA du service
            ChatService chatService = ChatServiceHelper.narrow(chatServiceRef);
            
            // Enregistrement du service (nom, objet CORBA)
            NameComponent[] pathChat = serviceNommage.to_name("ChatService");
            serviceNommage.rebind(pathChat, chatService);
            NameComponent[] pathUser = serviceNommage.to_name("UserService");
            serviceNommage.rebind(pathUser, userService);
            // Démarrage de la couche ORB
            orb.run();
            
            System.out.println("UserService and ChatService Server ready and waiting ...");

        } catch (InvalidName | CannotProceed | org.omg.CosNaming.NamingContextPackage.InvalidName | NotFound | AdapterInactive | ServantNotActive | WrongPolicy e) {
            System.err.println("ERROR: " + e);
            e.printStackTrace(System.out);
        }

        System.out.println("Server Exiting ...");

    }

}
