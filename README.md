#记录
<!-- /TOC -->
  - [1 设计模式](#1-设计模式)
    - [1.1 工厂模式](#11-工厂模式)
    - [1.2 代理模式](#12-代理模式)
    - [1.3 观察者模式](#13-观察者模式)
    - [1.4 单例模式](#14-单例模式)
<!-- /TOC -->


### 1 设计模式
#### 1.1 工厂模式
**工厂模式的好处：创建某个对象比较复杂，且不适合自定义创建，有时候还需要对调用方透明。**   
  
   
1：*创建复杂对象,特定参数（可能直接走的配置文件），下面是我们平时开发可能会用到很多的方法，线程池的创建，其实就是一个典型的工厂模式*
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
2：*对调用者隐蔽实际对象。food今天是Cook 创建的，可能因为Cook 做得不好，明天换给Cook2做了。这种就有避免我们换掉Cook后需要把所有调用create()的对象换掉。(虽然idea全局替换好用:dog:)。*
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
3：*通过不同的参数获取到不同的对象。比如说加密方式（md5,base64.....）,dubbo的通讯协议等等*
```
public class EncryptFactory {

    public static String encrypt(String txt, int type) {
        switch (type) {
            case 1:
                return Base64Encryption.encrypt(txt);
            case 2:
                return Md5Encryption.encrypt(txt);
            default:
                return "";
        }
    }
}
```
***占个坑[spring中的Factory]()***


#### 1.2 代理模式
**代理模式作用就是“增强”。**  
*先用JDK的实现方式来说下代理模式。JDK代理是基于接口的形式的，所以一般我们的代理类和目标类都会去实现这个接口。*
```
/**
 * 接口类
 */
public interface Start {

    void singe();
    
    void rap();
    
    void basketBall();

}


/**
 * 目标类
 */
public class KunKun implements Start {

    public void singe() {
        System.out.println("ji ni tai mei");
    }

    public void rap() {
    }

    public void basketBall() {
    }
}


/**
 * 代理类
 */
public class AgentHandler implements InvocationHandler {

    private Object target;


    AgentHandler(Object target) {
        this.target = target;
    }


    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object o = method.invoke(target, args);
        after();
        return o;
    }


    private void after() {
        System.out.println("6666");
    }


    /**
     * 通过目标类的接口（Start）去创建一个代理类，这个代理类是实现了Start 接口类。然后这个代理类调用Start的方法都会走被转发到这个AgentHandler的invoke方法中
     */
    public Object create() {
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }
}
```
***占坑[SpringAop中的代理，及Cglib和JDK代理区别]()***  
**记一次生产事故导致，其中就是代理类的出现了问题（rabbitMq日志记录）**


#### 1.3 观察者模式
**观察者模式：**
```

```
***占坑[ApplicationEvent中的观察者模式，源码实现]()***
#### 1.4 单例模式

