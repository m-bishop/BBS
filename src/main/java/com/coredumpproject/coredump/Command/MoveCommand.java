package com.coredumpproject.coredump.Command;

import com.coredumpproject.coredump.CommandParser;
import com.coredumpproject.coredump.Exception.NotFoundException;
import com.coredumpproject.coredump.model.Item;
import com.coredumpproject.coredump.model.User;
import com.coredumpproject.coredump.rest.ItemEndpoint;

import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MoveCommand extends AbstractCommand {

    private static final Logger LOGGER = Logger.getLogger(MoveCommand.class.getName());

    @Inject
    private ItemEndpoint itemEndpoint;

    @Inject
    private CommandParser commandParser;

    @Override
    public void execute(User user, String command, String args) {
        try {
            String destinationName = args.split(" ")[1];
            Item destination = itemEndpoint.getByName(destinationName);
            user.setContainer(destination);
            commandParser.parse(user,".entryrc");
        } catch (NotFoundException e) {
            LOGGER.log( Level.WARNING, "Destination room in move command not found!");
        }

    }
}
