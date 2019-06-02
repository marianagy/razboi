package com.razboi.razboi.business.service.user;

import com.razboi.razboi.persistence.user.DAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public abstract class CRUDService<T> {

    private static final Logger logger = LogManager.getLogger();
    DAO<T> dao;

    public CRUDService(DAO dao) {
        this.dao = dao;
    }

    public CRUDService() {

    }

    public DAO<T> getDao() {
        return dao;
    }

    public void setDao(DAO<T> dao) {
        this.dao = dao;
    }

    public void create(T entity) {
        this.dao.create(entity);
    }

    public T readOne(Integer id) {
        return this.dao.readOne(id);
    }

    public List<T> readAll() {
        return this.dao.readAll();
    }

    ;

    public void update() {
    }

    public void delete(Integer id) {
        this.dao.delete(id);
    }

    ;

}
