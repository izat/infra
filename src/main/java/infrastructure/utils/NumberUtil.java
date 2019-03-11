package infrastructure.utils;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Pattern;

/**
 * @author Qiuzh 2018/11/8 10:10
 */
public final class NumberUtil {

    private static final Pattern numberPattern = Pattern.compile("^(\\-|\\+)?\\d+(\\.\\d+)?$");
    private static final Pattern wholeNumberPattern = Pattern.compile("^-?[0-9]\\d*$");
    private static final BigDecimal _ONE = new BigDecimal("-1");

    /**
     * 是否是数字,包括小数
     *
     * @param s
     * @return
     */
    public static boolean isNumber(String s) {
        return s != null && numberPattern.matcher(s.trim()).matches();
    }

    /**
     * 是否是整数
     *
     * @param s
     * @return
     */
    public static boolean isWholeNumber(String s) {
        return s != null && wholeNumberPattern.matcher(s.trim()).matches();
    }

    /**
     * ***************************************************
     * 注意:1,BigDecimal需以String构造,否则失真
     *      2,默认保留2位,四舍五入
     *          6.666->6.67
     *          -6.664->-6.66
     * ***************************************************
     */

    /**
     * b1 乘以 b2,默认保留2位小数,四舍五入
     *
     * @param b1
     * @param b2
     * @return
     */
    public static BigDecimal multiply(BigDecimal b1, BigDecimal b2) {
        if (b1 == null || b2 == null || BigDecimal.ZERO.compareTo(b1) == 0 || BigDecimal.ZERO.compareTo(b2) == 0)
            return BigDecimal.ZERO;
        return b1.multiply(b2).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * b1 除以 b2,b1为null默认等于0,b2不能为null或等于0,否则返回null,默认保留2位小数,四舍五入
     *
     * @param b1
     * @param b2
     * @return
     */
    public static BigDecimal divide(BigDecimal b1, BigDecimal b2) {
        if (b2 == null || BigDecimal.ZERO.compareTo(b2) == 0) return null;
        if (b1 == null) b1 = BigDecimal.ZERO;
        return b1.divide(b2, 2, RoundingMode.HALF_UP);
    }

    /**
     * b1 加 b2,如果是null默认为0,默认保留2位小数,四舍五入
     *
     * @param bs
     * @return
     */
    public static BigDecimal add(BigDecimal... bs) {
        for (int i = 0; i < bs.length; i++) {
            if (bs[i] == null) bs[i] = BigDecimal.ZERO;
        }
        BigDecimal result = BigDecimal.ZERO;
        for (BigDecimal b : bs) {
            result = result.add(b);
        }
        return result.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 求正负数
     *
     * @param b
     * @return
     */
    public static BigDecimal opposite(BigDecimal b) {
        if (b == null) return BigDecimal.ZERO;
        return b.multiply(_ONE).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 默认保留2位小数,四舍五入,null返回0
     *
     * @param b
     * @return
     */
    public static BigDecimal scale(BigDecimal b) {
        if (b == null) return BigDecimal.ZERO;
        return b.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 是否比0大,null返回false
     *
     * @param b
     * @return
     */
    public static boolean biggerThanZero(BigDecimal b) {
        return b != null && (BigDecimal.ZERO.compareTo(b) < 0);
    }

    /**
     * 是否比0小,null返回false
     *
     * @param b
     * @return
     */
    public static boolean smallerThanZero(BigDecimal b) {
        return b != null && (BigDecimal.ZERO.compareTo(b) > 0);
    }

    /**
     * 是否等于0,null返回true
     *
     * @param b
     * @return
     */
    public static boolean isZero(BigDecimal b) {
        return b == null || (BigDecimal.ZERO.compareTo(b) == 0);
    }

    /**
     * 两个数值是否相等,null默认等于0
     *
     * @param b1
     * @param b2
     * @return
     */
    public static boolean isSameValue(BigDecimal b1, BigDecimal b2) {
        if (b1 == null) b1 = BigDecimal.ZERO;
        if (b2 == null) b2 = BigDecimal.ZERO;
        return b1.compareTo(b2) == 0;
    }

    /**
     * 是否<=0,null默认等于0
     *
     * @param b
     * @return
     */
    public static boolean isOrSmallerZero(BigDecimal b) {
        if (b == null) b = BigDecimal.ZERO;
        return b.compareTo(BigDecimal.ZERO) <= 0;
    }

    /**
     * 是否>=0,null默认等于0
     *
     * @param b
     * @return
     */
    public static boolean isOrBiggerZero(BigDecimal b) {
        if (b == null) b = BigDecimal.ZERO;
        return b.compareTo(BigDecimal.ZERO) >= 0;
    }
}