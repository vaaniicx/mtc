package at.if22b208.mtc.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    // TODO: Error handling
    public static <T> T getObjectFromJsonString(String json, Class<T> type) throws JsonProcessingException {
        return mapper.readValue(json, type);
    }

    public static <T> String getJsonStringFromObject(T object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }
}
