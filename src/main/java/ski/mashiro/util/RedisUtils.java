package ski.mashiro.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author MashiroT
 * @since 2023-08-21
 */
@Component
public class RedisUtils {
    private static final String EMPTY_VALUE = "";
    private static final long EMPTY_TTL = 900;
    private final ObjectMapper objectMapper;
    private final StringRedisTemplate redisTemplate;

    public RedisUtils(ObjectMapper objectMapper, StringRedisTemplate redisTemplate) {
        this.objectMapper = objectMapper;
        this.redisTemplate = redisTemplate;
    }

    public <T> void setCache(String key, T value, Long ttl, TimeUnit timeUnit) throws JsonProcessingException {
        redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(value), ttl, timeUnit);
    }

    public <T, ID> T getOrSetCache(String key, ID id, Class<T> clazz, Long ttl, TimeUnit timeUnit, Function<ID, T> callback) throws JsonProcessingException {
        String cacheContent = redisTemplate.opsForValue().get(key);
        if (StringUtils.hasText(cacheContent)) {
            return objectMapper.readValue(cacheContent, clazz);
        }
        if (Objects.nonNull(cacheContent)) {
            return null;
        }
        T data = callback.apply(id);
        if (Objects.isNull(data)) {
            redisTemplate.opsForValue().set(key, EMPTY_VALUE, EMPTY_TTL, TimeUnit.SECONDS);
            return null;
        }
        setCache(key, data, ttl, timeUnit);
        return data;
    }

    public <T, E, ID> T getOrSetCache(String key, ID id, Class<T> clazz, Class<E> genericClazz, Long ttl, TimeUnit timeUnit, Function<ID, T> callback) throws JsonProcessingException {
        String cacheContent = redisTemplate.opsForValue().get(key);
        if (StringUtils.hasText(cacheContent)) {
            JavaType parametricType = objectMapper.getTypeFactory().constructParametricType(clazz, genericClazz);
            return objectMapper.readValue(cacheContent, parametricType);
        }
        if (Objects.nonNull(cacheContent)) {
            return null;
        }
        T data = callback.apply(id);
        if (Objects.isNull(data)) {
            redisTemplate.opsForValue().set(key, EMPTY_VALUE, EMPTY_TTL, TimeUnit.SECONDS);
            return null;
        }
        setCache(key, data, ttl, timeUnit);
        return data;
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 大数据量慎用
     * @param parentKey 前缀Key
     */
    public void deleteBatch(String parentKey) {
        Set<String> keys = redisTemplate.keys(parentKey + ":*");
        if (Objects.isNull(keys) || keys.isEmpty()) {
            return;
        }
        for (String key : keys) {
            redisTemplate.delete(key);
        }
    }
}
