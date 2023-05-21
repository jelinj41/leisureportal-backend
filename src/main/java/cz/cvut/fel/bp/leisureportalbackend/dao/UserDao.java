package cz.cvut.fel.bp.leisureportalbackend.dao;

import cz.cvut.fel.bp.leisureportalbackend.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.Objects;


/**
 * Dao for User class
 */
@Repository
public class UserDao extends BaseDao<User> {
    /**
     * constructor
     */
    public UserDao(){super(User.class);}

    /**
     * Finds a user by ID.
     *
     * @param id The ID of the user.
     * @return The user with the specified ID.
     * @throws NullPointerException if the ID is null.
     */
    public User find(Integer id) {
        Objects.requireNonNull(id);
        return em.find(User.class, id);
    }

    /**
     * Finds a user by email.
     *
     * @param email The email of the user.
     * @return The user with the specified email, or null if not found.
     */
    public User findByEmail(String email) {
        try {
            return em.createNamedQuery("User.findByEmail", User.class).setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}