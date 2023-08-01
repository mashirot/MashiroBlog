package ski.mashiro.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author MashiroT
 * @since 2023-07-27
 */
@Component
public class JsonUtils {

    private final ObjectMapper objectMapper;

    public JsonUtils(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <K, V> Map<K, V> trans2Map(String json, Class<K> kClazz, Class<V> vClazz) throws JsonProcessingException {
        MapType mapType = objectMapper.getTypeFactory().constructMapType(Map.class, kClazz, vClazz);
        return objectMapper.readValue(json, mapType);
    }

    public <T> List<T> trans2List(String json, Class<T> clazz) throws JsonProcessingException {
        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
        return objectMapper.readValue(json, collectionType);
    }
}
