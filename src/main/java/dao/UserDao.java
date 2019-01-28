package dao;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import mapper.UserMapper;
import model.User;

@RegisterMapper(UserMapper.class)
public interface UserDao {

    @SqlQuery("select * from user;")
    public List<User> getUsers();

    @SqlQuery("select * from user where id = :id")
    public User getUser(@Bind("id") final int id);

    @SqlQuery("select id from user where user_name = :userName")
    public int getIdFromEmail(@Bind("userName") final String email);

    @SqlUpdate("insert into user (user_name, user_password) values(:userName, :userPassword)")
    @GetGeneratedKeys
    long createUser(@BindBean final User user);

    @SqlUpdate("update user set name = coalesce(:userName, user_name), code = coalesce(:userPassword, user_password) where id = :id")
    void editUser(@BindBean final User user);

    @SqlUpdate("delete from user where id = :id")
    int deleteUser(@Bind("id") final int id);

    @SqlQuery("select last_insert_id();")
    public int lastInsertId();

    @SqlQuery("select user_name from user where id = :id")
    public String getEmailFromId(@Bind("id") final int id);


}