package com.github.nigthcrawlerx1.jdacommands.utils;

public class TimeUtils {
    public static String getTime(long time) {
        final long variacao = time,
                seconds = variacao / 1000 % 60,
                minutes = variacao / 60000 % 60,
                hours = variacao / 3600000 % 24,
                days = variacao / 86400000 % 30;

        String duration = (days == 0 ? "" : days + " dias,") + (hours == 0 ? "" : hours + " horas, ")
                + (minutes == 0 ? "" : minutes + " minutos, ") + (seconds == 0 ? "" : seconds + " segundos, ");

        duration = replaceLast(duration, ", ", "");

        return replaceLast(duration, ",", " e");

    }
    private static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
    }

}
