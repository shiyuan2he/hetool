package cn.hsy.me.hetool.thread.pool;

import cn.hsy.me.hetool.log.Log;
import cn.hsy.me.hetool.log.LogFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author heshiyuan
 *     创建单个工作者来执行任务，如果这个线程异常结束，会创建另一个线程来代替
 *     能确保依照任务在队列中的顺序来串行执行
 * Copyright (c) 2017 shiyuan4work@sina.com All rights reserved.
 */
public class SingleThreadPool {
    Log log = LogFactory.get();
    volatile private static SingleThreadPool instance = null;
    volatile private ThreadPoolExecutor singleThreadPool ;

    private SingleThreadPool(){
        // 防止使用反射,克隆，根据序列化文件发序列化等方式实例化SingleThreadPool实例
        if(null != instance){
            log.error("【singleThreadPool】禁止实例化");
            throw new IllegalStateException("Error, Already initialized!") ;
        }
        // 线程池序号
        AtomicInteger poolNumber = new AtomicInteger(0);
        singleThreadPool = new ThreadPoolExecutor(
            1, 1,
            0L, TimeUnit.MILLISECONDS,
            // 最长1024个等待线程队列
            new LinkedBlockingQueue<>(1024),
            new ThreadFactory() {
                SecurityManager s = System.getSecurityManager();
                // 添加线程组
                private final ThreadGroup group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
                // 线程序号
                private final AtomicInteger threadNumber = new AtomicInteger(1);
                private final String namePrefix = "singlePool-" + poolNumber.incrementAndGet() + "-thread-";

                @Override
                public Thread newThread(Runnable runnable) {
                    log.info("【singleThreadPool】正在new一个线程，线程(ID:{})：", namePrefix + threadNumber.intValue());
                    // 创建新线程
                    Thread thread = new Thread(group, runnable, namePrefix + threadNumber.getAndIncrement());
                    // 线程池中禁止后台线程
                    if (thread.isDaemon()) {
                        thread.setDaemon(false);
                    }
                    return thread;
                }
            },
            // 线程数 > 1 + 1  + 1024 执行下面的中止策略，抛出RejectedExecutionException异常
            new ThreadPoolExecutor.AbortPolicy()
        );
    }

    /**
     * SingleThreadPool池子单例
     * @return
     */
    public static SingleThreadPool getInstance(){
        SingleThreadPool result = instance;
        if(null == result){
            synchronized (SingleThreadPool.class){
                result = instance ;
                if(null == result){
                    result = instance = new SingleThreadPool() ;
                }
            }
        }
        return result ;
    }
    /**
     * 获取线程池
     * @return 单线程的线程池
     * @author heshiyuan
     */
    public ThreadPoolExecutor getSingleThreadPool() {
        return singleThreadPool;
    }
}
