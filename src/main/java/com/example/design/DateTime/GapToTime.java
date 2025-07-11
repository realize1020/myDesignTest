package com.example.design.DateTime;

import cn.hutool.core.date.CalendarUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

public class GapToTime {
    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime localDateTime = now.withHour(18).withMinute(0).withSecond(0);
        if(now.isBefore(localDateTime)){
            localDateTime=localDateTime.plusDays(1);
        }
        System.out.println(localDateTime.format(DateTimeFormatter.ofPattern("YYYY-MM-dd:HH:mm:ss")));
        Duration between2 = LocalDateTimeUtil.between(LocalDateTime.now(), localDateTime);
        System.out.println(between2.getSeconds());

        System.out.println("----------------------------------------------");
        LocalDateTime localDateTime1 = now.withHour(18).withMinute(0).withSecond(0);
        LocalDateTime localDateTime2 = now.withHour(18).withMinute(10).withSecond(0);
        int i = localDateTime2.getMinute() - localDateTime1.getMinute();
        System.out.println(i);

        System.out.println("----------------------------------------------");

//        Duration between = LocalDateTimeUtil.between(localDateTime1, localDateTime2);
//        long seconds = between.getSeconds();
//        BigDecimal fstdardOutput = new BigDecimal(11);
//        BigDecimal funitPrice = new BigDecimal(11);
//        BigDecimal unitMoney = fstdardOutput.multiply(funitPrice);
//        BigDecimal secondMoney = unitMoney.divide(new BigDecimal(3600).multiply(fstdardOutput), 6, RoundingMode.HALF_UP);
//        BigDecimal lossMoney = secondMoney.multiply(new BigDecimal(seconds));
//        System.out.println(lossMoney);

        System.out.println("----------------------------------------------");

        String fpriceDate = "2025-06-01";
        String[] split = fpriceDate.split("-");
        LocalDateTime time = LocalDateTime.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), 0, 0, 0);
        LocalDateTime time2 = time.minusMonths(1);
        System.out.println(time2.format(DateTimeFormatter.ofPattern("YYYY-MM")));


        System.out.println(LocalDate.now().format(DateTimeFormatter.ofPattern("YYYY-MM")));

        System.out.println("*******************************************************");
        BigDecimal fstdardOutput = BigDecimal.valueOf(11);
        BigDecimal funitPrice = BigDecimal.valueOf(11);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        long betweentime = LocalDateTimeUtil.between(LocalDateTime.parse("2025-07-09 01:52:04",formatter), LocalDateTime.parse("2025-07-09 06:00:00",formatter), ChronoUnit.MINUTES);
        BigDecimal unitMoney = fstdardOutput.multiply(funitPrice);
        BigDecimal secondMoney = unitMoney.divide(new BigDecimal(60), 6, RoundingMode.HALF_UP);
        BigDecimal lossMoney = secondMoney.multiply(new BigDecimal(betweentime).divide(BigDecimal.ONE)).setScale(6,RoundingMode.HALF_UP);
        System.out.println(lossMoney);
    }
}
