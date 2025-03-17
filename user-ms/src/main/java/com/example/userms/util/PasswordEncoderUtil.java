package com.example.userms.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncoderUtil {

    public static String encodePassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean matchPassword(String rawPassword, String encodedPassword) {
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }
}
