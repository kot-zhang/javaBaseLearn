package designpattern.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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
