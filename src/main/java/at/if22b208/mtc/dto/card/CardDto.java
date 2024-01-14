package at.if22b208.mtc.dto.card;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record CardDto(@JsonProperty("id") UUID uuid, String name, int damage) {
}
