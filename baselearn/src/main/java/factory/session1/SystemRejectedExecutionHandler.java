package factory.session1;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class SystemRejectedExecutionHandler implements RejectedExecutionHandler {


    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

    }
}
