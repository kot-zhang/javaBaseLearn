package designpattern.singleton;

/**
 * 饿汉模式
 */
public class HungrySingleton {

    private HungrySingleton() {

    }

    private final static HungrySingleton instance = new HungrySingleton();

    public static HungrySingleton getInstance() {
        return instance;
    }
}
