package Adapter.session;

/**
 * 适配对象
 */
public class Adapter extends Adaptee implements Target {


    public void call() {
        //do something
        super.make();
    }
}
