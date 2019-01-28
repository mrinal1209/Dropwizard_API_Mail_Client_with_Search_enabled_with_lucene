package dao;

import mapper.MessageBoxMapper;
import model.MessageBox;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;


@RegisterMapper(MessageBoxMapper.class)
public interface MessageBoxDao {

    @SqlQuery("select * from message_box;")
    public List<MessageBox> getMessageBoxs();

    @SqlQuery("select message_id from message_box where receiver_id = :id;")
    public List<Integer> getMessageIdFromRecevierId(@Bind("id") final int id);


    @SqlQuery("select * from message_box where id = :id;")
    public MessageBox getMessageBox(@Bind("id") final int id);

    @SqlUpdate("insert into message_box (receiver_id,message_id) values(:receiverId,:messageId)")
    @GetGeneratedKeys
    long createMessageBox(@BindBean final MessageBox msgBox);

    @SqlUpdate("update message_box set is_read = 1 where id = :id;")
    void hasRead(@Bind("id") final int id);

    @SqlUpdate("delete from message_box where id = :id")
    int deleteMessageBox(@Bind("id") final int id);

    @SqlQuery("select last_insert_id();")
    public int lastInsertId();
}
