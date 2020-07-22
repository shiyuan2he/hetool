# 线程池

## 对比

使用new Thread执行任务的弊端：

- 每次new Thread新建对象性能差。
- 线程缺乏统一管理，可能无限制新建线程，相互之间竞争，及可能占用过多系统资源导致死机或oom。
- 缺乏更多功能，如定时执行、定期执行、线程中断。

线程池的优势：

- 重用存在的线程，减少对象创建、消亡的开销，性能佳。 
- 可有效控制最大并发线程数，提高系统资源的使用率，同时避免过多资源竞争，避免堵塞。
- 提供定时执行、定期执行、单线程、并发数控制等功能。 

web服务器，数据库服务器、文件服务器或邮件服务器等都接受来自整个互联网的"短时而量巨"的请求。
每当一个请求到达就创建一个新线程，然后在新线程中处理业务，那么这个服务器可能时常宕机，
导致服务不可用。

每个请求对应一个线程（thread-per-request）方法的不足之一是：为每个请求创建一个新线程的开销很大；为每个请求创建新线程的服务器在创建和销毁线程上花费的时间和消耗的系统资源要比花在处理实际的用户请求的时间和资源更多。

除了创建和销毁线程的开销之外，活动的线程也消耗系统资源。在一个 JVM 里创建太多的线程可能会导致系统由于过度消耗内存而用完内存或“切换过度”。为了防止资源不足，服务器应用程序需要一些办法来限制任何给定时刻处理的请求数目。

线程池为线程生命周期开销问题和资源不足问题提供了解决方案。通过对多个任务重用线程，线程创建的开销被分摊到了多个任务上。其好处是，因为在请求到达时线程已经存在，所以无意中也消除了线程创建所带来的延迟。这样，就可以立即为请求服务，使应用程序响应更快。而且，通过适当地调整线程池中的线程数目，也就是当请求的数目超过某个阈值时，就强制其它任何新到的请求一直等待，直到获得一个线程来处理为止，从而可以防止资源不足。

## ThreadPoolExecutor

熟悉阿里巴巴Java编码规范的同学都知道，使用Jdk自带的Executors创建线程池有如下弊端。

```
线程池不允许使用Executors去创建，而是通过ThreadPoolExecutor的方式，这样的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险。 
说明：Executors返回的线程池对象的弊端如下：
1）FixedThreadPool和SingleThreadPool:
  允许的请求队列长度为Integer.MAX_VALUE，可能会堆积大量的请求，从而导致OOM。
2）CachedThreadPool:
  允许的创建线程数量为Integer.MAX_VALUE，可能会创建大量的线程，从而导致OOM。
```

那么手动如何创建线程池呢？

查看源码不难发现Executors下创建的线程池底层都会使用 **new
ThreadPoolExecutor(...)**

那么我们来研究一下ThreadPoolExecutor，揭开其神秘面纱。

```java
    /**
     * Creates a new {@code ThreadPoolExecutor} with the given initial
     * parameters.
     * 利用给定的参数创建ThreadPoolExecutor
     *
     * @param corePoolSize the number of threads to keep in the pool, even
     *        if they are idle, unless {@code allowCoreThreadTimeOut} is set
     * 除非设置了allowCoreThreadTimeOut，否则要保持池中的线程数量为corePoolSize，包含闲置的线程
     *                     
     * @param maximumPoolSize the maximum number of threads to allow in the
     *        pool
     * 池子中允许的最大线程   
     *                        
     * @param keepAliveTime when the number of threads is greater than
     *        the core, this is the maximum time that excess idle threads
     *        will wait for new tasks before terminating.
     * 当线程池中的线程数大于corePoolSize时，此时空闲线程的最大等待时间，如果为0则表示不等待，直接销毁线程。
     * 比如说corePoolSize = 40， maximumPoolSize = 50
     * 其中只有45个线程任务在跑，相当于有5个空闲线程，这5个空闲线程不能让他一直在开着，
     * 因为线程的存在也会特别好资源的，所有就需要设置一个这个空闲线程的存活时间
     *                      
     * @param unit the time unit for the {@code keepAliveTime} argument
     * keepAliveTime的时间单位
     *             
     * @param workQueue the queue to use for holding tasks before they are
     *        executed.  This queue will hold only the {@code Runnable}
     *        tasks submitted by the {@code execute} method.
     * @param threadFactory the factory to use when the executor
     *        creates a new thread
     * @param handler the handler to use when execution is blocked
     *        because the thread bounds and queue capacities are reached
     * @throws IllegalArgumentException if one of the following holds:<br>
     *         {@code corePoolSize < 0}<br>
     *         {@code keepAliveTime < 0}<br>
     *         {@code maximumPoolSize <= 0}<br>
     *         {@code maximumPoolSize < corePoolSize}
     * @throws NullPointerException if {@code workQueue}
     *         or {@code threadFactory} or {@code handler} is null
     */
    public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory,
                              RejectedExecutionHandler handler) {
        if (corePoolSize < 0 ||
            maximumPoolSize <= 0 ||
            maximumPoolSize < corePoolSize ||
            keepAliveTime < 0)
            throw new IllegalArgumentException();
        if (workQueue == null || threadFactory == null || handler == null)
            throw new NullPointerException();
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.workQueue = workQueue;
        this.keepAliveTime = unit.toNanos(keepAliveTime);
        this.threadFactory = threadFactory;
        this.handler = handler;
    }
```
