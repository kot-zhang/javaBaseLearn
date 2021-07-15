##Spring加载流程
这篇文章我们大概的了解下Spring加载流程。

###基础概念
####1.bean
bean:说白了就是把一个个实体对象交由spring去创建和管理

####2.beanDefinition
beanDefinition是bean的定义。有点像spring对象的"Class"。  
![spring-1](https://i.loli.net/2021/07/14/FrInphlW3g7Xf4M.png)  

####3.BeanFactory,ApplicationContext
BeanFactory是一个基础的容器。ApplicationContext是BeanFactory的一个子接口，功能跟强大，是高级的容器。

####4.BeanFactoryPostProcessor ,BeanDefinitionRegistryPostProcessor
两者都是后置处理器。是Spring很重要的拓展点。这两个扩展点的触发时机，都是在BeanDefinition加载之后，Bean实例化之前。
官方建议是BeanDefinitionRegistryPostProcessor用来添加BeanDefinition,BeanFactoryPostProcessor用于修改
BeanDefinition。BeanDefinitionRegistryPostProcessor优先于BeanFactoryPostProcessor执行。

####5.BeanPostProcessor
BeanPostProcessor是bean的后置处理器，也是Spring重要的拓展点。它的触发时机是Bean的初始化。postProcessBeforeInitialization初始化前处理，
postProcessAfterInitialization初始化后处理。

####6.InitializingBean
InitializingBean 实现了 afterPropertiesSet方法就会在bean初始化时被调用

####7.BeanFactory ,FactoryBean
为什么要把这两个放在一起，因为他们长得像，除了像，没有其他半毛钱关系。BeanFactory时基础容器，FactoryBean是一个特殊的bean，是一个工厂bean。
也是Spring的一个重要的拓展点，通过这个类的getObject()方法创建一个实列对象。很多插件其实就是用的这个，例如mybatis的Mapper是一个接口，没有具体的
实现却可以操作数据库，其实时FactoryBean给它产生了一个代理类。


###加载基本流程
```
public ConfigurableApplicationContext run(String... args) {
   
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    ConfigurableApplicationContext context = null;
    Collection<SpringBootExceptionReporter> exceptionReporters = new ArrayList<>();

    configureHeadlessProperty();

    // 第一步：获取并启动监听器
    SpringApplicationRunListeners listeners = getRunListeners(args);
    listeners.starting();
    
    try {
        ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);

        // 第二步：根据SpringApplicationRunListeners以及参数来准备环境
        ConfigurableEnvironment environment = prepareEnvironment(listeners,applicationArguments);
        configureIgnoreBeanInfo(environment);

        // 准备Banner打印器
        Banner printedBanner = printBanner(environment);

        // 第三步：创建Spring容器
        context = createApplicationContext();

        exceptionReporters = getSpringFactoriesInstances(
                SpringBootExceptionReporter.class,
                new Class[] { ConfigurableApplicationContext.class }, context);

        // 第四步：Spring容器前置处理
        prepareContext(context, environment, listeners, applicationArguments,printedBanner);

        // 第五步：刷新容器
        refreshContext(context);
　　　　 // 第六步：Spring容器后置处理
        afterRefresh(context, applicationArguments);

  　　　 // 第七步：发出结束执行的事件
        listeners.started(context);
        // 第八步：执行Runners
        this.callRunners(context, applicationArguments);
        stopWatch.stop();
        // 返回容器
        return context;
    }
    catch (Throwable ex) {
        handleRunFailure(context, listeners, exceptionReporters, ex);
        throw new IllegalStateException(ex);
    }
}
```
createApplicationContext()这里创建容器，添加一些特定的后置处理器。其中就包括了我们后面要说的ConfigurationClassPostProcessor这个后置处理器，
也可以说它是spring加载中最重要的后置处理器。
refreshContext(context)这个方法是我们需要重点关注的，这个方法里面就是将我们的对象交由spring容器去管理，核心方法。
```
public void refresh() throws BeansException, IllegalStateException {
		synchronized (this.startupShutdownMonitor) {
			StartupStep contextRefresh = this.applicationStartup.start("spring.context.refresh");

			// Prepare this context for refreshing.
			prepareRefresh();

			// Tell the subclass to refresh the internal bean factory.
			ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

			// Prepare the bean factory for use in this context.
			prepareBeanFactory(beanFactory);

			try {
				// Allows post-processing of the bean factory in context subclasses.
				postProcessBeanFactory(beanFactory);

				StartupStep beanPostProcess = this.applicationStartup.start("spring.context.beans.post-process");
			    /**
                 * 激活各种BeanFactory处理器,包括BeanDefinitionRegistryBeanFactoryPostProcessor和普通的BeanFactoryPostProcessor
                 * 执行对应的postProcessBeanDefinitionRegistry方法 和  postProcessBeanFactory方法
                 */
				invokeBeanFactoryPostProcessors(beanFactory);

				/**
                  * 注册拦截Bean创建的Bean处理器，即注册BeanPostProcessor，不是BeanFactoryPostProcessor，注意两者的区别
                  * 注意，这里仅仅是注册，并不会执行对应的方法，将在bean的实例化时执行对应的方法
                  */
				registerBeanPostProcessors(beanFactory);
				beanPostProcess.end();

				//国际化
				initMessageSource();

				// 初始化事件和监听
				initApplicationEventMulticaster();

				//拓展点
				onRefresh();

				//注册事件监听
				registerListeners();

				//初始化bean
				finishBeanFactoryInitialization(beanFactory);

				//发布容器初始化完成消息
				finishRefresh();
			}

			catch (BeansException ex) {
				if (logger.isWarnEnabled()) {
					logger.warn("Exception encountered during context initialization - " +
							"cancelling refresh attempt: " + ex);
				}

				// Destroy already created singletons to avoid dangling resources.
				destroyBeans();

				// Reset 'active' flag.
				cancelRefresh(ex);

				// Propagate exception to caller.
				throw ex;
			}

			finally {
				// Reset common introspection caches in Spring's core, since we
				// might not ever need metadata for singleton beans anymore...
				resetCommonCaches();
				contextRefresh.end();
			}
		}
```
invokeBeanFactoryPostProcessors(beanFactory),这个方法会激活BeanDefinitionRegistryPostProcessor的后置处理器，其中就有就包括
ConfigurationClassPostProcessor这个类，它会将符合条件的bean转化为beanDefinition。
finishBeanFactoryInitialization(beanFactory),这个方法将会根据BeanDefinition去初始化bean,这一步完成后我们的bean就由spring接管了。

