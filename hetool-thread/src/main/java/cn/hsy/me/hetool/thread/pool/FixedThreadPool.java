package cn.hsy.me.hetool.thread.pool;
import cn.hsy.me.hetool.log.Log;
import cn.hsy.me.hetool.log.LogFactory;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author heshiyuan
 *     a.闯将一个固定长度的线程池，每当提交一个任务是就创建一个线程
 *       直到达到线程池的最大线程，这时线程池的规模将不再发生变化，如果
 *       某个线程由于发生了未预期的Exception而结束，那么线程池会补充一个新线程
 *     b.可重用固定线程数的线程池
 *       以共享的无界队列方式来运行这些线程。在任意点，
 *       在大多数 nThreads 线程会处于处理任务的活动状态。
 *       如果在所有线程处于活动状态时提交附加任务，则在有可用线程之前，
 *       附加任务将在队列中等待。如果在关闭前的执行期间由于失败而导致任何线程终止，
 *       那么一个新线程将代替它执行后续的任务（如果需要）。在某个线程被显式地关闭之前，
 *       池中的线程将一直存在。
 *      c.创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待
 *      </p>
 * Copyright (c) 2017 shiyuan4work@sina.com All rights reserved.
 */
public class FixedThreadPool {
    private static final Log log = LogFactory.get();
    volatile private static FixedThreadPool instance ;
    private static final int poolSize = Runtime.getRuntime().availableProcessors() * 2 + 1 ;
    volatile private static ThreadPoolExecutor fixedThreadPool;


    public ThreadPoolExecutor getFixedThreadPool(){
        return fixedThreadPool;
    }

    private FixedThreadPool(int threadPoolNum, String threadName) {
        // 防止使用反射,克隆，根据序列化文件发序列化等方式实例化HungryManImplementation实例
        if(null != instance){
            log.error("【fixedThreadPool】禁止实例化");
            throw new IllegalStateException("Error, Already initialized!") ;
        }
        log.info("【fixedThreadPool】正在初始化一个({})线程数的线程池", threadPoolNum);
        // 线程池序号
        AtomicInteger poolNumber = new AtomicInteger(0);
        fixedThreadPool = new ThreadPoolExecutor(
            threadPoolNum,
            threadPoolNum,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(),
            new ThreadFactory() {
                SecurityManager s = System.getSecurityManager();
                // 添加线程组
                private final ThreadGroup group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
                // 线程序号
                private final AtomicInteger threadNumber = new AtomicInteger(1);
                private final String namePrefix = "fixedPool-" + poolNumber.incrementAndGet() + "-thread-";

                @Override
                public Thread newThread(Runnable runnable) {
                    log.info("【fixedThreadPool】正在new一个线程，线程(ID:{})：", namePrefix + threadNumber.intValue());
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
     * 双锁保证线程池对象单例
     * 对外提供默认线程数的线程池
     * @return FixedThreadPool
     */
    public static FixedThreadPool getInstance(){
        return getInstance(poolSize);
    }
    public static FixedThreadPool getInstance(int poolSize){
        return getInstance(poolSize, null);
    }
    public static FixedThreadPool getInstance(int poolSize, String threadName){
        FixedThreadPool result = instance ;
        // 如果是null就锁类并实例化
        if(null == result){
            /**
             * 锁类并再次判断实例是否为空，否则new
             */
            synchronized (FixedThreadPool.class){
                result = instance ;
                if(null == result){
                    result = instance = new FixedThreadPool(poolSize, threadName);
                }
            }
        }
        return result ;
    }
}
