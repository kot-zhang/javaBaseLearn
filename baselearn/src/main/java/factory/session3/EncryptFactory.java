package factory.session3;

public class EncryptFactory {


    public static String encrypt(String txt, int type) {
        switch (type) {
            case 1:
                return Base64Encryption.encrypt(txt);
            case 2:
                return Md5Encryption.encrypt(txt);
            default:
                return "";
        }
    }
}
