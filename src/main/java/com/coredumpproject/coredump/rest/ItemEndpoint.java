package com.coredumpproject.coredump.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import com.coredumpproject.coredump.Exception.NotFoundException;
import com.coredumpproject.coredump.model.Item;

/**
 *
 */
@Stateless
@Path("/items")
public class ItemEndpoint {
	@PersistenceContext(unitName = "forgetest-persistence-unit")
	private EntityManager em;

	@POST
	@Consumes("application/json")
	public Response create(Item entity) {
		em.persist(entity);

		return Response.created(
				UriBuilder.fromResource(ItemEndpoint.class)
						.path(String.valueOf(entity.getId())).build()).build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id) {
		Item entity = em.find(Item.class, id);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		em.remove(entity);
		return Response.noContent().build();
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("application/json")
	public Response findById(@PathParam("id") Long id) {
		Item entity;
		try{
			entity = getById(id);
		} catch (NotFoundException nfe) {
			entity = null;
		}
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(entity).build();
	}

	public Item getById(Long id) throws NotFoundException{
		em.getEntityManagerFactory().getCache().evictAll();
		TypedQuery<Item> findByIdQuery = em
				.createQuery(
						"SELECT DISTINCT i FROM Item i LEFT JOIN FETCH i.actions LEFT JOIN FETCH i.items WHERE i.id = :entityId ORDER BY i.id",
						Item.class);
		findByIdQuery.setParameter("entityId", id);
		Item entity;
		try {
			entity = findByIdQuery.getSingleResult();
		} catch (NoResultException nre) {
			throw new NotFoundException("Item not Found",nre);
		}
		return entity;
	}

	public Item getByName(String name) throws NotFoundException{
		TypedQuery<Item> findByIdQuery = em
				.createQuery(
						"SELECT DISTINCT i FROM Item i LEFT JOIN FETCH i.actions LEFT JOIN FETCH i.items WHERE i.name = :entityName ORDER BY i.id",
						Item.class);
		findByIdQuery.setParameter("entityName", name);
		Item entity;
		try {
			entity = findByIdQuery.getSingleResult();
		} catch (NoResultException nre) {
			throw new NotFoundException("Item not Found",nre);
		}
		return entity;
	}

	@GET
	@Produces("application/json")
	public List<Item> listAll(@QueryParam("start") Integer startPosition,
							  @QueryParam("max") Integer maxResult) {
		TypedQuery<Item> findAllQuery = em
				.createQuery(
						"SELECT DISTINCT i FROM Item i LEFT JOIN FETCH i.actions LEFT JOIN FETCH i.items ORDER BY i.id",
						Item.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		final List<Item> results = findAllQuery.getResultList();
		return results;
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes("application/json")
	public Response update(@PathParam("id") Long id, Item entity) {
		if (entity == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		if (id == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		if (!id.equals(entity.getId())) {
			return Response.status(Status.CONFLICT).entity(entity).build();
		}
		if (em.find(Item.class, id) == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		try {
			entity = em.merge(entity);
		} catch (OptimisticLockException e) {
			return Response.status(Response.Status.CONFLICT)
					.entity(e.getEntity()).build();
		}

		return Response.noContent().build();
	}
}