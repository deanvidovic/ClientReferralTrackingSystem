package hr.clientreferraltrackingsystem.model;

/**
 * Abstract base class representing an entity with an identifier.
 */
public abstract class Entity {
    private Integer id;

    /**
     * Constructs an entity with the specified ID.
     *
     * @param id the identifier of the entity
     */
    protected Entity(Integer id) {
        this.id = id;
    }

    /**
     * Returns the ID of the entity.
     *
     * @return the entity ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the ID of the entity.
     *
     * @param id the ID to set
     */
    public void setId(Integer id) {
        this.id = id;
    }
}
