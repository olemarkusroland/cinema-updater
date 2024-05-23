package cinema.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;

@Embeddable
public class Rating {
    @JsonProperty("Source")
    public String source;

    @JsonProperty("Value")
    public String value;
}
