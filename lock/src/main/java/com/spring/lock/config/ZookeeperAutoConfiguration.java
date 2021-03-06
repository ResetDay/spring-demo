package com.spring.lock.config;


import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @Author ResetDay
 * @Date 2021-05-30 12:10
 */
@ConditionalOnClass(CuratorFramework.class)
public class ZookeeperAutoConfiguration{


    @Value("${zookeeper.url}")
    private String url;

    private CuratorFramework client;

    @Bean
    @ConditionalOnMissingBean
    public CuratorFramework getClient(){
        client = CuratorFrameworkFactory
                .builder()
                .connectString(url)
                .connectionTimeoutMs(60000)
                .sessionTimeoutMs(5000) // session存活时间
                .retryPolicy(new ExponentialBackoffRetry(10000,3))
                .build();

        client.start();

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                client.close();
            }
        }));

        return client;
    }

}
