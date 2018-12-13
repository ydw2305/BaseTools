package cn.ydw.www.toolslib.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * =====================================
 * 作    者: 杨德望
 * 版    本：${VERSION_CODE}.
 * 创建日期：2018/1/12.
 * 描    述：线程处理工具类 <br/> FIXME 目前为测试阶段, 主要为载入图片而使用
 * =====================================
 */
public class ThreadPoolUtil extends ThreadPoolExecutor {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE = 1;
    private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<>(128);


    public ThreadPoolUtil(){
        this(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, sPoolWorkQueue);
    }


    public ThreadPoolUtil(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                         TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }
    /**
     * 新建线程池,  注: 后面两个参数无效, 使用的是默认值, 可以传空
     * @param corePoolSize 线程池中核心线程的数量
     * @param maximumPoolSize 线程池中最大线程数量
     * @param keepAliveTime 非核心线程的超时时长，当系统中非核心线程闲置时间超过keepAliveTime之<br/>
     *                      后，则会被回收。如果ThreadPoolExecutor的allowCoreThreadTimeOut属性<br/>
     *                      设置为true，则该参数也表示核心线程的超时时长
     * @param unit 第三个参数的单位，有纳秒、微秒、毫秒、秒、分、时、天等
     * @param workQueue 线程池中的任务队列，该队列主要用来存储已经被提交但是尚未执行的任务。存储<br/>
     *                  在这里的任务是由ThreadPoolExecutor的execute方法提交来的。
     * @param threadFactory 为线程池提供创建新线程的功能，这个我们一般使用默认即可
     * @param handler 拒绝策略，当线程无法执行新任务时（一般是由于线程池中的线程数量已经达到最大<br/>
     *                数或者线程池关闭导致的），默认情况下，当线程池无法处理新线程时，会抛出<br/>
     */
    public ThreadPoolUtil(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                         TimeUnit unit, BlockingQueue<Runnable> workQueue,
                         ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        // " 开始执行任务！"
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        // " 任务执行结束！"
    }

    @Override
    protected void terminated() {
        super.terminated();
        //当调用shutDown()或者shutDownNow()时会触发该方法
        // " 线程池关闭！"
    }
}
