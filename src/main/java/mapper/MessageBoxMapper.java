package mapper;

import model.MessageBox;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageBoxMapper implements ResultSetMapper<MessageBox> {
    private static final String ID = "id";
    private static final String RECEIVER_ID = "receiver_id";
    private static final String MESSAGE_ID = "message_id";
    private static final String IS_READ = "is_read";


    public MessageBox map(int i, ResultSet resultSet, StatementContext statementContext)
            throws SQLException {
        return new MessageBox(resultSet.getInt(ID),resultSet.getInt(RECEIVER_ID), resultSet.getInt(MESSAGE_ID),
                resultSet.getByte(IS_READ));
    }
}