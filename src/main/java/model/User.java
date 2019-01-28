package model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;



public class User {

    private Integer id;

    @NotBlank @Length(min=2, max=45)
    private String userName;

    @NotBlank @Length(min=2, max=45)
    private String userPassword;

    public User(){}

    public User(Integer id , String userName , String userPassword){
        this.id = id;
        this.userName = userName;
        this.userPassword = userPassword;
    }

    public Integer getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

}
