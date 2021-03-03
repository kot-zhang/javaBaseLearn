package designpattern.factory.session1;

import java.util.concurrent.ThreadPoolExecutor;

public class Client {

    public static void main(String[] args) {
        test();

    }

    private static void test() {
        ThreadPoolExecutor executor = SystemThreadPoolFactory.createSystemThreadPool();
        executor.execute(new Runnable() {
            public void run() {
                System.out.println("------");
            }
        });
    }



}
