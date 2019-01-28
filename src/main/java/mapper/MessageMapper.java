package mapper;

import model.Message;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageMapper implements ResultSetMapper<Message> {
    private static final String ID = "id";
    private static final String SUBJECT = "subject";
    private static final String BODY = "body";
    private static final String CREATER_ID = "creator_id";
    private static final String PARENT_ID = "parent_message_id";
    private static final String CREATION_TIME = "creation_time";

    public Message map(int i, ResultSet resultSet, StatementContext statementContext)
            throws SQLException {
        return new Message(resultSet.getInt(ID),resultSet.getInt(CREATER_ID), resultSet.getString(SUBJECT),
                resultSet.getString(BODY) , resultSet.getLong(CREATION_TIME) , resultSet.getInt(PARENT_ID));
    }
}