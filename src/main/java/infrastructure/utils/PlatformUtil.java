package infrastructure.utils;

import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * @author Qiuzh 2018/11/26 16:06
 */
public final class PlatformUtil {

    public static final String CODE_PREFIX = "MBER";

    /**
     * 校验客户ID是否符合平台格式
     *
     * @param strCode
     * @return
     */
    public static boolean isCodeLegal(String strCode) {
        return StringUtils.isNotBlank(strCode) &&
                strCode.trim().matches("MBER[0-9]{8}");
    }

    /**
     * MBER00010000->10000
     *
     * @param strCode
     * @return
     */
    public static Long strToCode(String strCode) {
        try {
            return Long.valueOf(strCode.replace(CODE_PREFIX, ""));
        } catch (NumberFormatException ignored) {
        }
        return null;
    }

    /**
     * 10000->MBER00010000
     *
     * @param code
     * @return
     */
    public static String codeToStr(Long code) {
        return String.format("MBER%08d", code);
    }


    public static boolean isEmpty(Collection c) {
        return c == null || c.isEmpty();
    }

    public static boolean isEmpty(Map map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Collection c) {
        return c != null && !c.isEmpty();
    }

    public static boolean isNotEmpty(Map map) {
        return map != null && !map.isEmpty();
    }

}
