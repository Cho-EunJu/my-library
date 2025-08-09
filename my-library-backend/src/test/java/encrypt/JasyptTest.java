package encrypt;

import com.cho.library.backend.config.EncryptorConfig;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import static org.hamcrest.MatcherAssert.assertThat;

public class JasyptTest extends EncryptorConfig {

    public static void main(String[] args) {
        String plainText = "edu_user";

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("my-secret-key");

        String encText = encryptor.encrypt(plainText);
        String decText = encryptor.decrypt(encText);

        System.out.println("Ellen :: " + encText);
        System.out.println("Ellen :: " + decText);
    }
}
