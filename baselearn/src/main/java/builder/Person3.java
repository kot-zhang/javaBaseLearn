package builder;

public class Person3 {

    /**
     * 必须属性
     */
    private final String name;
    /**
     * 必须属性
     */
    private final Integer age;

    private String address;

    private String phone;

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

    private Person3(PersonBuilder personBuilder) {
        this.address = personBuilder.address;
        this.age = personBuilder.age;
        this.name = personBuilder.name;
        this.phone = personBuilder.phone;
    }

    public static class PersonBuilder {
        /**
         * 必须属性
         */
        private final String name;
        /**
         * 必须属性
         */
        private final Integer age;

        private String address;

        private String phone;

        /**
         * 构造方法传必填函数
         */
        public PersonBuilder(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        public PersonBuilder address(String address) {
            this.address = address;
            return this;
        }

        public PersonBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Person3 build() {
            return new Person3(this);
        }
    }
}
