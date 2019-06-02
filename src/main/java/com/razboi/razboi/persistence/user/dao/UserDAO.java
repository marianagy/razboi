package com.razboi.razboi.persistence.user.dao;

import com.razboi.razboi.persistence.user.AuthDAO;
import com.razboi.razboi.persistence.user.JDBC;
import com.razboi.razboi.persistence.user.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements AuthDAO<User> {

    private static final Logger logger = LogManager.getLogger();
    private JDBC dbUtils;

    public UserDAO() {
        logger.info("Initializing UserDAO with properties");
        dbUtils = new JDBC();
    }

    @Override
    public void create(User entity) {
        logger.traceEntry("saving user {} ", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into users values (?,?,?)")) {
            preStmt.setLong(1, entity.getID());
            preStmt.setString(2, entity.getUsername());
            preStmt.setString(3, entity.getPassword());
            int result = preStmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Integer id) {
        logger.traceEntry("deleting user with {}", id);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("delete from users where ID=?")) {
            preStmt.setLong(1, id);
            int result = preStmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(User entity) {
        Integer id = entity.getID();
        logger.traceEntry("updating user with id {} ", id);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement("update users set username=?, password=? where ID=?")) {
            preStmt.setString(1, entity.getUsername());
            preStmt.setString(2, entity.getPassword());
            preStmt.setLong(4, id);
            int rows = preStmt.executeUpdate();
            if (rows == 0) {
                logger.traceExit("No user found with id {}", id);
            } else {
                logger.traceExit("Updated user with id {}", id);
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
    }

    @Override
    public Integer count() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select count(*) as [SIZE] from users")) {
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    logger.traceExit(result.getInt("SIZE"));
                    return result.getInt("SIZE");
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        return 0;
    }

    @Override
    public List<User> readAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<User> users = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from users")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    User user = getUserFromResult(result);
                    users.add(user);
                }
                return users;
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB " + e);
        }
        logger.traceExit(users);
        return users;
    }

    @Override
    public User readOne(Integer id) {
        logger.traceEntry("finding user with id {} ", id);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement("select * from users where ID=?")) {
            preStmt.setLong(1, id);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    User user = getUserFromResult(result);
                    logger.traceExit(user);
                    return user;
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit("No user found with id {}", id);

        return null;
    }

    @Override
    public User findByUsername(String username) {
        logger.traceEntry("finding user with username {} ", username);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement("select * from users where username=?")) {
            preStmt.setString(1, username);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    User user = getUserFromResult(result);
                    logger.traceExit(user);
                    return user;
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit("No user found with username {}", username);

        return null;
    }

    /**
     * Gets a user object from a result
     *
     * @param result
     * @return
     * @throws SQLException
     */
    private User getUserFromResult(ResultSet result) throws SQLException {
        Integer userId = result.getInt("ID");
        String username = result.getString("username");
        String passw = result.getString("password");
        return new User(userId, username, passw);
    }


}