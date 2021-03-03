package designpattern.observer.session1;

import java.util.Observable;
import java.util.Observer;

/**
 * 观察者
 */
public class RapFan implements Observer {


    public void update(Observable o, Object arg) {
        System.out.println("RapFan收到：" + arg);
    }
}
