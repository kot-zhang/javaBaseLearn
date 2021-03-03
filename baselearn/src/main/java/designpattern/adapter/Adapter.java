package designpattern.adapter;

public class Adapter extends Target implements Adaptee {

    public void request() {
        super.say();
    }
}
