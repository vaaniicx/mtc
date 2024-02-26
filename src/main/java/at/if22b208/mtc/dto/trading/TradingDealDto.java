package at.if22b208.mtc.dto.trading;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import at.if22b208.mtc.entity.enumeration.CardType;
import lombok.Data;

@Data
public class TradingDealDto {
    @JsonProperty("id")
    private UUID uuid;

    @JsonProperty("cardtotrade")
    private UUID cardUuid;

    @JsonProperty("type")
    private CardType cardType;

    @JsonProperty("minimumdamage")
    private double minimumDamage;
}
