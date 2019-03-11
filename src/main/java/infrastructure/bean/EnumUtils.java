package infrastructure.bean;

import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

/**
 * 枚举工具类。
 */
public final class EnumUtils {
    private EnumUtils() {
    }

    public interface ValueTypeEnum<T> {
        T getValue();
    }

    /**
     * 存储全部枚举的Map。
     */
    private static final Map<Class<? extends Enum<? extends ValueTypeEnum>>, Map<Object, Object>> enumHolder = Maps.newHashMap();

    /**
     * 向Map中添加参数指定枚举的全部枚举值。
     *
     * @param cls 需要注册的Enum类型
     */
    private static void register(Class<? extends Enum<? extends ValueTypeEnum>> cls) {
        if (enumHolder.containsKey(cls)) {
            throw new IllegalArgumentException("Enum " + cls + " has been registered already.");
        }

        Map<Object, Object> entityMap = new HashMap<>();

        for (Enum e : cls.getEnumConstants()) {
            Object value = ((ValueTypeEnum) e).getValue();

            if (entityMap.containsKey(value)) {
                throw new IllegalArgumentException("Enum " + cls + " contains conflicted value " + value
                        + " used by " + entityMap.get(value) + " and " + e);
            }

            entityMap.put(value, e);
        }

        enumHolder.put(cls, entityMap);
    }

    /**
     * 通过枚举的value属性获取枚举。
     *
     * @param cls   枚举类
     * @param value 枚举value
     * @param <T>   枚举类型
     * @return 枚举
     */
    @SuppressWarnings("unchecked")
    public static <T extends Enum<? extends ValueTypeEnum>> T of(Class<T> cls, Object value) {
        if (!enumHolder.containsKey(cls)) {
            register(cls);
        }
        return (T) enumHolder.get(cls).get(value);
    }
}

