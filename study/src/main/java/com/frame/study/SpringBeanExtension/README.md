###Spring Bean 扩展点
对于框架设计而言，能够方便扩展是一个必要的条件。

####ApplicationContextAware
ApplicationContext是Spring的容器，管理Bean,控制Bean的生命周期。  
日常编码中用得比较常见的是通过ApplicationContext去获取需要使用的Bean。如何获取到ApplicationContext就是一个问题了。总不能去New吧。
所以Spring 提供了一个接口ApplicationContextAware去获取到我们需要的上下文ApplicationContext
````
public class Ex_ApplicationContext implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    /**
     * 通过bean 名称获取容器中的Bean
     */
    public Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }
}
````
举个例子，平时开发中也会遇见问题,同一给类中调用的方法被事务注解修饰方法，会发现事务是失效了。原因是通过@Transactional声明,
在调用的时候通过Spring Aop 产生一个代理类,然后对需要调用的方法进行事务的处理。
[@Transactional调用流程](<https://blog.csdn.net/m0_37609579/article/details/81503478>)
```
public class Ex_TestBean {


    public void doSomething(......) {
        saveBill();
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveBill(.....) {
            ..........
    }
}

```
在出现上面这种情况，需要在同一个类中调用带有事务声明的方法是后就可以通过ApplicationContext获取实际的Bean，
而非代理对象，再通过实际的Bean去调用代理Bean中被事务声明的方法。
```
@Compent
public class Ex_TestBean {

    @Autowired
    private Ex_ApplicationContext ex_ApplicationContext;

    public void doSomething(......) {
     Ex_TestBean ex_TestBean = ex_ApplicationContext.getBean("Ex_TestBean");
     ex_TestBean.saveBill();
    }


    @Transactional(rollbackFor = Exception.class)
    public void saveBill(.....) {
            ..........
    }
}
```


####BeanDefinitionRegistryPostProcessor
BeanDefinitionRegistryPostProcessor




