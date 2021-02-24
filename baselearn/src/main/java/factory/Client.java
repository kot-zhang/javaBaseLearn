package factory;

import java.util.concurrent.ThreadPoolExecutor;

public class Client {

    public void test() {
        ThreadPoolExecutor executor = SystemThreadPoolFactory.createSystemThreadPool();
        executor.execute(new Runnable() {
            public void run() {
                //.......
            }
        });
    }

}
