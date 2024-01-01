package at.if22b208.mtc.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * Utility class for handling JSON serialization and deserialization using Jackson.
 */
public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();
    // TODO: Error handling

    /**
     * Deserializes a JSON string into an object of the specified type.
     *
     * @param <T>   The type of the target object.
     * @param json  The JSON string to deserialize.
     * @param type  The class representing the target type.
     * @return      An object of the specified type.
     * @throws JsonProcessingException If an error occurs during deserialization.
     */
    public static <T> T getObjectFromJsonString(String json, Class<T> type) throws JsonProcessingException {
        return mapper.readValue(json, type);
    }

    /**
     * Deserializes a JSON string into a list of objects of the specified type.
     *
     * @param <T>   The type of the elements in the list.
     * @param json  The JSON string to deserialize.
     * @param type  The class representing the target type.
     * @return      A list of objects of the specified type.
     * @throws JsonProcessingException If an error occurs during deserialization.
     */
    public static <T> List<T> getListFromJsonString(String json, Class<T> type) throws JsonProcessingException {
        return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, type));
    }

    /**
     * Serializes an object to a JSON string.
     *
     * @param <T>     The type of the object.
     * @param object  The object to serialize.
     * @return        A JSON string representing the serialized object.
     * @throws JsonProcessingException If an error occurs during serialization.
     */
    public static <T> String getJsonStringFromObject(T object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }
}
