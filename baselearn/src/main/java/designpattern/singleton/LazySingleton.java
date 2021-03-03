package designpattern.singleton;

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
