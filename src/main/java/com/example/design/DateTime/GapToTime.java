package com.example.design.DateTime;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;

import java.text.DateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GapToTime {
    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime localDateTime = now.withHour(18).withMinute(0).withSecond(0);
        if(now.isBefore(localDateTime)){
            localDateTime=localDateTime.plusDays(1);
        }
        System.out.println(localDateTime.format(DateTimeFormatter.ofPattern("YYYY-MM-dd:HH:mm:ss")));
        Duration between = LocalDateTimeUtil.between(LocalDateTime.now(), localDateTime);
        System.out.println(between.getSeconds());


    }
}
