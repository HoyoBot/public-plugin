package net.catrainbow.dailyreward;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TimeUtil {

    public static boolean canUnBan(LocalDateTime nowTime, LocalDateTime newTime) {
        return nowTime.isAfter(newTime);
    }

    public static int[] getTimeBetween2(LocalDateTime nowTime, LocalDateTime newTime) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = localTimeToDate(newTime);
        Date date = localTimeToDate(nowTime);
        long l = now.getTime() - date.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        return new int[]{
                (int) day,
                (int) hour,
                (int) min,
                (int) s
        };

    }

    public static Date localTimeToDate(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        java.util.Date date = Date.from(instant);
        return date;
    }

    public static int[] getNowTimeForArray() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return new int[]{
                localDateTime.getYear(),
                localDateTime.getMonthValue(),
                localDateTime.getDayOfMonth(),
                localDateTime.getHour(),
                localDateTime.getMinute(),
                localDateTime.getSecond()
        };
    }

    public static LocalDateTime stringToTime(String str) {
        DateTimeFormatter timeDtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(str, timeDtf);
        return localDateTime;
    }

    public static int[] timeToArray(LocalDateTime time) {
        return new int[]{
                time.getYear(),
                time.getMonthValue(),
                time.getDayOfMonth(),
                time.getHour(),
                time.getMinute(),
                time.getSecond()
        };
    }

    public static LocalDateTime getNowTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime;
    }

    public static LocalDateTime plusTimeByDays(LocalDateTime now, int days) {
        return now.plusDays(days);
    }

    public static LocalDateTime plusTimeByHours(LocalDateTime now, int hours) {
        return now.plusHours(hours);
    }

    public static LocalDateTime plusTimeByMinute(LocalDateTime now, int minute) {
        return now.plusMinutes((long) minute);
    }

    public static LocalDateTime plusTimeByMonth(LocalDateTime now, int month) {
        return now.plusMonths(month);
    }

    public static LocalDateTime createTime(int year, int month, int day, int hour, int second) {
        LocalDateTime localDateTime = LocalDateTime.of(year, month, day, hour, second);
        return localDateTime;
    }

    public static String formatTime(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("YYYY-MM-dd hh:mm:ss"));
    }

}
