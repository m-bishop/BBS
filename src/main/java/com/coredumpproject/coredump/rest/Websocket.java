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
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by Gregory on 5/18/2017.
 */
@ServerEndpoint("/chat")
public class Websocket {

    private static final Logger LOGGER = Logger.getLogger(Websocket.class.getName());

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

                //get the real userName (or rather the login name)
                session.getBasicRemote().sendText( kp.getKeycloakSecurityContext().getIdToken().getPreferredUsername());
                name = kp.getKeycloakSecurityContext().getIdToken().getPreferredUsername();
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
    public void message(String message, Session client) {
        try {

            LOGGER.log( Level.INFO, "message:" + message + " On Message userID:"+user.getUserID()+" Container:"+user.getContainer().getName());
            commandParser.parse(user,message);

        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
