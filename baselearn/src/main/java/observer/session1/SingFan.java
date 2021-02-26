package observer.session1;

import java.util.Observable;
import java.util.Observer;

/**
 * 观察者
 */
public class SingFan implements Observer {

    public void update(Observable o, Object arg) {
        System.out.println("SingFan收到:" + arg);
    }
}
