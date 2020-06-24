package com.github.nigthcrawlerx1.jdacommands.utils;

public class TimeUtils {
    public static String getTime(long time) {
        long variacao = time;
        long varsegundos = variacao / 1000 % 60;
        long varminutos = variacao / 60000 % 60;
        long varhoras = variacao / 3600000 % 24;
        long vardias = variacao / 86400000 % 30;

        String segundos = String.valueOf(varsegundos).replaceAll("-", "");
        String minutos = String.valueOf(varminutos).replaceAll("-", "");
        String horas = String.valueOf(varhoras).replaceAll("-", "");
        String dias = String.valueOf(vardias).replaceAll("-", "");

        if (dias.equals("0") && horas.equals("0") && minutos.equals("0")) {
            return segundos + "segundos";
        }
        if (dias.equals("0") && horas.equals("0")) {
            return minutos + "minutos " + segundos + "segundos";
        }
        if (dias.equals("0")) {
            return horas + "horas " + minutos + "minutos " + segundos + "segundos";
        }
        return dias + "dias " + horas + "horas " + minutos + "minutos " + segundos + "segundos ";
    }
}
