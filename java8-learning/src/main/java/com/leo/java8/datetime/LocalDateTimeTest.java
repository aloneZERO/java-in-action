package com.leo.java8.datetime;

import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

import static org.junit.Assert.assertEquals;

public class LocalDateTimeTest {

    // 1. LocalDate、LocalTime、LocalDateTime
    @Test
    public void test1() {
        LocalDateTime ldt = LocalDateTime.now();
        System.out.println(ldt + "\n"
                + ldt.getYear() + "-"
                + ldt.getMonthValue() + "-"
                + ldt.getDayOfMonth() + " "
                + ldt.getHour() + ":"
                + ldt.getMinute() + ":"
                + ldt.getSecond());

        LocalDateTime ld2 = LocalDateTime.of(
                2019, 3, 9,
                17, 43, 10);
        System.out.println(ld2);

        LocalDateTime ldt3 = ld2.plusYears(20);
        System.out.println(ldt3);

        LocalDateTime ldt4 = ld2.minusMonths(2);
        System.out.println(ldt4);
    }

    //2. Instant：时间戳
    //   使用 Unix 元年 1970年1月1日 00:00:00 所经历的毫秒值
    @Test
    public void test2() {
        Instant ins = Instant.now(); // 默认使用 UTC 时区
        System.out.println(ins);
        System.out.println(ins.toEpochMilli());

        OffsetDateTime odt = ins.atOffset(ZoneOffset.ofHours(8));
        System.out.println(odt);

        // 从元年开始加 5 秒
        Instant ins2 = Instant.ofEpochSecond(5);
        System.out.println(ins2);
    }

    //3. Duration：用于计算两个“时间”间隔
    //   Period：用于计算两个“日期”间隔
    @Test
    public void test3() {
        Instant ins1 = Instant.now();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Instant ins2 = Instant.now();
        System.out.println("间隔时间为：" +
                Duration.between(ins1, ins2).toMillis() + "ms");

        System.out.println("--------------------------------");

        LocalDate ld1 = LocalDate.of(2019, 3, 9);
        LocalDate ld2 = LocalDate.of(2019, 1, 1);

        Period period = Period.between(ld2, ld1);
        System.out.println(period.getYears() + "年"
                + period.getMonths() + "月"
                + period.getDays() + "天");
        System.out.println(period.toTotalMonths() + "月");
        System.out.println(Math.abs(ld1.toEpochDay() - ld2.toEpochDay()) + "天");
        // 2010-3-8 和 2019-3-9 算相隔一天
    }

    // 4. TemporalAdjuster：时间校正器
    @Test
    public void test4() {
        LocalDateTime ldt = LocalDateTime.now();
        System.out.println("当前日期：" + ldt);

        LocalDateTime ldt2 = ldt.withDayOfMonth(1);
        System.out.println("指定为1号：" + ldt2);

        LocalDateTime ldt3 = ldt.with(
                TemporalAdjusters.next(DayOfWeek.SUNDAY));
        System.out.println("下一个周日：" + ldt3);
    }

    // 自定义：下一个工作日
    @Test
    public void nextWorkDay() {
        LocalDateTime ldt = LocalDateTime.now()
                .with(x -> {
                    LocalDateTime now = (LocalDateTime) x;
                    DayOfWeek dow = now.getDayOfWeek();

                    if (dow.equals(DayOfWeek.FRIDAY)) {
                        return now.plusDays(3);
                    } else if (dow.equals(DayOfWeek.SATURDAY)) {
                        return now.plusDays(2);
                    } else {
                        return now.plusDays(1);
                    }
                });
        System.out.println(ldt);
    }

    // 5. DateTimeFormatter：解析和格式化日期或时间
    @Test
    public void test5() {
        LocalDateTime ldt = LocalDateTime.now();

        String strDate = DateTimeFormatter.ISO_LOCAL_DATE
                .format(ldt);
        System.out.println(strDate);

        strDate = DateTimeFormatter
                .ofPattern("yyyy年MM月dd日 HH:mm:ss E")
                .format(ldt);
        System.out.println(strDate);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate newLd = LocalDate.parse("2019/03/09", dtf);
        System.out.println(newLd);
    }

    // 6. ZonedDate、ZonedTime、ZonedDateTime：带时区的时间或日期
    @Test
    public void test6() {
        // 全部时区
        ZoneId.getAvailableZoneIds()
                .forEach(System.out::println);
    }

    // 设置时区
    @Test
    public void test7() {
        LocalDateTime ldt = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
        System.out.println(ldt);

        ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Tokyo"));
        System.out.println(zdt);

        assertEquals(1,
                zdt.getHour() - ldt.getHour());
    }
}
