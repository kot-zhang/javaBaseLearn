###Spring源码解析之 ——ConfigurationClassPostProcessor

> 思考：  

(1) @Configuration,@Component,@Bean,@Import 注解的作用是什么? 什么时候被解析的。 
>这里就ConfigurationClassPostProcessor就上场了  

先看张图，这个就是ConfigurationClassPostProcessor处理的构建节点。ConfigurationClassPostProcessor实现了BeanDefinitionRegistryPostProcessor，
而BeanDefinitionRegistryPostProcessor继承了BeanFactoryPostProcessor，所以 ConfigurationClassPostProcessor 中需要重写postProcessBeanDefinitionRegistry()
方法和postProcessBeanFactory()方法。而ConfigurationClassPostProcessor类的作用就是通过这两个方法去实现的。
![ConfigurationClassPostProcessor-1](https://s3.bmp.ovh/imgs/2021/08/e5a2bcf6caf0ea24.png)
> ConfigurationClassPostProcessor做些什么？  

1：postProcessBeanDefinitionRegistry()将@Configuration,@Component,@Bean,@Import等注解的对象,解析为BeanDefinition,放在容器中，等待被解析为Spring bean；
这里会将@Configuration注解的类的BeanDefinition添加个属性，标记是FULL，还是LITE
2：postProcessBeanFactory()被标记FULL的BeanDefinition的beanClass替换为Cglib代理的class名


###processConfigBeanDefinitions()代码解析
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
####1.parser.parse()
根据BeanDefinition类型的不同，调用parse()不同的重载方法。实际是调用ConfigurationClassParser中的processConfigurationClass()方法
parse()具体代码
```
	public void parse(Set<BeanDefinitionHolder> configCandidates) {
		for (BeanDefinitionHolder holder : configCandidates) {
			BeanDefinition bd = holder.getBeanDefinition();
			try {
				if (bd instanceof AnnotatedBeanDefinition) {
					parse(((AnnotatedBeanDefinition) bd).getMetadata(), holder.getBeanName());
				}
				else if (bd instanceof AbstractBeanDefinition && ((AbstractBeanDefinition) bd).hasBeanClass()) {
					parse(((AbstractBeanDefinition) bd).getBeanClass(), holder.getBeanName());
				}
				else {
					parse(bd.getBeanClassName(), holder.getBeanName());
				}
			}
			catch (BeanDefinitionStoreException ex) {
				throw ex;
			}
			catch (Throwable ex) {
				throw new BeanDefinitionStoreException(
						"Failed to parse configuration class [" + bd.getBeanClassName() + "]", ex);
			}
		}

		this.deferredImportSelectorHandler.process();
	}
```
processConfigurationClass()方法中实际解析的方法为doProcessConfigurationClass()方法
```
	protected void processConfigurationClass(ConfigurationClass configClass, Predicate<String> filter) throws IOException {
        //.....省略代码
        
		SourceClass sourceClass = asSourceClass(configClass, filter);
		do {
			sourceClass = doProcessConfigurationClass(configClass, sourceClass, filter);
		}
		while (sourceClass != null);

		this.configurationClasses.put(configClass, configClass);
	}
```
doProcessConfigurationClass()方法中就开始了Spring一些注解的解析
```
	protected final SourceClass doProcessConfigurationClass(
                ConfigurationClass configClass, SourceClass sourceClass, Predicate<String> filter)
                throws IOException {
    
            //解析@Component注解的类
            if (configClass.getMetadata().isAnnotated(Component.class.getName())) {
                //遍历被注解类的内部类，这个也是为什么一些内部类一样会被spring获取到的原因
                processMemberClasses(configClass, sourceClass, filter);
            }
    
            // 解析@PropertySource注解
            for (AnnotationAttributes propertySource : AnnotationConfigUtils.attributesForRepeatable(
                    sourceClass.getMetadata(), PropertySources.class,
                    org.springframework.context.annotation.PropertySource.class)) {
                if (this.environment instanceof ConfigurableEnvironment) {
                    processPropertySource(propertySource);
                } else {
                    logger.info("Ignoring @PropertySource annotation on [" + sourceClass.getMetadata().getClassName() +
                            "]. Reason: Environment must implement ConfigurableEnvironment");
                }
            }
    
            // 解析@ComponentScans , @ComponentScan注解
            Set<AnnotationAttributes> componentScans = AnnotationConfigUtils.attributesForRepeatable(
                    sourceClass.getMetadata(), ComponentScans.class, ComponentScan.class);
            if (!componentScans.isEmpty() &&
                    !this.conditionEvaluator.shouldSkip(sourceClass.getMetadata(), ConfigurationPhase.REGISTER_BEAN)) {
                for (AnnotationAttributes componentScan : componentScans) {
                    //解析@ComponentScans , @ComponentScan扫描包下的@Component,@Service,@Controller,@Repository注解。
                    //将扫描出来的类转为bd添加到beanDefinitionMap中
                    Set<BeanDefinitionHolder> scannedBeanDefinitions =
                            this.componentScanParser.parse(componentScan, sourceClass.getMetadata().getClassName());
                    //对@Configuration注解进行特殊处理
                    for (BeanDefinitionHolder holder : scannedBeanDefinitions) {
                        BeanDefinition bdCand = holder.getBeanDefinition().getOriginatingBeanDefinition();
                        if (bdCand == null) {
                            bdCand = holder.getBeanDefinition();
                        }
                        //checkConfigurationClassCandidate给@Configuration注解的类打上FULL标记
                        if (ConfigurationClassUtils.checkConfigurationClassCandidate(bdCand, this.metadataReaderFactory)) {
                            parse(bdCand.getBeanClassName(), holder.getBeanName());
                        }
                    }
                }
            }
    
            //解析Import注解注册的bean，这一步只会将import注册的bean变为ConfigurationClass,不会变成BeanDefinition
            processImports(configClass, sourceClass, getImports(sourceClass), filter, true);
    
            //引入配置文件
            AnnotationAttributes importResource =
                    AnnotationConfigUtils.attributesFor(sourceClass.getMetadata(), ImportResource.class);
            if (importResource != null) {
                String[] resources = importResource.getStringArray("locations");
                Class<? extends BeanDefinitionReader> readerClass = importResource.getClass("reader");
                for (String resource : resources) {
                    String resolvedResource = this.environment.resolveRequiredPlaceholders(resource);
                    configClass.addImportedResource(resolvedResource, readerClass);
                }
            }
    
            // 解析@Bean注解
            Set<MethodMetadata> beanMethods = retrieveBeanMethodMetadata(sourceClass);
            for (MethodMetadata methodMetadata : beanMethods) {
                configClass.addBeanMethod(new BeanMethod(methodMetadata, configClass));
            }
            //......省略代码
            // No superclass -> processing is complete
            return null;
        }
```
componentScanParser.parse()在扫描时会通过includeFilters和excludeFilters条件去决定具体扫描的bean,但是实际includeFilters中只有
@Component注解，但是实际中扫描的到的bean,也会将@Service,@Controller,@Repository,@Controller都会被扫描出来。
![ConfigurationClassProcessPost-2.png](https://img03.sogoucdn.com/app/a/100520146/9fd4be07ffc679025ba7c0c7d886323f)
这里大概是@Component元注解，Spring在读取@Service,@Controller,@Repository,@Controller，也读取了它的元注解，并将作为@Component处理。这里就不做具体分析了。

#### 2.this.reader.loadBeanDefinitions()
在执行parse()方法后@Component,@Service等注解的bean会被注解添加到beanDefinitionMap,而@Import,@Bean这些会被转为ConfigurationClass进行下一步解析。
这里是loadBeanDefinitions()里面核心方法
```
    private void loadBeanDefinitionsForConfigurationClass(
            ConfigurationClass configClass, TrackedConditionEvaluator trackedConditionEvaluator) {

        if (trackedConditionEvaluator.shouldSkip(configClass)) {
            String beanName = configClass.getBeanName();
            if (StringUtils.hasLength(beanName) && this.registry.containsBeanDefinition(beanName)) {
                this.registry.removeBeanDefinition(beanName);
            }
            this.importRegistry.removeImportingClass(configClass.getMetadata().getClassName());
            return;
        }
        //注册@Import修饰的bean
        if (configClass.isImported()) {
            registerBeanDefinitionForImportedConfigurationClass(configClass);
        }
        //注册@Bean修饰的bean
        for (BeanMethod beanMethod : configClass.getBeanMethods()) {
            loadBeanDefinitionsForBeanMethod(beanMethod);
        }
        //注册@ImportResources配置的信息
        loadBeanDefinitionsFromImportedResources(configClass.getImportedResources());
        //@Import注解的bean,且bean实现了ImportBeanDefinitionRegistrar
        loadBeanDefinitionsFromRegistrars(configClass.getImportBeanDefinitionRegistrars());
    }
```
####3.postProcessBeanFactory()方法
postProcessBeanFactory()对加了@Configuration注解的类进行CGLIB代理和向Spring中添加一个后置处理器ImportAwareBeanPostProcessor。
```
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
	    //.....省略代码
	    
	    //@Configuration注解的类进行CGLIB代理
		enhanceConfigurationClasses(beanFactory);
		//添加一个后置处理器ImportAwareBeanPostProcessor
		beanFactory.addBeanPostProcessor(new ImportAwareBeanPostProcessor(beanFactory));
	}
```
