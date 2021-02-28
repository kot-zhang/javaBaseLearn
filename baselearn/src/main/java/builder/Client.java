package builder;

public class Client {


    public static void main(String[] args) {
        Person3 person3 = new Person3.PersonBuilder("zhang", 12).address("武汉").phone("159xxxxxx795").build();
    }
}
