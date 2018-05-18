package com.coredumpproject.coredump;

import com.coredumpproject.coredump.EJB.UserPersistantEJB;
import com.coredumpproject.coredump.Exception.NotFoundException;
import com.coredumpproject.coredump.model.Action;
import com.coredumpproject.coredump.model.Item;
import com.coredumpproject.coredump.model.User;
import com.coredumpproject.coredump.rest.ItemEndpoint;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Gregory on 5/26/2017.
 */
@Singleton
@TransactionManagement(value= TransactionManagementType.BEAN)
public class World {

    @Inject
    private UserPersistantEJB userBean;

    @Inject
    private ItemEndpoint itemEndpoint;

    private String name;
    private Set<User> users = Collections.synchronizedSet(new HashSet<>());

    public World(){
        name = "New World";
    }

    public String getName(){
        return this.name;
    }

    @PostConstruct
    public void init() {
        System.out.println("Creating The World.");
    }

    @Schedule(second="*/30", minute="*",hour="*", persistent=false)
    public void doWork(){
        try {
            synchronized (users) {
                for (User u : users) {
                    u.sendMessage(new Message("system", "1/2 minute tick"));
                }
            }
            /*
            if (!this.users.isEmpty()) {
                for (Session session : this.users.iterator().next().getSession().getOpenSessions()) {
                    session.getBasicRemote().sendText("1/2 minute tick");
                }
            }
            */
        } catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
        }
    }

    public Set<User> getUsers(){
        Set<User> rSet = new HashSet<>();
        synchronized (users){
            rSet.addAll(users);
        }
        return rSet;
    }

    public User getUserById(String userID){
        User user = null;
        synchronized (users) {
            for (User u : users) {
                if (u.getUserID().equals(userID)) {
                    user = u;
                    break;
                }
            }
            if (user == null) {
                user = new User();
                user.setUserID(userID);
                try {
                    user = userBean.getUser(user);
                } catch (NotFoundException e) {
                    this.users.add(user);
                }

            }
        }
        return user;
    }

    public  User addUser(User user){
        System.out.println("In addUser");

        User entity;
        synchronized(users) {
            try {
                entity = userBean.getUser(user);
                entity.setSession(user.getSession());
                if (entity.getAvatar() == null){
                    entity.setAvatar(user.getAvatar());
                    System.out.println("Created new Avatar for a loaded user."); // log as warning
                }
                this.users.add(entity);
            } catch (NotFoundException nfe) {
                this.users.add(user);
                entity = user;
            }
        }
        initNewUser(entity);
        return entity;
      }

    public void removeUser(User user){
        System.out.println("In removeUser");

        userBean.saveUser(user);
        synchronized (users) {
            this.users.remove(user);
        }
    }

    private void initNewUser(User user){
        try {
            user.getSession().getBasicRemote().sendText("Loaded User ...");
            Item lobby = itemEndpoint.getById(1L); // First room must be the first thing created!
            for (Action action : lobby.getActions()){ // TODO debug line, remove when done.
                System.out.println("lobby action:"+action.getCommand());
            }
            user.setContainer(lobby);
            user.sendMessage(new Message("user","User has entered:"+user.getContainer().getName()));
        } catch (Exception e) {
            System.out.println("Error communicating to newly loaded user in addUser.");
        }
    }

}
