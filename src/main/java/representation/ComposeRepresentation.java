package representation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.jackson.JsonSnakeCase;

import java.util.List;

@JsonSnakeCase
public class ComposeRepresentation {
    private Integer messageId;
    private String creatorId;
    private List<String> recevierId;
    private String subject;
    private String body;

    @JsonCreator
    public ComposeRepresentation(@JsonProperty("from") String creatorId, @JsonProperty("to") List<String> recevierId, @JsonProperty("subject") String subject, @JsonProperty("body") String body , @JsonProperty("message_id") Integer messageId) {
        this.creatorId = creatorId;
        this.recevierId = recevierId;
        this.subject = subject == null ? "" : subject;
        this.body = body == null ? "" : body;
        this.messageId = messageId == null ? 0 : messageId;
    }


    @JsonProperty
    public Integer getMessageId() {
        return this.messageId;
    }
    @JsonProperty
    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }
    @JsonProperty
    public String getCreatorId() {
        return creatorId;
    }
    @JsonProperty
    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }
    @JsonProperty
    public List<String> getRecevierId() {
        return recevierId;
    }
    @JsonProperty
    public void setRecevierId(List<String> recevierId) {
        this.recevierId = recevierId;
    }
    @JsonProperty
    public String getSubject() {
        return subject;
    }
    @JsonProperty
    public void setSubject(String subject) {
        this.subject = subject;
    }
    @JsonProperty
    public String getBody() {
        return body;
    }
    @JsonProperty
    public void setBody(String body) {
        this.body = body;
    }
}
