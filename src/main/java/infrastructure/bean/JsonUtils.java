package infrastructure.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.TimeZone;

import static infrastructure.bean.ExceptionUtils.uncheck;

public final class JsonUtils {
    private JsonUtils() {
    }

    private static ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
            .timeZone(TimeZone.getDefault())
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .featuresToEnable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .featuresToDisable(
                    SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS,
                    DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                    DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS)
            .build();

    public static void setObjectMapper(ObjectMapper objectMapper) {
        JsonUtils.objectMapper = objectMapper;
    }

    public static String toJson(Object object) {
        return uncheck(() -> objectMapper.writeValueAsString(object));
    }

    public static <T> T toObject(String json, Class<T> clazz) {
        return uncheck(() -> objectMapper.readValue(json, clazz));
    }
}