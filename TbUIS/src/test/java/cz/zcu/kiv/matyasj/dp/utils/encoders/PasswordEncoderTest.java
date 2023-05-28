package cz.zcu.kiv.matyasj.dp.utils.encoders;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * PasswordEncoderMD5 test suite for BCrypt hash
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class PasswordEncoderTest {
    /** Expect BCrypt encoder */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * This method tests BCryptPasswordEncoder function - Encoding of string.
     */
    @Test
    public void encode() throws Exception {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        String stringBeforeEncode = "VeryHardPassword";
        String encodeString = passwordEncoder.encode(stringBeforeEncode);

        assertTrue(bCryptPasswordEncoder.matches(stringBeforeEncode, encodeString));
    }

    /**
     * This method tests BCryptPasswordEncoder function - Validating of string in plain text and hashed string.
     */
    @Test
    public void validate() throws Exception {
        String stringBeforeEncode = "VeryHardPassword";
        String correctHash = "$2a$10$e50s5HXnrMXwSlbgKvV3duGldBkhO3G4LAW0D.ZT3zxOjh/JFYD0O";
        /* Incorrect hash MD5 */
        String inCorrectHash = "b7608279dd8566a122364ea6eb44edc4";

        boolean encodeState = passwordEncoder.matches(stringBeforeEncode, correctHash);

        assertTrue(encodeState);

        encodeState = passwordEncoder.matches(stringBeforeEncode, inCorrectHash);

        assertFalse(encodeState);
    }
}