package model;

public class MessageBox {

    Integer id;

    Integer receiverId;

    Integer messageId;

    Byte isSeen;

    public MessageBox(Integer receiverId, Integer messageId) {
      //  this.id = id;
        this.receiverId = receiverId;
        this.messageId = messageId;
       // this.isSeen = isSeen;
    }

    public MessageBox(Integer id, Integer receiverId, Integer messageId, Byte isSeen) {
        this.id = id;
        this.receiverId = receiverId;
        this.messageId = messageId;
        this.isSeen = isSeen;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public Byte getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(Byte isSeen) {
        this.isSeen = isSeen;
    }
}
