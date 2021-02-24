package factory;

import java.util.concurrent.*;

public class SystemThreadPoolFactory {

    private static final int corePoolSize = 20;

    private static final int maximumPoolSize = 100;

    private static final long keepAliveTime = 0L;

    private static final TimeUnit unit = TimeUnit.SECONDS;

    private static final RejectedExecutionHandler handler = new SystemRejectedExecutionHandler();


    public static ThreadPoolExecutor createSystemThreadPool() {
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<Runnable>(), handler);
    }
}
