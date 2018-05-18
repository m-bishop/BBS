package com.coredumpproject.coredump.rest;

import com.coredumpproject.coredump.CommandParser;
import com.coredumpproject.coredump.World;
import com.coredumpproject.coredump.model.Item;
import com.coredumpproject.coredump.model.User;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;

import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;


/**
 * Created by Gregory on 5/18/2017.
 */
@ServerEndpoint("/chat")
public class Websocket {

    @Inject
    private World world;
    @Inject
    private CommandParser commandParser;

    private User user;

    @OnOpen
    public void open(Session session) {
        try {
            String name = "";
            String UserId = session.getUserPrincipal().getName();
            session.getBasicRemote().sendText(UserId);
            if (session.getUserPrincipal() instanceof KeycloakPrincipal) {
                KeycloakPrincipal<KeycloakSecurityContext> kp = (KeycloakPrincipal<KeycloakSecurityContext>)  session.getUserPrincipal();

                // this is how to get the real userName (or rather the login name)
                session.getBasicRemote().sendText( kp.getKeycloakSecurityContext().getIdToken().getPreferredUsername());
                name = kp.getKeycloakSecurityContext().getIdToken().getPreferredUsername();
                /*
                Client client = ClientBuilder.newClient();
                String response = client.target("http://127.0.0.1:8080/forgetest/rest/items")
                        .request(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + kp.getKeycloakSecurityContext().getTokenString())
                        .get(String.class);

                session.getBasicRemote().sendText(response);
                */
                User user = new User();
                user.setSession(session);
                user.setUserID(UserId);
                Item avatar = new Item();
                avatar.setName(name);
                user.setAvatar(avatar);
                this.user = world.addUser(user);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @OnClose
    public void close(Session session) {
        world.removeUser(user);
    }

    @OnError
    public void onError(Throwable error) {
        world.removeUser(user);
    }

    @OnMessage
    public void message(String message, Session client) throws IOException, EncodeException {
        try {
            System.out.println("message: " + message);

            System.out.println("On Message userID:"+user.getUserID()+" Container:"+user.getContainer().getName());


            commandParser.parse(user,message);

            /*
            for (User u : world.getUsers()){
                u.sendMessage(new Message("chat",message));
            }
            */

            /*
            String[] command = message.split(" ");

            if (command[1].equalsIgnoreCase("claim")) {
                world.claim(command[0]);
                System.out.println(command[0] + " claimed");
                for (Session peer : client.getOpenSessions()) {
                    peer.getBasicRemote().sendText(world.getName());
                }
            }else if (command[1].equalsIgnoreCase("getname")) {
                System.out.println(command[0] + " asked who owns this world!");
                for (Session peer : client.getOpenSessions()) {
                    peer.getBasicRemote().sendText(world.getName());
                }
            }
            for (Session peer : client.getOpenSessions()) {
                peer.getBasicRemote().sendText(message);
            }
            */

        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
