package observer.session1;

import java.util.Observable;

/**
 * 被观察者
 */
public class KunKun extends Observable {


    public void update(String str) {
        setChanged();
        notifyObservers(str);
    }

}
