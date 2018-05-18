package com.coredumpproject.coredump.Command;

import com.coredumpproject.coredump.Message;
import com.coredumpproject.coredump.World;
import com.coredumpproject.coredump.model.User;

import javax.inject.Inject;
import java.io.IOException;

public class ChatCommand extends AbstractCommand {

    @Inject
    World world;

    @Override
    public void execute(User user, String command, String args) {
        for (User u :world.getUsers()){
            if (u.getContainer().getName().equals(user.getContainer().getName())) {// send chat to users in the same room.
                try {
                    u.sendMessage(new Message("chat", user.getAvatar().getName()+">"+command.substring(5)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
