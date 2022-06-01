package cn.edu.ruc.hola.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

public final class SHA256Utils {

    private static final Logger logger = Logger.getLogger("SHA256Utils");

    private SHA256Utils() {

    }

    public static String getSHA256Str(String str) {
        String encodeStr;
        encodeStr = DigestUtils.sha256Hex(str);
        logger.info(encodeStr);
        return encodeStr;
    }

    public static boolean verify(String text, String sha256) {
        String sha256Str = getSHA256Str(text);
        return sha256Str.equalsIgnoreCase(sha256);
    }
}
