#记录
<!-- /TOC -->
  - [1 设计模式](#1-设计模式)
    - [1.1 工厂模式](#11-工厂模式)
    - [1.2 代理模式](#12-代理模式)
    - [1.3 观察者模式](#13-观察者模式)
    - [1.4 单例模式](#14-单例模式)
    - [1.5 创建者模式](#15-创建者模式)
    - [1.6 原型模式](#16-原型模式)
    - [1.7 适配器模式](#17-适配器模式)
    - [1.8 策略模式](#18-策略模式)
    - [1.9 装饰模式](#19-装饰模式)
  - [2 Java基础](#2-java基础)
    - [2.1 反射](#21-反射)
    - [2.2 static关键字](#22-static关键字)
    - [2.3 内加载顺序ClassLoader](#23-内加载顺序ClassLoader)
    - [2.4 泛型](#24-泛型)
    - [2.5 拷贝(浅拷贝,深拷贝)。](#25-拷贝)
    - [2.6 关键字final,this,super](#26-关键字final,this,super)
    - [2.7 内部类](#27-内部类)
  - [3 集合](#3-集合)
  - [4 线程](#4-线程)
  - [5 文件与io流](#5-文件与io流)
  - [6 jdbc数据库操作](#6-jdbc数据库操作)
  
    
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
**观察者模式：定义对象间的一种一对多依赖关系，使得每当一个对象状态发生改变时，其相关依赖对象皆得到通知并被自动更新。**
```
/**
 * 观察者接口
 */
public interface Fan {


    void update();
}


/**
 * 被观察者
 */
public class KunKun {

    private List<Fan> fans;

    public KunKun() {
        fans = new ArrayList<Fan>();
    }

    public void addFan(Fan fan) {
        fans.add(fan);
    }

    public void notifyFan() {
        for (Fan fan : fans) {
            fan.update();
        }
    }
}

/**
 * 观察者
 */
public class RapFan implements Fan {


    public void update() {
        System.out.println("Rap : UNKUN 出来了！");
    }
}


/**
 * 观察者
 */
public class SingFan implements Fan {

    public void update() {
        System.out.println("Sing ：KUNKUN 出来了！");
    }
}


 /**
  * 测试观察者模式
  */
public static void main(String[] args) {
    KunKun kunKun = new KunKun();
    kunKun.addFan(new RapFan());
    kunKun.addFan(new SingFan());
    kunKun.notifyFan();
}


```
JDK也提供了工具方法：Observable 被观察者类 和  Observer 观察者接口 。  
结合实际业务，观察者模式带给我们的是模块的解耦。比如客户首次注册，会奖励一些积分，下次需要再奖励一个徽章。这种业务类型就可以用观察者模式。
从上面代码看，"被观察者"需要维护"观察者"，而且在通知"观察者"是同步调用，这种方式其实对系统也是不友好的。这个时候***ApplicationEvent***就可以上场了。  
***占坑[ApplicationEvent中的观察者模式，源码实现]()***
#### 1.4 单例模式
***单例模式 ：防止多次创建作用相同的对象，一次创建多次使用，避免多次创建导致不必要的内存开销。***  
单例模式常见的有两种实现方法：懒汉模式，饿汉模式。  
懒汉模式：调用的时候去创建，创建的时候要考虑到线程安全的问题。  
饿汉模式：类加载的时候去创建，本身是线程安全的。
```
/**
 * 懒加载
 */
public class LazySingleton {

    private LazySingleton() {

    }

    private static volatile LazySingleton instance;

    public static LazySingleton getInstance() {
        if (instance == null) {
            synchronized (LazySingleton.class) {
                if (instance == null) {
                    instance = new LazySingleton();
                }
            }
        }
        return instance;
    }
}


/**
 * 饿汉模式
 */
public class HungrySingleton {

    private final static HungrySingleton instance = new HungrySingleton();

    public static HungrySingleton getInstance() {
        return instance;
    }
}

```
***占坑[Spring中bean创建的单例模式]()***  
***占坑[volatile关键字]()***  
***占坑[static关键字]()***
#### 1.5 创建者模式
***创建者模式：解决了构造函数含有较多了参数，让创建的对象必然是属于可用状态***
```
public class Person {
    /**
     * 必须属性
     */
    private final String name;
    /**
     * 必须属性
     */
    private final Integer age;

    private final String address;

    private final String phone;

    //final成员的要求是最晚构造函数得初始化，否则编译报错

    public Person(String name, Integer age) {
        this(name, age, "", "");
    }

    public Person(String name, Integer age, String address) {
        this(name, age, address, "");
    }

    public Person(String name, Integer age, String address, String phone) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

}
```
当Person类的成员属性为不可变的final的。参数少的情况下可以勉强维持，一旦参数多了可读性和维护性都比较差了。当我想给name,age,phone赋值构造方法就满足不了了。  
我们可以看看我们的第二种方法，给javaBean设置一个空的构造函数，提供setxx方法去赋值对象属性
```
public class Person2 {
    /**
     * 必须属性
     */
    private String name;
    /**
     * 必须属性
     */
    private Integer age;

    private String address;

    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
```
这种也会存在一些问题"对象会产生不一致的状态",你想要将setxx方法调用完成后，对象才属于valid状态。
当调用了部分setxx方法时候，对象实际没有被创建完成，对象被调用，这个时候就会存在问题。  
接下来就是通过builder模式解决上面的问题了。
```
public class Person3 {

    /**
     * 必须属性
     */
    private final String name;
    /**
     * 必须属性
     */
    private final Integer age;

    private String address;

    private String phone;

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    private Person3(PersonBuilder personBuilder) {
        this.address = personBuilder.address;
        this.age = personBuilder.age;
        this.name = personBuilder.name;
        this.phone = personBuilder.phone;
    }

    public static class PersonBuilder {
        /**
         * 必须属性
         */
        private final String name;
        /**
         * 必须属性
         */
        private final Integer age;

        private String address;

        private String phone;

        /**
         * 构造方法传必填函数
         */
        public PersonBuilder(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        public PersonBuilder address(String address) {
            this.address = address;
            return this;
        }

        public PersonBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Person3 build() {
            return new Person3(this);
        }
    }
}


public class Client {
    public static void main(String[] args) {
        Person3 person3 = new Person3.PersonBuilder("zhang", 12).address("武汉").phone("159xxxxxx795").build();
    }
}
```

[参考大宽宽回答：Builder](https://www.zhihu.com/question/326142180/answer/697172067)
#### 1.6 原型模式
```

```
#### 1.7 适配器模式
```

```

#### 1.8 策略模式
```

```

#### 1.9 装饰模式
```

```