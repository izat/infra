package infrastructure.bean;

import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static infrastructure.bean.ExceptionUtils.uncheck;

public final class HttpUtils {
    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    private HttpUtils() {
    }

    private static String responseAsString(Request request) {
        return uncheck(() -> request.execute().returnContent().asString(CharsetUtils.UTF_8));
    }

    private static <T> T responseAsObject(Request request, Class<T> clazz) {
        return uncheck(() -> JsonUtils.toObject(responseAsString(request), clazz));
    }

    public static <T> T get(String url, Class<T> clazz) {
        logger.info("start HTTP GET {}", url);
        String json = responseAsString(Request.Get(url));
        logger.info("end HTTP GET {}, response {}", url, json);
        return JsonUtils.toObject(json, clazz);
    }

    public static <T> T post(String url, Object body, Class<T> clazz) {
        String bodyString = JsonUtils.toJson(body);
        Request request = Request.Post(url).bodyString(bodyString, ContentType.APPLICATION_JSON);
        logger.info("start HTTP POST {} {}", url, bodyString);
        String json = responseAsString(request);
        logger.info("end HTTP POST {} {}, response {}", url, body, json);
        return JsonUtils.toObject(json, clazz);
    }
}