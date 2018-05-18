package com.coredumpproject.coredump.Command;

import com.coredumpproject.coredump.model.User;

import javax.ejb.Stateless;

@Stateless
public abstract class AbstractCommand {

    public abstract void execute(User user, String command, String args);

}
