package infrastructure.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


/**
 * 日期工具类
 * 默认使用 "yyyy-MM-dd HH:mm:ss" 格式化日期
 */
public final class DateUtil {
    /**
     * 英文简写（默认）如：2010-12-01
     */
    public static String FORMAT_SHORT = "yyyy-MM-dd";

    /**
     * 英文全称  如：2010-12-01 23:15:06
     */
    public static String FORMAT_LONG = "yyyy-MM-dd HH:mm:ss";

    /**
     * 精确到毫秒的完整时间    如：yyyy-MM-dd HH:mm:ss.S
     */
    public static String FORMAT_FULL = "yyyy-MM-dd HH:mm:ss.S";

    /**
     * 中文简写  如：2010年12月01日
     */
    public static String FORMAT_SHORT_CN = "yyyy年MM月dd";

    /**
     * 中文全称  如：2010年12月01日  23时15分06秒
     */
    public static String FORMAT_LONG_CN = "yyyy年MM月dd日  HH时mm分ss秒";

    /**
     * 精确到毫秒的完整中文时间
     */
    public static String FORMAT_FULL_CN = "yyyy年MM月dd日  HH时mm分ss秒SSS毫秒";

    /**
     * PACS系统中日期格式 yyyyMMDDhhmmss
     */
    public static String FORMAT_PACS_FROM = "yyyyMMddhhmmss";

    /**
     * 订单日志格式 yyyyMMDDHHmmss
     */
    public static String FORMAT_ORDER_FROM = "yyyyMMddHHmmss";

    /**
     * PACS系统中生日日期格式 yyyyMMdd
     */
    public static String FORMAT_PACS_B_DAY = "yyyyMMdd";

    /**
     * PACS系统中日期格式yyyy-MM-dd HH:mm
     */
    public static String FORMAT_PACS_TO = "yyyy-MM-dd HH:mm";

    /**
     * Thu Oct 11 00:00:00 CST 2018 ->2018-10-11 00:00:00
     */
    public static final String FORMAT_SYSTEM = "EEE MMM dd HH:mm:ss zzz yyyy";

    /**
     * 获得默认的 date pattern
     */
    public static String getDatePattern() {
        return FORMAT_LONG;
    }

    /**
     * 根据预设格式返回当前日期
     *
     * @return {@link String}
     */
    public static String getNow() {
        return format(new Date());
    }

    /**
     * 根据用户格式返回当前日期
     *
     * @param format
     * @return {@link String}
     */
    public static String getNow(String format) {
        return format(new Date(), format);
    }


    /**
     * 使用预设格式格式化日期
     *
     * @param date
     * @return {@link String}
     */
    public static String format(Date date) {
        return format(date, getDatePattern());
    }

    /**
     * 使用用户格式格式化日期
     *
     * @param date    日期
     * @param pattern 日期格式
     * @return {@link Date}
     */
    public static String format(Date date, String pattern) {
        String returnValue = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            returnValue = df.format(date);
        }
        return (returnValue);
    }

    /**
     * 使用预设格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @return {@link Date}
     */
    public static Date parse(String strDate) {
        return parse(strDate, getDatePattern());
    }

    /**
     * 使用用户格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期格式
     * @return {@link Date}
     */
    public static Date parse(String strDate, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(strDate);
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
    }

    /**
     * 在日期上增加数个整月
     *
     * @param date 日期
     * @param n    要增加的月数
     * @return {@link Date}
     */
    public static Date addMonth(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, n);
        return cal.getTime();
    }

    /**
     * 在日期上增加天数
     *
     * @param date 日期
     * @param n    要增加的天数
     * @return {@link Date}
     */
    public static Date addDay(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, n);
        return cal.getTime();
    }

    /**
     * 获取时间戳
     */
    public static String getTimeString() {
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_FULL);
        Calendar calendar = Calendar.getInstance();
        return df.format(calendar.getTime());
    }

    /**
     * 获取日期年份
     *
     * @param date 日期
     * @return {@link String}
     */
    public static String getYear(Date date) {
        return format(date).substring(0, 4);
    }

    /**
     * 按默认格式的字符串距离今天的天数
     *
     * @param date 日期字符串
     * @return {@link Integer}
     */
    public static int countDays(String date) {
        return countDays(parseDate(date));
    }

    /**
     * 按默认格式的字符串距离今天的天数
     *
     * @param date 日期
     * @return {@link Integer}
     */
    public static int countDays(Date date) {
        long t = Calendar.getInstance().getTime().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        long t1 = c.getTime().getTime();
        return (int) (t / 1000 - t1 / 1000) / 3600 / 24;
    }

    /**
     * 按用户格式字符串距离今天的天数
     *
     * @param date   日期字符串
     * @param format 日期格式
     * @return {@link Integer}
     */
    public static int countDays(String date, String format) {
        long t = Calendar.getInstance().getTime().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(parse(date, format));
        long t1 = c.getTime().getTime();
        return (int) (t / 1000 - t1 / 1000) / 3600 / 24;
    }

    /**
     * 按用户格式字符串计算两个日期相距的天数
     *
     * @param date1  date2   日期字符串
     * @param format 日期格式
     * @return {@link Integer}
     */

    public static int countDays(String date1, String date2, String format) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(parse(date1, format));
        c2.setTime(parse(date2, format));

        long t1 = c1.getTime().getTime();
        long t2 = c2.getTime().getTime();
        return (int) (t2 / 1000 - t1 / 1000) / 3600 / 24;
    }

    /**
     * 计算两个日期相距的小时数
     *
     * @param date1
     * @param date2
     * @return {@link Integer}
     */
    public static int countHours(Date date1, Date date2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(date1);
        c2.setTime(date2);

        long t1 = c1.getTime().getTime();
        long t2 = c2.getTime().getTime();
        return Math.round((float) ((t2 / 1000d - t1 / 1000d) / 3600));
    }

    /**
     * 计算两个日期相距的天数
     *
     * @param date1
     * @param date2
     * @return {@link Integer}
     */
    public static int countDays(Date date1, Date date2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(date1);
        c2.setTime(date2);

        long t1 = c1.getTime().getTime();
        long t2 = c2.getTime().getTime();
        return Math.round((float) ((t2 / 1000d - t1 / 1000d) / 3600 / 24f));
    }

    /**
     * 计算两个日期相距的返回的字符串
     *
     * @param date1
     * @param date2
     * @return {@link String}
     */
    public static String getCountDays(Date date1, Date date2) {
        if (date1 == null || date2 == null) return "未知";
        int hours = countHours(date1, date2);
        if (hours < 72) return hours + "小时";
        else if (hours < 24 * 365) return (hours / 24) + "天";
        else return getAge(date1, FORMAT_FULL) + "岁";
    }

    /**
     * 计算当前时间距离date1距离返回的字符串
     *
     * @param date1
     * @return
     */
    public static String getCountDays(Date date1) {
        Calendar c = Calendar.getInstance();
        return getCountDays(date1, c.getTime());
    }

    /**
     * 按用户格式字符串计算两个日期相距的天数
     *
     * @param date1
     * @param date2
     * @param format
     * @return
     */
    @SuppressWarnings("deprecation")
    public static int countDays(Date date1, String date2, String format) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        date1.setHours(0);
        date1.setMinutes(0);
        date1.setSeconds(0);
        c1.setTime(date1);

        c2.setTime(parse(date2, format));

        long t1 = c1.getTime().getTime();
        long t2 = c2.getTime().getTime();
        return (int) (t2 / 1000 - t1 / 1000) / 3600 / 24;
    }

    /**
     * 按用户格式字符串计算两个日期相距的小时数除以6
     *
     * @param date1
     * @param date2
     * @param format
     * @return
     */

    public static int countHours(String date1, String date2, String format) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(parse(date2 + " 00:00:00", format));
        c2.setTime(parse(date2 + " 00:00:00", format));

        long t1 = c1.getTime().getTime();
        long t2 = c2.getTime().getTime();
        return (int) (t2 / 1000 - t1 / 1000) / 3600 / 6;
    }

    /**
     * 得到curDate本周的第一天
     *
     * @param curDate
     * @return
     */
    @SuppressWarnings("static-access")
    public static Date getFirstDayInWeek(Date curDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(curDate);
        c.set(c.DAY_OF_WEEK, c.getFirstDayOfWeek());
        return c.getTime();
    }

    /**
     * 得到curDate本周的最后一天
     *
     * @param curDate
     * @return
     */
    @SuppressWarnings("static-access")
    public static Date getLastDayInWeek(Date curDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(curDate);
        c.set(c.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6);
        return c.getTime();
    }

    /**
     * 日期格式化转换
     *
     * @param dateStr
     * @param bFormat
     * @param eFormat
     * @return
     */
    public static String converFormat(String dateStr, String bFormat, String eFormat) {
        Date date = parse(dateStr, bFormat);
        return format(date, eFormat);
    }

    /**
     * 得到岁数
     *
     * @param birthday
     * @param fromat
     * @return
     */
    @SuppressWarnings("deprecation")
    public static int getAge(String birthday, String fromat) {
        Date date = parse(birthday, fromat);
        Date currDate = new Date();
        return currDate.getYear() - date.getYear();
    }

    /**
     * 得到岁数
     *
     * @param birthday
     * @param fromat
     * @return
     */
    @SuppressWarnings("deprecation")
    public static int getAge(Date birthday, String fromat) {

        Date currDate = new Date();
        return currDate.getYear() - birthday.getYear();
    }

    /**
     * 得到当前时间
     *
     * @return
     */
    public static Calendar getCurrent() {
        return Calendar.getInstance();
    }

    /**
     * 得到cal的年月日和设置小时，分，秒的时间
     *
     * @param cal
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static Calendar getCalendar(Calendar cal, int hour, int minute, int second) {
        return getCalendar(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH),
                hour, minute, second);
    }

    /**
     * 得到设置年,月,日的时间 小时0，分钟0，秒数0，毫秒0
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static Calendar getCalendar(int year, int month, int day) {
        return getCalendar(year, month, day, 0, 0, 0);
    }

    /***
     * 得到设置年,月,日,小时，分钟，秒数的时间 毫秒0
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param second
     * @return 设置的时间
     */
    public static Calendar getCalendar(int year, int month, int day, int hour, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, hour, minute, second);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    /**
     * 得到本周第一天开始时间
     *
     * @return
     */
    public static Date getCurrentWeekBefore() {
        Calendar cal = getCurrent();
        cal.set(Calendar.DAY_OF_WEEK, 1);
        return getCalendar(cal, 0, 0, 0).getTime();
    }

    /**
     * 得到过去一周的时间
     *
     * @return
     */
    public static Date getBeforeWeek() {
        Calendar cal = getCurrent();
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 6);
        return getCalendar(cal, 24, 0, 0).getTime();
    }

    /**
     * 得到过去一个月的时间
     *
     * @return
     */
    public static Date getBeforeMonth() {
        Calendar cal = getCurrent();
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 29);
        return getCalendar(cal, 24, 0, 0).getTime();
    }

    /**
     * 得到下周开始时间
     *
     * @return
     */
    public static Date getCurrentWeekAfter() {
        Calendar cal = getCurrent();
        cal.set(Calendar.DAY_OF_WEEK, 14);
        return getCalendar(cal, 24, 0, 0).getTime();
    }

    /**
     * 得到今天0点的时间
     *
     * @return
     */
    public static Date getCurrentDateBefore() {
        Calendar cal = getCurrent();
        return getCalendar(cal, 0, 0, 0).getTime();
    }


    /**
     * 得到明天0点的时间
     *
     * @return
     */
    public static Date getCurrentDateAfter() {
        Calendar cal = getCurrent();
        return getCalendar(cal, 24, 0, 0).getTime();
    }

    /**
     * 得到现在过去一天的时间
     *
     * @return
     */
    public static Date getCurrentDateBeforeDay() {
        Calendar cal = getCurrent();
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 1);
        return cal.getTime();
    }

    /**
     * 得到现在未来一天的时间
     *
     * @return
     */
    public static Date getCurrentDateAfterDay() {
        Calendar cal = getCurrent();
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
        return cal.getTime();
    }

    /**
     * 得到指定时间0点的时间
     *
     * @return
     */
    public static Date getCurrentDateBefore(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return getCalendar(cal, 0, 0, 0).getTime();
    }

    /**
     * 得到指定时间明天0点的时间
     *
     * @return
     */
    public static Date getCurrentDateAfter(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return getCalendar(cal, 24, 0, 0).getTime();
    }

    /**
     * 得到当前日期的前<code>day<code>天0:00:00时间
     *
     * @return
     */
    public static Date getCurrentDateBeforeDay(int day) {
        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal2.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) - day, 0, 0, 0);
        cal2.set(Calendar.MILLISECOND, 0);
        return cal2.getTime();
    }

    /**
     * 得到当前日期的前<code>hour<code>天00:00时间
     *
     * @return
     */
    public static Date getCurrentDateBeforeHour(int hour) {
        Calendar cal = Calendar.getInstance();
        return getCalendar(cal, cal.get(Calendar.HOUR_OF_DAY) - hour, 0, 0).getTime();
    }

    /**
     * 得到指定时间0分0秒的时间
     *
     * @return
     */
    public static Date getCurrentHourBefore(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return getCalendar(cal, cal.get(Calendar.HOUR_OF_DAY), 0, 0).getTime();
    }

    /**
     * 转换字符串成date
     *
     * @param date
     * @return
     */
    public static Date parseDate(String date) {
        String[] pattern = new String[]{
                "yyyy-MM-dd HH:mm:ss",
                "yyyy/MM/dd HH:mm:ss",
                "yyyyMMddHHmmss",
                "yyyy-MM-dd",
                "yyyy/MM/dd",
                "yyyyMMdd"
        };
        Date cDate;
        for (int i = 0; i < pattern.length; i++) {
            cDate = parse(date, pattern[i]);
            if (cDate != null) {
                return cDate;
            }
        }
        return null;
    }


    /**
     * 得到指定下一个小时时间0分0秒的时间
     *
     * @return
     */
    public static Date getCurrentHourAfter(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return getCalendar(cal, cal.get(Calendar.HOUR_OF_DAY) + 1, 0, 0).getTime();
    }

    /**
     * 得到年龄
     *
     * @param date
     * @return
     */
    public static String getAgesDetail(String date) {
        Date dat = parseDate(date);
        return getAgesDetail(dat);
    }

    /**
     * 得到年龄
     *
     * @param date
     * @return
     */
    public static String getAgesDetail(Date date) {

        Calendar c = Calendar.getInstance();
        if (date == null) {
            c = null;
        } else {
            c.setTime(date);
        }
        return getAgesDetail(c);
    }

    /**
     * 得到年龄
     *
     * @param date
     * @return
     */
    public static String getAgesDetail(Calendar date) {
        if (date == null) {
            return "未知";
        }

        Calendar cal = Calendar.getInstance();
        int hour = countHours(date.getTime(), cal.getTime());
        if (hour < 72) {
            return hour + "小时";
        } else if (hour < 100 * 24) {
            return (hour / 24) + "天";
        } else if (hour < 24 * 24 * 30) {
            if (cal.get(Calendar.YEAR) == date.get(Calendar.YEAR)) {
                return (cal.get(Calendar.MONTH) - date.get(Calendar.MONTH)) + "月";
            } else {
                return (cal.get(Calendar.MONTH) + 12 * (cal.get(Calendar.YEAR) - date.get(Calendar.YEAR)) - date.get(Calendar.MONTH)) + "月";
            }
        } else {
            return (cal.get(Calendar.YEAR) - date.get(Calendar.YEAR)) + "岁";
        }
    }

    /**
     * 用于显示动态发出的时间
     * 判断一个日期跟现在相隔多长时间
     * 如果这个日期在当前日期之后则显示刚刚
     *
     * @param date
     * @return
     */
    public static String getDateDiff(Date date) {
        long dateTime = date.getTime();
        long now = new Date().getTime();
        long min = 1000 * 60;
        long h = min * 60;
        long day = h * 24;
        long month = day * 30;
        long diffValue = now - dateTime;
        String result = "";
        if (diffValue < 0) {
            result = "刚刚";
            return result;
        }
        long monthC = diffValue / month;
        long weekC = diffValue / (7 * day);
        long dayC = diffValue / day;
        long hourC = diffValue / h;
        long minC = diffValue / min;
        if (monthC >= 1) {
            result = monthC + "月前";
        } else if (weekC >= 1) {
            result = weekC + "周前";
        } else if (dayC >= 1) {
            result = dayC + "天前";
        } else if (dayC < 1 && hourC >= 1) {
            result = hourC + "小时前";
        } else if (hourC < 1 && minC >= 1) {
            result = minC + "分钟前";
        } else if (minC < 1) {
            result = "刚刚";
        }
        return result;
    }

    public final static String yesterdayString(String pattern) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        return DateFormatUtils.format(cal.getTime(), pattern);
    }


    /**
     * localDateTime 转 OffsetDateTime
     *
     * @param time
     * @return
     */
    public static OffsetDateTime localConvertOffset(LocalDateTime time) {
        if (time != null) {
            ZonedDateTime zonedDateTime = time.atZone(ZoneId.systemDefault());
            return zonedDateTime.toOffsetDateTime();
        }
        return null;
    }

    /**
     * Date -> OffsetDateTime
     *
     * @param date
     * @return
     */
    public static OffsetDateTime dateConvertOffset(Date date) {
        if (date == null) return null;
        return localConvertOffset(LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()));
    }

    /**
     * 是否是早于现在的时间
     *
     * @param date
     * @return
     */
    public static boolean isEarlyThanNow(Date date) {
        return date != null && date.getTime() < System.currentTimeMillis();
    }


    /**
     * 得到指定日期的0点
     *
     * @param time
     * @return
     */
    public static OffsetDateTime getZeroTime(OffsetDateTime time) {
        if (Objects.isNull(time)) time = OffsetDateTime.now();
        return time.withHour(0).withMinute(0).withSecond(0).withNano(0);
    }
    /**
     * 判断日期是否距离现在一年以内
     *
     * @param time
     * @return
     */
    public static boolean inOneYear(OffsetDateTime time) {
        return Period.between(time.toLocalDate(), LocalDate.now()).getYears() == 0;
    }

    /**
     * 获取毫秒值(默认东八区北京时间)
     *
     * @param time
     * @return
     */
    public static long getTimeMills(OffsetDateTime time) {
        if (Objects.isNull(time)) return 0;
        return time.toLocalDateTime().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 日期String转换成OffsetDateTime
     *
     * @param time
     * @return
     */
    public static OffsetDateTime strToOffTime(String time) {
        if (StringUtils.isBlank(time)) return null;
        LocalDateTime localDateTime = null;
        try {
            localDateTime = LocalDateTime.parse(time);
        } catch (Exception ignored) {
        }
        if (localDateTime == null) {
            Date date = parseDate(time);
            if (date == null) {
                SimpleDateFormat format = new SimpleDateFormat(FORMAT_SYSTEM, Locale.ENGLISH);
                try {
                    date = format.parse(time);
                } catch (ParseException ignored) {
                }
                if (date == null) return null;
            }
            localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        }
        return localConvertOffset(localDateTime);
    }

    public static void main(String[] args) throws Exception {
//        System.out.println(LocalDateTime.parse("2018-12-14 03:59:01"));
//        System.out.println(strToOffTime("2018-10-05"));
//        System.out.println(dateConvertOffset(parseDate("2018-10-03 12:10:11")));
//        System.out.println(getLastDayInWeek(new Date()));parse("19850101 83336","yyyyMMDDhhmmss");
//        System.out.println(parse("2011-7-29",FORMAT_LONG).getTime());
//        System.out.println(parse("2011-11-2 11:10:00 00:00:00",FORMAT_LONG));
//        System.out.println(getCurrentDateBefore());
//        System.out.println(getCurrentDateAfter());
//        System.out.println(parse("19850101 231053","yyyyMMDD hhmmss"));
//        System.out.println(converFormat("19850101231053", FORMAT_PACS_FROM, FORMAT_PACS_TO));
//        System.out.println(getAge("19850101", FORMAT_PACS_B_DAY));
//        System.out.println(format(new Date(), "yyyy-MM-dd"));
//        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String dstr="2018-01-09 14:08:08";
//        java.util.Date date=sdf.parse(dstr);
//        System.out.println(getDateDiff(date));
//        System.out.println(dateConvertOffset(parseDate("2008/01/01")));
    }
}