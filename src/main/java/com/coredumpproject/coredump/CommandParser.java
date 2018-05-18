package com.coredumpproject.coredump;

import com.coredumpproject.coredump.Command.AbstractCommand;
import com.coredumpproject.coredump.Command.ChatCommand;
import com.coredumpproject.coredump.Command.MoveCommand;
import com.coredumpproject.coredump.Command.PrintCommand;
import com.coredumpproject.coredump.model.Action;
import com.coredumpproject.coredump.model.Item;
import com.coredumpproject.coredump.model.User;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.HashMap;

@Stateless
public class CommandParser {

    @Inject
    private ChatCommand chatCommand;
    @Inject
    private MoveCommand moveCommand;
    @Inject
    private PrintCommand printCommand;

    private HashMap<String,AbstractCommand> commandMap;
    private String command;
    private User user;


    public CommandParser(){
    }

    @PostConstruct
    public void init(){
        System.out.println(printCommand.toString());
        commandMap = new HashMap<>();
        commandMap.put("chat",chatCommand);
        commandMap.put("move",moveCommand);
        commandMap.put("print",printCommand);
    }

    public void parse(User user, String command){
        this.command = command;
        this.user = user;
        String[] tokens = command.split(" ");
        Item room;

        room = user.getContainer();

        for (Action action : room.getActions()){
            System.out.println("parsing:"+tokens[0]+" against:"+action.getCommand());
            if (action.getCommand().equals(tokens[0])){
                executeScript(action.getScript());
            }
        }

    }

    private void executeScript(String script){
        try {
            commandMap.get(script.split("\\s+")[0]).execute(user, command, script);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Command Not Found in Execute Script");
        }
    }
}
