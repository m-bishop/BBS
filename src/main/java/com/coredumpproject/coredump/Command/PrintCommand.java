package com.coredumpproject.coredump.Command;

import com.coredumpproject.coredump.Message;
import com.coredumpproject.coredump.model.User;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrintCommand extends AbstractCommand {

    private static final Logger LOGGER = Logger.getLogger(PrintCommand.class.getName());

    @Override
    public void execute(User user, String command, String args) {
        try {
            user.sendMessage(new Message("user",args.substring(5)));
        } catch (IOException e){
            LOGGER.log( Level.WARNING, "Message send failed in Print command",e);
        }
    }
}
