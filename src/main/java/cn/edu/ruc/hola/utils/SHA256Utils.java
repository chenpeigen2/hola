package cn.edu.ruc.hola.utils;

import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

public final class SHA256Utils {

    private static final Logger logger = Logger.getLogger("SHA256Utils");

    private SHA256Utils() {

    }

    public static String getSHA256Str(String str) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(str.getBytes(StandardCharsets.UTF_8));
            encodeStr = Hex.encodeHexString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        logger.info(encodeStr);
        return encodeStr;
    }

    public static boolean verify(String text, String sha256) {
        String sha256Str = getSHA256Str(text);
        return sha256Str.equalsIgnoreCase(sha256);
    }
}
