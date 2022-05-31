package cn.edu.ruc.hola.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.logging.Logger;

public final class MD5Utils {

    private static final Logger logger = Logger.getLogger("MD5Utils");

    private MD5Utils() {

    }

    // 不带秘钥加密
    public static String md5(String text) {
        // 加密后的字符串
        String md5str = DigestUtils.md5Hex(text);
        logger.info("MD52加密后的字符串为:" + md5str + "\t长度：" + md5str.length());
        return md5str;
    }

    /**
     * MD5验证方法
     */
    // 根据传入的密钥进行验证
    public static boolean verify(String text, String md5) {
        String md5str = md5(text);
        if (md5str.equalsIgnoreCase(md5)) {
            logger.info("MD5验证通过");
            return true;
        }
        return false;
    }
}
