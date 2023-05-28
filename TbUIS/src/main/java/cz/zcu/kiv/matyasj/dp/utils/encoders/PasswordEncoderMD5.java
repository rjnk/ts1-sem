package cz.zcu.kiv.matyasj.dp.utils.encoders;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Objects of this class serves to encode passwords int MD5 hash.
 * MD5 is not safe, but there is just for testing purpose.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@Component
public class PasswordEncoderMD5 implements PasswordEncoder{
    /** static object MessageDigest */
    private static MessageDigest md;

    /**
     * Initialization method for init static object MessageDigest
     */
    private static void init() {
        if (md == null) {
            try {
                md = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method encode string a return MD5 hash
     *
     * @param password  Password
     * @return hash of password
     */
    @Override
    public String encode(CharSequence password) {
        init();
        byte[] bytes = md.digest(password.toString().getBytes());

        StringBuilder sb = new StringBuilder();
        for (byte oneByte : bytes) {
            sb.append(Integer.toHexString((oneByte & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

    /**
     * Validate if equal password in plaintext and password in MD5 hash
     *
     * @param password  Password
     * @param hash      Hash of password
     * @return true if password matches
     */
    @Override
    public boolean matches(CharSequence password, String hash) {
        init();
        String passwordHash = encode(password);

        return passwordHash.equals(hash);
    }
}