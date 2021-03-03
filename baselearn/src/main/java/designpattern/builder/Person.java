package designpattern.builder;

public class Person {
    /**
     * 必须属性
     */
    private final String name;
    /**
     * 必须属性
     */
    private final Integer age;

    private final String address;

    private final String phone;

    //final成员的要求是最晚构造函数得初始化，否则编译报错

    public Person(String name, Integer age) {
        this(name, age, "", "");
    }

    public Person(String name, Integer age, String address) {
        this(name, age, address, "");
    }

    public Person(String name, Integer age, String address, String phone) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

}
