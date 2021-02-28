package singleton;

/**
 * 饿汉模式
 */
public class HungrySingleton {

    private final static HungrySingleton instance = new HungrySingleton();

    public static HungrySingleton getInstance() {
        return instance;
    }
}
