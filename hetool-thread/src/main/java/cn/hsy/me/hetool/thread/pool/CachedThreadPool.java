package cn.hsy.me.hetool.thread.pool;

import cn.hsy.me.hetool.log.Log;
import cn.hsy.me.hetool.log.LogFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author heshiyuan
 *     创建一个可缓存的线程池，如果线程池的当前规模超过了处理需求时，那么将回收空闲的线程
 *     而当需求增加时，则可以添加新的线程，线程池的规模不存在任何限制
 *     如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
 * Copyright (c) 2017 shiyuan4work@sina.com All rights reserved.
 */
public class CachedThreadPool {
    private static final Log log = LogFactory.get();

    volatile private static CachedThreadPool instance = null;
    volatile private ThreadPoolExecutor cachedThreadPool ;

    private CachedThreadPool(){
        // 防止使用反射,克隆，根据序列化文件发序列化等方式实例化HungryManImplementation实例
        if(null != instance){
            log.error("【cachedThreadPool】禁止实例化");
            throw new IllegalStateException("Error, Already initialized!") ;
        }
        log.info("正在初始化CachedThreadPool的线程池：");
        // 线程池序号
        AtomicInteger poolNumber = new AtomicInteger(0);
        this.cachedThreadPool = new ThreadPoolExecutor(
            0,
            Integer.MAX_VALUE,
            60L,
            TimeUnit.SECONDS,
            new SynchronousQueue<>(),
            new ThreadFactory() {
                SecurityManager s = System.getSecurityManager();
                // 添加线程组
                private final ThreadGroup group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
                // 线程序号
                private final AtomicInteger threadNumber = new AtomicInteger(1);
                private final String namePrefix = "cachedPool-" + poolNumber.incrementAndGet() + "-thread-";

                @Override
                public Thread newThread(Runnable runnable) {
                    log.info("【cachedThreadPool】正在new一个线程，线程(ID:{})：", namePrefix + threadNumber.intValue());
                    // 创建新线程
                    Thread thread = new Thread(group, runnable,namePrefix + threadNumber.getAndIncrement());
                    // 线程池中禁止后台线程
                    if (thread.isDaemon()) {thread.setDaemon(false);}
                    return thread ;
                }
            }
        );
    }

    /**
     * CachedThreadPool池子单例
     * @return
     */
    public static CachedThreadPool getInstance(){
        CachedThreadPool result = instance;
        if(null == result){
            synchronized (CachedThreadPool.class){
                result = instance ;
                if(null == result){
                    result = instance = new CachedThreadPool() ;
                }
            }
        }
        return result ;
    }
    /**
     * 获取不定长可缓存重复执行的线程池
     * @return 可缓存的线程池
     * @author heshiyuan
     */
    public ExecutorService getCachedThreadPool() {
        return cachedThreadPool;
    }
}
