###Spring源码解析之 ——ConfigurationClassPostProcessor

> 思考：  

(1) @Configuration,@Component,@Bean,@Import 注解的作用是什么? 什么时候被解析的。 
(2) @Configuration和@Component有什么区别？  
>这里就ConfigurationClassPostProcessor就上场了  

先看张图，这个就是ConfigurationClassPostProcessor处理的构建节点。ConfigurationClassPostProcessor实现了BeanDefinitionRegistryPostProcessor，
而BeanDefinitionRegistryPostProcessor继承了BeanFactoryPostProcessor，所以 ConfigurationClassPostProcessor 中需要重写postProcessBeanDefinitionRegistry()
方法和postProcessBeanFactory()方法。而ConfigurationClassPostProcessor类的作用就是通过这两个方法去实现的。
![ConfigurationClassPostProcessor-1](https://img02.sogoucdn.com/app/a/100520146/0b1fd50ae5a2bcf6caf0ea246e440d26)
> ConfigurationClassPostProcessor做些什么？  

1：postProcessBeanDefinitionRegistry()将@Configuration,@Component,@Bean,@Import等注解的对象,解析为BeanDefinition,放在容器中，等待被解析为Spring bean；
这里会将@Configuration注解的类的BeanDefinition添加个属性，标记是FULL，还是LITE
2：postProcessBeanFactory()被标记FULL的BeanDefinition的beanClass替换为Cglib代理的class名

