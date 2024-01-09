package cn.ruc.edu.hola.fakeTee.tee;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSA {
    /**
     * 如果需要在后台使用将Base64换成后台的Base64即可
     * rsa加密
     *
     * @param source  加密的数据为AES key
     * @param pub_key 公钥
     */
    public static String rsaEncrypt(String source, String pub_key) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(java.util.Base64.getDecoder().decode(pub_key.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] bs = cipher.doFinal(source.getBytes());
            return java.util.Base64.getEncoder().encodeToString(bs);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 如果需要在后台使用将Base64换成后台的Base64即可
     * rsa解密
     *
     * @param pri_key 私钥
     */
    public static String rsaDecrypt(String rsa, String pri_key) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(java.util.Base64.getDecoder().decode(pri_key.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] b = cipher.doFinal(Base64.getDecoder().decode(rsa.getBytes()));
            return new String(b, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
