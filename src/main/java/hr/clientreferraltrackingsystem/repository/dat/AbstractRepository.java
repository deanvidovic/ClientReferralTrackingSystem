package hr.clientreferraltrackingsystem.repository.dat;

import hr.clientreferraltrackingsystem.model.Entity;

import java.util.List;

public abstract class AbstractRepository<T extends Entity> {
    public abstract List<T> findAllById(Integer id);
    public abstract List<T> findAll();
    public abstract void save(T entity);
    public abstract void update(T entity);
    public abstract void delete(T entity);
}
