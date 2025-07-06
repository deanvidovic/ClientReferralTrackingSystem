package hr.clientreferraltrackingsystem.model;

public abstract class Entity {
    private Integer id;

    protected Entity(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
