package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;
import org.skife.jdbi.v2.exceptions.UnableToObtainConnectionException;
import org.skife.jdbi.v2.sqlobject.CreateSqlObject;

import dao.UserDao;
import model.User;

public abstract class UserService {
    private static final String USER_NOT_FOUND = "User id %s not found.";
    private static final String DATABASE_REACH_ERROR =
            "Could not reach the MySQL database. The database may be down or there may be network connectivity issues. Details: ";
    private static final String DATABASE_CONNECTION_ERROR =
            "Could not create a connection to the MySQL database. The database configurations are likely incorrect. Details: ";
    private static final String DATABASE_UNEXPECTED_ERROR =
            "Unexpected error occurred while attempting to reach the database. Details: ";
    private static final String SUCCESS = "Success...";
    private static final String UNEXPECTED_ERROR = "An unexpected error occurred while deleting part.";

    @CreateSqlObject
    abstract UserDao userDao();

    public List<User> getUsers() {
        return userDao().getUsers();
    }

    public User getUser(int id) {
        User user = userDao().getUser(id);
        if (Objects.isNull(user)) {
            throw new WebApplicationException(String.format(USER_NOT_FOUND, id), Status.NOT_FOUND);
        }
        return user;
    }

    public List<Integer> getIdFromEmail(List<String> emails){
        List<Integer> ids = new ArrayList<>();
        for(String email : emails)
            ids.add(userDao().getIdFromEmail(email));
        return ids;
    }
    public long getIdFromEmail(String email){
       return userDao().getIdFromEmail(email);
    }
    public String getEmailFromId(int id){
        return userDao().getEmailFromId(id);
    }

    public User createUser(User user) {
        return userDao().getUser((int)userDao().createUser(user));
    }

    public User editUser(User user) {
        if (Objects.isNull(userDao().getUser(user.getId()))) {
            throw new WebApplicationException(String.format(USER_NOT_FOUND, user.getId()),
                    Status.NOT_FOUND);
        }
        userDao().editUser(user);
        return userDao().getUser(user.getId());
    }

    public String deleteUser(final int id) {
        int result = userDao().deleteUser(id);
        switch (result) {
            case 1:
                return SUCCESS;
            case 0:
                throw new WebApplicationException(String.format(USER_NOT_FOUND, id), Status.NOT_FOUND);
            default:
                throw new WebApplicationException(UNEXPECTED_ERROR, Status.INTERNAL_SERVER_ERROR);
        }
    }

    public String performHealthCheck() {
        try {
            userDao().getUsers();
        } catch (UnableToObtainConnectionException ex) {
            return checkUnableToObtainConnectionException(ex);
        } catch (UnableToExecuteStatementException ex) {
            return checkUnableToExecuteStatementException(ex);
        } catch (Exception ex) {
            return DATABASE_UNEXPECTED_ERROR + ex.getCause().getLocalizedMessage();
        }
        return null;
    }

    private String checkUnableToObtainConnectionException(UnableToObtainConnectionException ex) {
        if (ex.getCause() instanceof java.sql.SQLNonTransientConnectionException) {
            return DATABASE_REACH_ERROR + ex.getCause().getLocalizedMessage();
        } else if (ex.getCause() instanceof java.sql.SQLException) {
            return DATABASE_CONNECTION_ERROR + ex.getCause().getLocalizedMessage();
        } else {
            return DATABASE_UNEXPECTED_ERROR + ex.getCause().getLocalizedMessage();
        }
    }

    private String checkUnableToExecuteStatementException(UnableToExecuteStatementException ex) {
        if (ex.getCause() instanceof java.sql.SQLSyntaxErrorException) {
            return DATABASE_CONNECTION_ERROR + ex.getCause().getLocalizedMessage();
        } else {
            return DATABASE_UNEXPECTED_ERROR + ex.getCause().getLocalizedMessage();
        }
    }
}