package ski.mashiro.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;

import java.util.List;
import java.util.Map;

/**
 * @author MashiroT
 * @since 2023-07-27
 */
public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <K, V> Map<K, V> trans2Map(String json, Class<K> kClazz, Class<V> vClazz) throws JsonProcessingException {
        MapType mapType = OBJECT_MAPPER.getTypeFactory().constructMapType(Map.class, kClazz, vClazz);
        return OBJECT_MAPPER.readValue(json, mapType);
    }

    public static <T> List<T> trans2List(String json, Class<T> clazz) throws JsonProcessingException {
        CollectionType collectionType = OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz);
        return OBJECT_MAPPER.readValue(json, collectionType);
    }
}
