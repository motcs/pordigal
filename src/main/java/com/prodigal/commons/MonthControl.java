package com.prodigal.commons;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author <a href="https://github.com/motcs">motcs</a>
 * @since 2023-07-13 星期四
 */
@Data
@NoArgsConstructor
public class MonthControl {

    public static LocalDateTime startDate(Integer year, Integer month) {
        LocalDateTime startDate = LocalDateTime.of(year, 1, 1, 0, 0);
        if (!ObjectUtils.isEmpty(month) && month > 0 && month < 13) {
            startDate = LocalDateTime.of(year, month, 1, 0, 0);
        }
        return startDate;
    }

    public static LocalDateTime endDate(Integer year, Integer month) {
        LocalDateTime startDate = startDate(year, month);
        LocalDateTime endDate = startDate.plusYears(1).minusDays(1);
        if (!ObjectUtils.isEmpty(month) && month > 0 && month < 13) {
            endDate = startDate.plusMonths(1).minusDays(1);
        }
        return endDate;
    }

    public static long LocalDateTimeToSecond(LocalDateTime time) {
        //获取当前时间减去获取当前议政开始时间
        return Duration.between(LocalDateTime.now(), time).toSeconds();
    }

}