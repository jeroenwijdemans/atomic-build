package atomic.server;

import java.util.concurrent.ThreadFactory;

/**
 * Class copied (and shortened) from Spring's org.springframework.util.CustomizableThreadCreator
 */
public class CustomerThreadFactory implements ThreadFactory {

    private Object threadCountMonitor = new Object();
    private int threadCount = 0;
    private final String threadName;

    public CustomerThreadFactory(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(null, runnable, nextThreadName());
        thread.setPriority(Thread.NORM_PRIORITY);
        thread.setDaemon(false);
        return thread;
    }

    protected String nextThreadName() {
        int threadNumber = 0;
        synchronized (this.threadCountMonitor) {
            this.threadCount++;
            threadNumber = this.threadCount;
        }
        return threadName + threadNumber;
    }

}
