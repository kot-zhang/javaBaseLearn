#记录
  - [1 设计模式](#1-设计模式)
    - [1.1 工厂模式](#11-工厂模式)
    - [1.2 代理模式](#12-代理模式)
    - [1.3 观察者模式](#13-观察者模式)
    - [1.4 单例模式](#14-单例模式)
<!-- /TOC -->


### 1 设计模式
#### 1.1 工厂模式
**工厂模式的好处：创建某个对象比较复杂，且不适合自定义创建，有时候还需要对调用方透明**   
[工厂模式解析](https://www.zhihu.com/question/42975862/answer/1239305317)  
1：创建复杂对象,特定参数（可能直接走的配置文件）
```
import java.util.concurrent.*;

public class SystemThreadPoolFactory {

    private static final int corePoolSize = 20;

    private static final int maximumPoolSize = 100;

    private static final long keepAliveTime = 0L;

    private static final TimeUnit unit = TimeUnit.SECONDS;

    /**
     * 自定义异常处理
     */
    private static final RejectedExecutionHandler handler = new SystemRejectedExecutionHandler();

    public static ThreadPoolExecutor createSystemThreadPool() {
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit,
                new LinkedBlockingQueue<Runnable>(), handler);
    }
}



import java.util.concurrent.ThreadPoolExecutor;
public class Client {

    public static void main(String[] args) {
        ThreadPoolExecutor executor = SystemThreadPoolFactory.createSystemThreadPool();
        executor.execute(new Runnable() {
            public void run() {
                System.out.println("------");
            }
        });
    }
}
```
2：对调用者隐蔽实际对象。food 今天是Cook 创建的，可能因为Cook 做得不好，明天换给Cook2做了。这种就有避免我们换掉Cook后需要把所有调用create()的对象换掉。(虽然idea全局替换好用:dog:)
```
public class FoodFactory {

    public static Food create() {
        Cook cook1 = new Cook();
        return cook1.create();

//        Cook2 cook2 = new Cook2();
//        return cook2.create();
    }
}

```
#### 1.2 代理模式

```

```
#### 1.3 观察者模式
```


```
#### 1.4 单例模式

