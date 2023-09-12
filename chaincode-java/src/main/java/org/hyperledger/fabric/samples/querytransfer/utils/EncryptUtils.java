package org.hyperledger.fabric.samples.querytransfer.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EncryptUtils {

    /**
     * 对称AES加密 ECB方式
     *
     * @param key 128 192 256位  即Key长度为 16, 24， 32
     */
    public static String encryptAES(String content, String key) {
        try {
            byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
            byte[] enCodeFormat = key.getBytes();
            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
            // 指定加密的算法、工作模式和填充方式
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encryptedBytes = cipher.doFinal(byteContent);
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 对称AES解密  ECB方式
     *
     * @param key 128 192 256位  即Key长度为 16, 24， 32
     */
    public static String decryptAES(String content, String key) {
        try {
            byte[] encryptedBytes = Base64.getDecoder().decode(content.getBytes());
            byte[] enCodeFormat = key.getBytes();
            SecretKeySpec secretKey = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] result = cipher.doFinal(encryptedBytes);
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
