package model;

import java.time.Instant;

public class Message {
    private Integer id;
    private Integer creatorId;
    private String subject;
    private String body;
    private long creationTime;
    private Integer messageParentId;

    public Message(Integer creatorId, String subject, String body) {
        //this.id = id;
        this.creatorId = creatorId;
        this.subject = subject;
        this.body = body;
        this.creationTime = Instant.now().toEpochMilli();
        //this.messageParentId = messageParentId;
    }

    public Message(Integer creatorId, String subject, String body, Integer messageParentId) {
        this.creatorId = creatorId;
        this.subject = subject;
        this.body = body;
        this.creationTime = Instant.now().toEpochMilli();
        this.messageParentId = messageParentId;
    }

    public Message(Integer id, Integer creatorId, String subject, String body, long creationTime, Integer messageParentId) {
        this.id = id;
        this.creatorId = creatorId;
        this.subject = subject;
        this.body = body;
        this.creationTime = creationTime;
        this.messageParentId = messageParentId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public Integer getMessageParentId() {
        return messageParentId;
    }

    public void setMessageParentId(Integer messageParentId) {
        this.messageParentId = messageParentId;
    }
}
