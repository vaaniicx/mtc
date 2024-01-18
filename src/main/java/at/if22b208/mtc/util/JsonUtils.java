package at.if22b208.mtc.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for handling JSON serialization and deserialization using Jackson.
 */
@Slf4j
public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();
    // TODO: Error handling

    /**
     * Deserializes a JSON string into an object of the specified type.
     *
     * @param <T>  The type of the target object.
     * @param json The JSON string to deserialize.
     * @param type The class representing the target type.
     * @return An object of the specified type.
     */
    public static <T> T getObjectFromJsonString(String json, Class<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * Deserializes a JSON string into a list of objects of the specified type.
     *
     * @param <T>  The type of the elements in the list.
     * @param json The JSON string to deserialize.
     * @param type The class representing the target type.
     * @return A list of objects of the specified type.
     */
    public static <T> List<T> getListFromJsonString(String json, Class<T> type) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, type));
        } catch (JsonProcessingException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Serializes an object to a JSON string.
     *
     * @param <T>    The type of the object.
     * @param object The object to serialize.
     * @return A JSON string representing the serialized object.
     */
    public static <T> String getJsonStringFromObject(T object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    /**
     * Serializes an array to a JSON string.
     *
     * @param <T>   The type of the array.
     * @param array The array to serialize.
     * @return A JSON string representing the serialized array.
     */
    public static <T> String getJsonStringFromArray(T[] array) {
        try {
            return mapper.writeValueAsString(array);
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}
