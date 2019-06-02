package com.razboi.razboi.persistence.user;

public interface AuthDAO<T> extends DAO<T> {

    /**
     * Returns the entity with the username given. Only works if entity has a username and needs to authenticate.
     * @param usernames
     * @return
     */
    T findByUsername(String usernames);

}
