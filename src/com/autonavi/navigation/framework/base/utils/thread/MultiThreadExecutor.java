
package com.autonavi.navigation.framework.base.utils.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 采用JDK自带的线程池，可延时启动线程.
 * <p>
 * 简单地将平时使用的new
 * Thread(runnable).start()的做法改成MultiThreadExecutor.getInstance().schedule
 * (Runnable runnable)即可
 * <p>
 * 使用示例：
 * 
 * <pre>
 * MultiThreadExecutor.getInstance().schedule(Runnable command);
 * </pre>
 */
public class MultiThreadExecutor {

    private static MultiThreadExecutor instance = new MultiThreadExecutor();

    private final static int poolSize = 1;

    /**
     * 单例获取该实例
     * 
     * @return
     */
    public static synchronized MultiThreadExecutor getInstance() {
        if (instance == null) {
            return new MultiThreadExecutor();
        }
        return instance;
    }

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(poolSize);

    private MultiThreadExecutor() {
    }

    /**
     * 销毁线程池
     */
    public void dispose() {
        if (executor != null) {
            executor.shutdownNow();
            executor = null;
        }
    }

    /**
     * 创建并执行在给定延迟后启用的一次性操作。
     * 
     * @param <T>
     * @param callable
     * @param delay
     * @param unit
     * @return
     */
    public <T> ScheduledFuture<T> schedule(Callable<T> callable, long delay, TimeUnit unit) {
        if (executor == null) {
            executor = Executors.newScheduledThreadPool(poolSize);
        }
        return executor.schedule(callable, delay, unit);
    }

    /**
     * 创建并执行在给定延迟后启用的一次性操作。
     * 
     * @param r
     * @param delay
     * @param unit
     */
    public void schedule(Runnable r, long delay, TimeUnit unit) {
        if (executor == null) {
            executor = Executors.newScheduledThreadPool(poolSize);
        }
        executor.schedule(r, delay, unit);
    }

    public void execute(Runnable runnable) {
        if (executor == null) {
            executor = Executors.newScheduledThreadPool(poolSize);
        }
        executor.execute(runnable);
    }

    /**
     * 创建并执行一个在给定初始延迟后首次启用的定期操作，后续操作具有给定的周期； 也就是将在 initialDelay 后开始执行，然后在
     * initialDelay+period 后执行，接着在 initialDelay + 1 * period 后执行，依此类推。
     * 
     * @param command
     * @param initialDelay
     * @param delay
     * @param unit
     * @return
     */
    @SuppressWarnings("rawtypes")
    public ScheduledFuture scheduleWithFixedDelay(Runnable command, long initialDelay, long delay,
            TimeUnit unit) {
        if (executor == null) {
            executor = Executors.newScheduledThreadPool(poolSize);
        }
        return executor.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }

    public <T> Future<T> submit(Callable<T> callable) {
        if (executor == null) {
            executor = Executors.newScheduledThreadPool(poolSize);
        }
        return executor.submit(callable);
    }
}
