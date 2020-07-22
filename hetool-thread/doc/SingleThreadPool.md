# 单线程线程池

## 定义
 
`一个池子只有一个线程存活，所有从池子获取线程执行时需要排队获取线程使用权。如果这个线程异常结束，会创建另一个线程来代替能确保依照任务在队列中的顺序来串行执行`

## 说明

根据阿里巴巴Java编码规范：
 
 ```
线程池不允许使用Executors去创建，而是通过ThreadPoolExecutor的方式，这样的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险。 说明：Executors返回的线程池对象的弊端如下：
1）FixedThreadPool和SingleThreadPool:
  允许的请求队列长度为Integer.MAX_VALUE，可能会堆积大量的请求，从而导致OOM。
2）CachedThreadPool:
  允许的创建线程数量为Integer.MAX_VALUE，可能会创建大量的线程，从而导致OOM。
            
Positive example 1：
    //org.apache.commons.lang3.concurrent.BasicThreadFactory
    ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
        new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d").daemon(true).build());

Positive example 2：
    ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
        .setNameFormat("demo-pool-%d").build();

    //Common Thread Pool
    ExecutorService pool = new ThreadPoolExecutor(5, 200,
        0L, TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

    pool.execute(()-> System.out.println(Thread.currentThread().getName()));
    pool.shutdown();//gracefully shutdown
            
Positive example 3：
    <bean id="userThreadPool"
        class="org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor">
        <property name="corePoolSize" value="10" />
        <property name="maxPoolSize" value="100" />
        <property name="queueCapacity" value="2000" />

    <property name="threadFactory" value= "threadFactory"/>
        <property name="rejectedExecutionHandler">
            <ref local="rejectedExecutionHandler" />
        </property>
    </bean>
    //in code
    userThreadPool.execute(thread);
 ```

## 实现

基于以上原则，我们设计出了自己的单线程线程池实现伪代码

```java
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
            1, 
            1,
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
```

## 测试

| 序号 | 用例 | 期望 | 结果 |
|---|---|---|---|
| 1  | 多次getInstance，永远获取的是同一个池子 | 每两次返回的示例相等 | |
| 2  | 10个线程中的第5个抛出异常，看后面的线程能否继续执行 | 中间线程抛异常不影响后续线程执行，但线程id会变化| |
| 3  | 100个线程同时执行任务 |线程id不变并且按照线程放入的顺序执行 | |
| 4  | 超出线程等待队列的最大值 |抛出自定义的中止策略 | |
