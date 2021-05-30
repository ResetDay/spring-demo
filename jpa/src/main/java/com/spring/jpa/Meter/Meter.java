package com.spring.jpa.Meter;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.*;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author ResetDay
 * @Date 2021-05-23 19:15
 */
@Configuration
@Component
public class Meter {
    @Resource
    MeterRegistry registry;


    @Scheduled(cron = "0/1 * * * * *")
    @Timed(value = "aws.scrape")
    public void get(){
        Timer.Sample sample = Timer.start(registry);
        Counter counter = registry.counter("mycounter");
        counter.increment();
        System.out.println(System.currentTimeMillis());
        Metrics.counter("order.count", "order.channel", "fsadf").increment();
       Timer timer = Metrics.timer("method.cost.time", "method.name", "fdg");
        timer.totalTime(TimeUnit.DAYS);
        Metrics.gauge("test", Tags.of("atete","fds"),1);
        registry.gauge("numberGauge", new AtomicInteger(0));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sample.stop(registry.timer("my.timer", "response","fsadf"));
        System.out.println(System.currentTimeMillis());
    }

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        System.out.println(111);
        return new TimedAspect(registry);
    }


}
