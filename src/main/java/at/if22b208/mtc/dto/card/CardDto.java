package at.if22b208.mtc.dto.card;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CardDto {
    @JsonProperty("id")
    private UUID uuid;

    private String name;

    private double damage;
}
