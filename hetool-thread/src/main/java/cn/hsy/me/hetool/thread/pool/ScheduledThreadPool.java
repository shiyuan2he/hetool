package cn.hsy.me.hetool.thread.pool;

import cn.hsy.me.hetool.log.Log;
import cn.hsy.me.hetool.log.LogFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author heshiyuan
 *     创建一个固定长度的线程池，而且以延迟或定时的方式来执行任务，类似于Timer
 *     支持定时及周期性任务执行
 * Copyright (c) 2017 shiyuan4work@sina.com All rights reserved.
 */
public class ScheduledThreadPool {
    private static final Log log = LogFactory.get();
    volatile private static ScheduledThreadPool instance = null;
    volatile private ScheduledThreadPoolExecutor scheduledThreadPool ;
    private static final int poolSize = Runtime.getRuntime().availableProcessors() * 2 + 1 ;

    private ScheduledThreadPool(int poolSize){
        // 防止使用反射,克隆，根据序列化文件发序列化等方式实例化HungryManImplementation实例
        if(null != instance){
            log.error("【scheduledThreadPool】禁止实例化");
            throw new IllegalStateException("Error, Already initialized!") ;
        }
        // 线程池序号
        AtomicInteger poolNumber = new AtomicInteger(0);
        scheduledThreadPool = new ScheduledThreadPoolExecutor(
            poolSize,
            new ThreadFactory() {
                SecurityManager s = System.getSecurityManager();
                // 添加线程组
                private final ThreadGroup group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
                // 线程序号
                private final AtomicInteger threadNumber = new AtomicInteger(1);
                private final String namePrefix = "scheduledPool-" + poolNumber.incrementAndGet() + "-thread-";

                @Override
                public Thread newThread(Runnable runnable) {
                    log.info("【scheduledThreadPool】正在new一个线程，线程(ID:{})：", namePrefix + threadNumber.intValue());
                    // 创建新线程
                    Thread thread = new Thread(group, runnable, namePrefix + threadNumber.getAndIncrement());
                    // 线程池中禁止后台线程
                    if (thread.isDaemon()) {
                        thread.setDaemon(false);
                    }
                    return thread;
                }
            },
            // FIXME 线程池策略
            new ThreadPoolExecutor.AbortPolicy()
        );
    }

    /**
     * ScheduledThreadPool池子单例
     * @return
     */
    public static ScheduledThreadPool getInstance(){
        return getInstance(poolSize) ;
    }
    public static ScheduledThreadPool getInstance(int poolSize){
        ScheduledThreadPool result = instance;
        if(null == result){
            synchronized (ScheduledThreadPool.class){
                result = instance ;
                if(null == result){
                    result = instance = new ScheduledThreadPool(poolSize) ;
                }
            }
        }
        return result ;
    }
    /**
     * <p>获取线程池</p>
     * @return 可调度的线程池
     * @author heshiyuan
     */
    public ExecutorService getScheduledThreadPool() {
        return scheduledThreadPool;
    }
}
