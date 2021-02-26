package observer.session;

public class Client {


    public static void main(String[] args) {
        KunKun kunKun = new KunKun();
        kunKun.addFan(new RapFan());
        kunKun.addFan(new SingFan());
        kunKun.notifyFan();
    }
}
