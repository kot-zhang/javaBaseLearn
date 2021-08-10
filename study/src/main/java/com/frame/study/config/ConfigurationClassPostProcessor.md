###Spring源码解析之 ——ConfigurationClassPostProcessor

> 思考：  

(1) @Configuration,@Component,@Bean,@Import 注解的作用是什么? 什么时候被解析的。 
(2) @Configuration和@Component有什么区别？  
>这里就ConfigurationClassPostProcessor就上场了  

先看张图，这个就是ConfigurationClassPostProcessor处理的构建节点。ConfigurationClassPostProcessor实现了BeanDefinitionRegistryPostProcessor，
而BeanDefinitionRegistryPostProcessor继承了BeanFactoryPostProcessor，所以 ConfigurationClassPostProcessor 中需要重写postProcessBeanDefinitionRegistry()
方法和postProcessBeanFactory()方法。而ConfigurationClassPostProcessor类的作用就是通过这两个方法去实现的。
![ConfigurationClassPostProcessor-1](https://s3.bmp.ovh/imgs/2021/08/e5a2bcf6caf0ea24.png)
> ConfigurationClassPostProcessor做些什么？  

1：postProcessBeanDefinitionRegistry()将@Configuration,@Component,@Bean,@Import等注解的对象,解析为BeanDefinition,放在容器中，等待被解析为Spring bean；
这里会将@Configuration注解的类的BeanDefinition添加个属性，标记是FULL，还是LITE
2：postProcessBeanFactory()被标记FULL的BeanDefinition的beanClass替换为Cglib代理的class名


####processConfigBeanDefinitions()代码解析
```
  public void processConfigBeanDefinitions(BeanDefinitionRegistry registry) {
        List<BeanDefinitionHolder> configCandidates = new ArrayList<>();
        String[] candidateNames = registry.getBeanDefinitionNames();

        //.....省略


        // 将候选者进行排序
        configCandidates.sort((bd1, bd2) -> {
            int i1 = ConfigurationClassUtils.getOrder(bd1.getBeanDefinition());
            int i2 = ConfigurationClassUtils.getOrder(bd2.getBeanDefinition());
            return Integer.compare(i1, i2);
        });

        // Detect any custom bean name generation strategy supplied through the enclosing application context
        SingletonBeanRegistry sbr = null;
        if (registry instanceof SingletonBeanRegistry) {
            sbr = (SingletonBeanRegistry) registry;
            if (!this.localBeanNameGeneratorSet) {
                BeanNameGenerator generator = (BeanNameGenerator) sbr.getSingleton(
                        AnnotationConfigUtils.CONFIGURATION_BEAN_NAME_GENERATOR);
                if (generator != null) {
                    this.componentScanBeanNameGenerator = generator;
                    this.importBeanNameGenerator = generator;
                }
            }
        }

        if (this.environment == null) {
            this.environment = new StandardEnvironment();
        }

        //创建@Configuration解析器
        ConfigurationClassParser parser = new ConfigurationClassParser(
                this.metadataReaderFactory, this.problemReporter, this.environment,
                this.resourceLoader, this.componentScanBeanNameGenerator, registry);

        Set<BeanDefinitionHolder> candidates = new LinkedHashSet<>(configCandidates);
        Set<ConfigurationClass> alreadyParsed = new HashSet<>(configCandidates.size());
        do {
            StartupStep processConfig = this.applicationStartup.start("spring.context.config-classes.parse");
            // 解析配置类，在此处会解析配置类上的注解(ComponentScan扫描出的类，@Import注册的类，以及@Bean方法定义的类)
            // 注意：这一步只会将加了@Configuration注解以及通过@ComponentScan注解扫描的类才会加入到BeanDefinitionMap中
            // 通过其他注解(例如@Import、@Bean)的方式，在parse()方法这一步并不会将其解析为BeanDefinition放入到BeanDefinitionMap中，
            // 而是先解析成ConfigurationClass类
            // 真正放入到map中是在下面的this.reader.loadBeanDefinitions()方法中实现的
            parser.parse(candidates);
            parser.validate();

            Set<ConfigurationClass> configClasses = new LinkedHashSet<>(parser.getConfigurationClasses());
            configClasses.removeAll(alreadyParsed);

            // Read the model and create bean definitions based on its content
            if (this.reader == null) {
                this.reader = new ConfigurationClassBeanDefinitionReader(
                        registry, this.sourceExtractor, this.resourceLoader, this.environment,
                        this.importBeanNameGenerator, parser.getImportRegistry());
            }
            // 将上一步parser解析出的ConfigurationClass类加载成BeanDefinition
            // 实际上经过上一步的parse()后，解析出来的bean已经放入到BeanDefinition中了，但是由于这些bean可能会引入新的bean，例如实现了ImportBeanDefinitionRegistrar或者ImportSelector接口的bean，或者bean中存在被@Bean注解的方法
            // 因此需要执行一次loadBeanDefinition()，这样就会执行ImportBeanDefinitionRegistrar或者ImportSelector接口的方法或者@Bean注释的方法
            this.reader.loadBeanDefinitions(configClasses);
            alreadyParsed.addAll(configClasses);
            processConfig.tag("classCount", () -> String.valueOf(configClasses.size())).end();

            candidates.clear();
            //....省略代码
        }
        while (!candidates.isEmpty());
        //.....省略代码
    }
```
####