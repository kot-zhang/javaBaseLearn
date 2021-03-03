package designpattern.observer.session;

import java.util.ArrayList;
import java.util.List;

/**
 * 被观察者
 */
public class KunKun {

    private List<Fan> fans;

    public KunKun() {
        fans = new ArrayList<Fan>();
    }

    public void addFan(Fan fan) {
        fans.add(fan);
    }

    public void notifyFan() {
        for (Fan fan : fans) {
            fan.update();
        }
    }
}
