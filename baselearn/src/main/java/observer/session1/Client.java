package observer.session1;

public class Client {

    public static void main(String[] args) {

        KunKun kunKun = new KunKun();
        kunKun.addObserver(new RapFan());
        kunKun.addObserver(new SingFan());
        kunKun.update("我出来了");
    }
}
