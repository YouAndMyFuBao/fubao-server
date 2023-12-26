package com.fubao.project.global.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class DateUtil {
    private static final int SEC = 60;
    private static final int MIN = 60;
    private static final int HOUR = 24;

    public String dateFormatToPost(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();
        long diffTime = createdAt.until(now, ChronoUnit.SECONDS);
        String msg;
        if ((diffTime /= SEC) < MIN) {
            msg = diffTime + "분 전";
        } else if ((diffTime /= MIN) < HOUR) {
            msg = (diffTime) + "시간 전";
        } else
            msg = (diffTime / HOUR) + "일 전";
        return msg;
    }
}
