package com.fubao.project;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Set;
import java.util.TimeZone;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class FubaoServerApplication {
    @PostConstruct
    void started() {
        // timezone 셋팅
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    public static void main(String[] args) {
        SpringApplication.run(FubaoServerApplication.class, args);
    }

}