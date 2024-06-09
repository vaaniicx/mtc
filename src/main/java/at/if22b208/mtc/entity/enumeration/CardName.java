package at.if22b208.mtc.entity.enumeration;

import lombok.Getter;

@Getter
public enum CardName {

    DRAGON("dragon"),
    GOBLIN("goblin"),
    ORK("ork"),
    WIZARD("wizard"),
    KNIGHT("knight"),
    WATER_SPELL("waterspell"),
    KRAKEN("kraken"),
    FIRE_ELF("fireelf");

    private final String value;

    CardName(String value) {
        this.value = value;
    }
}
