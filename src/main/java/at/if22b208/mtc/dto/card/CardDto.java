package at.if22b208.mtc.dto.card;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class CardDto {
    @JsonProperty("id")
    private UUID uuid;
    private String name;
    private double damage;
}
