package factory.session2;

public class FoodFactory {

    public static Food create() {
        Cook cook1 = new Cook();
        return cook1.create();

//        Cook2 cook2 = new Cook2();
//        return cook2.create();
    }
}
