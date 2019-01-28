package dao;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import mapper.MessageMapper;
import model.Message;


@RegisterMapper(MessageMapper.class)
public interface MessageDao {

    @SqlQuery("select * from message;")
    public List<Message> getMessages();

    @SqlQuery("select * from message where id = :id")
    public Message getMessage(@Bind("id") final int id);

    @SqlUpdate("insert into message (creator_id,subject,body,creation_time) values(:creatorId, :subject , :body , :creationTime )")
    @GetGeneratedKeys
    long createMessage(@BindBean final Message msg);

    @SqlUpdate("insert into message (creator_id,subject,body,creation_time,parent_message_id) values(:creatorId, :subject , :body , :creationTime ,:messageParentId)")
    @GetGeneratedKeys
    long createMessageWithParentId(@BindBean final Message msg);

    @SqlUpdate("delete from message where id = :id")
    int deleteMessage(@Bind("id") final int id);

    @SqlQuery("select last_insert_id();")
    public int lastInsertId();
}
