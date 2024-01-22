package at.if22b208.mtc.dto.trading;

import at.if22b208.mtc.entity.enumeration.CardType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

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
