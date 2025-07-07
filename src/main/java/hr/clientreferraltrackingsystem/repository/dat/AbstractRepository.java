package hr.clientreferraltrackingsystem.repository.dat;

import hr.clientreferraltrackingsystem.model.Entity;

import java.util.List;

/**
 * Abstract base class for repositories handling entities.
 *
 * @param <T> the type of entity managed by the repository
 */
public abstract class AbstractRepository<T extends Entity> {

    /**
     * Finds all entities by a given identifier.
     *
     * @param id the identifier to filter entities
     * @return a list of entities matching the id
     */
    public abstract List<T> findAllById(Integer id);

    /**
     * Finds all entities in the repository.
     *
     * @return a list of all entities
     */
    public abstract List<T> findAll();

    /**
     * Saves a new entity in the repository.
     *
     * @param entity the entity to save
     */
    public abstract void save(T entity);

    /**
     * Updates an existing entity in the repository.
     *
     * @param entity the entity to update
     */
    public abstract void update(T entity);

    /**
     * Deletes an entity from the repository.
     *
     * @param entity the entity to delete
     */
    public abstract void delete(T entity);
}
