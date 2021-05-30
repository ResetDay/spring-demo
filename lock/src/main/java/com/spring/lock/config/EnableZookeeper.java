package com.spring.lock.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author ResetDay
 * @Date 2021-05-30 12:09
 */

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Import(ZookeeperAutoConfiguration.class)
    public @interface EnableZookeeper {

    }
