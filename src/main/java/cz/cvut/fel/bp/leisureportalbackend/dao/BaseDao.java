package cz.cvut.fel.bp.leisureportalbackend.dao;


import cz.cvut.fel.bp.leisureportalbackend.exception.PersistenceException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Base Data Access Object (DAO) providing common operations for managing entities.
 *
 * @param <T> The type of the entity.
 */
public abstract class BaseDao<T> implements GenericDao<T> {

    @PersistenceContext
    protected EntityManager em;

    protected final Class<T> type;

    /**
     * constructor
     */
    protected BaseDao(Class<T> type) {
        this.type = type;
    }

    /**
     * Finds an entity by its ID.
     *
     * @param id The ID of the entity.
     * @return The entity with the specified ID, or null if not found.
     */
    @Override
    public T find(Integer id) {
        Objects.requireNonNull(id);
        return em.find(type, id);
    }

    /**
     * Finds all entities of the specified type.
     *
     * @return A list of all entities of the specified type.
     * @throws PersistenceException if an error occurs during the query execution.
     */
    @Override
    public List<T> findAll() {
        try {
            return em.createQuery("SELECT e FROM " + type.getSimpleName() + " e", type).getResultList();
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    /**
     * Persists an entity.
     *
     * @param entity The entity to persist.
     * @throws PersistenceException if an error occurs during the persistence operation.
     */
    @Override
    public void persist(T entity) {
        Objects.requireNonNull(entity);
        try {
            em.persist(entity);
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    /**
     * Persists a collection of entities.
     *
     * @param entities The collection of entities to persist.
     * @throws PersistenceException if an error occurs during the persistence operation.
     */
    @Override
    public void persist(Collection<T> entities) {
        Objects.requireNonNull(entities);
        if (entities.isEmpty()) {
            return;
        }
        try {
            entities.forEach(this::persist);
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    /**
     * Updates an entity.
     *
     * @param entity The entity to update.
     * @return The updated entity.
     * @throws PersistenceException if an error occurs during the update operation.
     */
    @Override
    public T update(T entity) {
        Objects.requireNonNull(entity);
        try {
            return em.merge(entity);
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    /**
     * Removes an entity.
     *
     * @param entity The entity to remove.
     * @throws PersistenceException if an error occurs during the removal operation.
     */
    @Override
    public void remove(T entity) {
        Objects.requireNonNull(entity);
        try {
            final T toRemove = em.merge(entity);
            if (toRemove != null) {
                em.remove(toRemove);
            }
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    /**
     * Checks if an entity with the specified ID exists.
     *
     * @param id The ID to check.
     * @return true if an entity with the specified ID exists, false otherwise.
     */
    @Override
    public boolean exists(Integer id) {
        return id != null && em.find(type, id) != null;
    }

}
