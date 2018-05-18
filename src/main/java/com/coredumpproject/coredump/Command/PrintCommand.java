package com.coredumpproject.coredump.Command;

import com.coredumpproject.coredump.Message;
import com.coredumpproject.coredump.model.User;

import java.io.IOException;

public class PrintCommand extends AbstractCommand {

    @Override
    public void execute(User user, String command, String args) {
        try {
            System.out.println("Executing print.");
            user.sendMessage(new Message("user",args.substring(5)));
        } catch (IOException e){
            System.out.println("Message send failed in Print command.");
            e.printStackTrace();
        }
    }
}
