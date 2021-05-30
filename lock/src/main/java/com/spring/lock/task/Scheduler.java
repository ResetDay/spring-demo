package com.spring.lock.task;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Author ResetDay
 * @Date 2021-05-30 11:46
 */
@Component
@Slf4j
public class Scheduler {

    @Resource
    private CuratorFramework client;
    private InterProcessMutex lock;
    private  String clientName = "test";
    @PostConstruct
    void start() {
        lock = new InterProcessMutex(client, "/lock");
    }

//    @Scheduled(cron = "0/1 * * * * ?")
//    void task() {
//        try {
//            if (!lock.acquire(1, TimeUnit.SECONDS)) {
//                log.info("can not get the lock");
//            } else {
//                log.info("lock start");
//                Thread.sleep(1000 * 5);
//                lock.release();
//                log.info("lock end");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    @Scheduled(cron = "0/1 * * * * ?")
//    void task1() {
//        try {
//            if(lock(this::work)){
//                log.info("lock end");
//            }else {
//                log.info("can not get the lock");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    boolean lock(Runnable  func) throws Exception{
        if ( lock.acquire(1, TimeUnit.SECONDS) )
        {
            try
            {
                // do some work inside of the critical section here
                log.info("lock start");
                func.run();
                return true;
            }
            finally
            {
                log.info("lock release");
                lock.release();
            }
        }
        return false;
    }

    void work() {
        log.info("I am worker");
        try {
            Thread.sleep(1000 * 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0/1 * * * * ?")
    void task1() {
        try {
            doWork(this::work,1,TimeUnit.SECONDS);
        } catch (IllegalStateException e) {
            log.info(e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void    doWork(Runnable  func,long time, TimeUnit unit) throws Exception
    {
        if ( !lock.acquire(time, unit) )
        {
            throw new IllegalStateException(clientName + " could not acquire the lock");
        }
        try
        {
            System.out.println(clientName + " has the lock");
            func.run();
        }
        finally
        {
            System.out.println(clientName + " releasing the lock");
            lock.release(); // always release the lock in a finally block
        }
    }


}
