package com.example.myweb.util;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SchedulTest {

    //@Scheduled(fixedDelay = 30000)     //如果有定时任务，取消注释即可
    public void jobTest() {
        try {
            System.out.println("执行定时任务");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
