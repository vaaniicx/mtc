package at.if22b208.mtc.entity;

import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

@Builder
@Data
public class User {
    @With
    private UUID uuid;
    private String username;
    private String password;
    private BigInteger balance;
    private List<Card> stack;
    private List<Card> deck;
}
