package com.coredumpproject.coredump.EJB;


import com.coredumpproject.coredump.Exception.NotFoundException;
import com.coredumpproject.coredump.model.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;



@Stateless
public class UserPersistantEJB {

    @PersistenceContext(unitName = "forgetest-persistence-unit")
    private EntityManager em;

    public  User getUser(User user) throws NotFoundException {

        TypedQuery<User> findByIdQuery = em
                .createQuery(
                        "SELECT DISTINCT u FROM User u WHERE u.userID = :userId ORDER BY u.id",
                        User.class);
        findByIdQuery.setParameter("userId", user.getUserID());
        User entity;
        try {
            entity = findByIdQuery.getSingleResult();
        }  catch (NoResultException nre) {
            throw new NotFoundException("User not Found",nre);
        }

        return entity;
    }

    public void saveUser(User user){

        User entity;
        try {
            entity = this.getUser(user);
        } catch (NotFoundException nfe) {
            entity = null;
        }
        if (entity == null) {
                em.persist(user);
        } else {

                em.merge(user);
        }
    }
}
