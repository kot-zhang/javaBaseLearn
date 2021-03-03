package designpattern.observer.session;

/**
 * 观察者
 */
public class SingFan implements Fan {

    public void update() {
        System.out.println("Sing ：KUNKUN 出来了！");
    }
}
