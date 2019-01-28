package representation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchRepresentation {
    private String inField;
    private String query;

    @JsonCreator
    public SearchRepresentation(@JsonProperty("inField") String inField,@JsonProperty("query")String query) {
        this.inField = inField;
        this.query = query;
    }
    @JsonProperty
    public String getInField() {
        return inField;
    }
    @JsonProperty
    public void setInField(String inField) {
        this.inField = inField;
    }
    @JsonProperty
    public String getQuery() {
        return query;
    }
    @JsonProperty
    public void setQuery(String query) {
        this.query = query;
    }
}
