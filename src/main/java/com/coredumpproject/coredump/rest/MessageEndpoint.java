package com.coredumpproject.coredump.rest;

import com.coredumpproject.coredump.Message;
import com.coredumpproject.coredump.World;
import com.coredumpproject.coredump.model.User;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * 
 */
@Stateless
@Path("/messages")
public class MessageEndpoint {

	@Inject
	private World world;

	@Inject
	private HttpServletRequest session;


	@POST
	@Consumes("application/json")
	public Response create(Message entity) {
		//parse command and return applicable messages.
		return Response.ok().build();
	}





	@GET
	@Produces("application/json")
	public List<Message> listAll(){

		String UserId = session.getUserPrincipal().getName();

		User user = world.getUserById(UserId);

		final List<Message> results = user.readMessages();
		return results;
	}


}
