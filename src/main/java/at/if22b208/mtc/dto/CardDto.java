package at.if22b208.mtc.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class CardDto {
    private UUID uuid;
    private String name;
    private int damage;
}
