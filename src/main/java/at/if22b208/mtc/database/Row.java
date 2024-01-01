package at.if22b208.mtc.database;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class Row {
    private final Map<String, Object> content = new HashMap<>();

    public void put(String column, Object content) {
        getContent().put(column, content);
    }

    public Integer getInt(String column) {
        return (Integer) getContent().get(column);
    }

    public Long getLong(String column) {
        return (Long) getContent().get(column);
    }

    public UUID getUuid(String column) {
        return (UUID) getContent().get(column);
    }

    public String getString(String column) {
        return (String) getContent().get(column);
    }
}
