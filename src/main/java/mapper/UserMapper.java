package mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import model.User;

public class UserMapper implements ResultSetMapper<User> {
    private static final String ID = "id";
    private static final String NAME = "user_name";
    private static final String PASS = "user_password";

    public User map(int i, ResultSet resultSet, StatementContext statementContext)
            throws SQLException {
        return new User(resultSet.getInt(ID), resultSet.getString(NAME), resultSet.getString(PASS));
    }
}