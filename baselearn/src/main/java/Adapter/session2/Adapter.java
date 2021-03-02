package Adapter.session2;

public class Adapter implements Target {

    private Adaptee adaptee;

    Adapter(Adaptee adaptee) {
        this.adaptee = adaptee;
    }

    public void call() {
        //do something
        adaptee.make();
    }
}
