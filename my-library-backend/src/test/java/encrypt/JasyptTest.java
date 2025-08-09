package encrypt;

import com.cho.library.backend.config.EncryptorConfig;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class JasyptTest extends EncryptorConfig {

    public static void main(String[] args) {

        String encKey = System.getProperty("jasypt.encryptor.password");
        System.out.println(encKey);
        String plainText = "testTxt";

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(encKey);

        String encText = encryptor.encrypt(plainText);
        String decText = encryptor.decrypt(encText);

        System.out.println("Ellen :: " + encText);
        System.out.println("Ellen :: " + decText);
    }
}
