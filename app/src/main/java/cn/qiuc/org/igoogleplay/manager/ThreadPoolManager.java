package cn.qiuc.org.igoogleplay.manager;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by admin on 2016/5/18.
 */
public class ThreadPoolManager {
    private static Object Lock = new Object();

    private int corePoolSize;
    private int maximumPoolize;
    private long keepAliveTime;
    private ThreadPoolExecutor executor;

    private static ThreadPoolManager mInstance;

    private ThreadPoolManager(){
        int poolSize = Runtime.getRuntime().availableProcessors()*2 +1;
        this.corePoolSize = poolSize;
        maximumPoolize = corePoolSize;
        keepAliveTime = 0;
    }

    public static ThreadPoolManager getInstance() {
        synchronized (Lock) {
            if (mInstance == null) {
                mInstance = new ThreadPoolManager();
            }
            return mInstance;
        }
    }

    public void execute(Runnable runnable){
        if (runnable==null)return;

        if (executor == null || executor.isShutdown()) {
            executor = new ThreadPoolExecutor(
                    corePoolSize,
                    maximumPoolize,
                    keepAliveTime,
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>(),
                    Executors.defaultThreadFactory(),
                    new ThreadPoolExecutor.AbortPolicy()
            );
        }

        executor.execute(runnable);

    }

    public void cancel(Runnable runnable) {
        if (runnable != null) {
            executor.remove(runnable);
        }

    }

}
