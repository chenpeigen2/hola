package cn.ruc.edu.hola.backbend.encrypy;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class EncryptUtils {

    public static String RSA_PUBLIC_KEY = "MIGdMA0GCSqGSIb3DQEBAQUAA4GLADCBhwKBgQC2a7bm+na9YhCfiWYZU7x+99MktMW9el8sjd+v0AEtth95Nh/6f7Wzc9ydwsuOJSu8OKDWnTsEYCE4wyJ4Zmw6nCpRYiO1zwgt+dRLm7IIhdcjsI1SjLLyptToJJ1ab2E3dq6Ck1ifMpJ36lj8cLAFLKRqW8edSbf87mFyxYZY/wIBAw==";
    public static String RSA_PRIVATE_KEY = "MIIBNgIBADANBgkqhkiG9w0BAQEFAASCASAwggEcAgEAAoGBALZrtub6dr1iEJ+JZhlTvH730yS0xb16XyyN36/QAS22H3k2H/p/tbNz3J3Cy44lK7w4oNadOwRgITjDInhmbDqcKlFiI7XPCC351EubsgiF1yOwjVKMsvKm1OgknVpvYTd2roKTWJ8yknfqWPxwsAUspGpbx51Jt/zuYXLFhlj/AgEAAoGAeZ0kmfxPKOwLFQZEEOJ9qfqMwyMuflGUyF6VH+AAySQU+3lqpv/Od6KTE9cyXsNyfXsV5GjSAurA0IIW+u7y0Jy+0IJuCTCt7vZwa4v+CA3Qy8sVBkbCve0KaFZIAYmdLKDfPOrKJRrLUVOoEM2oaaVfIvAhwLOqpBUmVwGa/zsCAQACAQACAQACAQACAQA=";


    /**
     * 如果需要在后台使用将Base64换成后台的Base64即可
     * rsa加密
     *
     * @param source  加密的数据为AES key
     * @param pub_key 公钥
     * @return
     */
    public static String rsaEncrypt(String source, String pub_key) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(pub_key.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] bs = cipher.doFinal(source.getBytes());
            return Base64.getEncoder().encodeToString(bs);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 如果需要在后台使用将Base64换成后台的Base64即可
     * rsa解密
     *
     * @param rsa
     * @param pri_key 私钥
     * @return
     */
    public static String rsaDecrypt(String rsa, String pri_key) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(pri_key.getBytes()));
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

    /**
     * 对称AES加密 ECB方式
     *
     * @param content
     * @param key     128 192 256位  即Key长度为 16, 24， 32
     * @return
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
     * @param content
     * @param key     128 192 256位  即Key长度为 16, 24， 32
     * @return
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

    public static void testRsa() {
        //实际开发中RSA是加密AES的KEY使用, 注意1024生成的KEY加密长度限制
        String data = "ABCDEFGHIJ123456";
        try {
//            String jniRsa = Encrypts.rsaEncrypt(data);
//            String jniData = Encrypts.rsaDecrypt(jniRsa);
            String javaRsa = EncryptUtils.rsaEncrypt(data, RSA_PUBLIC_KEY);
            String javaData = EncryptUtils.rsaDecrypt(javaRsa, RSA_PRIVATE_KEY);

            var zzz = EncryptUtils.rsaDecrypt("E050BoSpU6ZTiv4XwrSUdAqk4wCdWKAhqflcbPbt1/QELAJeWX+p754OVTQA7r0M6aNW1Cj24/Ca7tDZHhbtFjbfavxVQlo/iFGhR8HG4OEFx5O5hROHo8IgmY827YtebEa6fbcSQIu+biHwkfhXhJ+a2eBVGPbkzAdUm6C5RlI=", RSA_PRIVATE_KEY);

            System.out.println(zzz);
            System.out.println("zoooooo");

            System.out.println(javaRsa);

            System.out.println(javaData);

//            LogUtils.i("Jni rsa = %s , Java rsa =  %s ", jniData, javaData);

            //分别用C解密Java加密的数据, 和用Java解密C加密的数据
//            String jniDeJavaRsa = Encrypts.rsaDecrypt(javaRsa);
//            String javaDeJniRsa = EncryptUtils.rsaDecrypt(jniRsa, RSA_PRIVATE_KEY);
//            LogUtils.i("Jni解密Java = %s,  Java解密Jni = %s, 结果是否一致 = %b", jniDeJavaRsa, javaDeJniRsa, jniDeJavaRsa.equals(javaDeJniRsa));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testEcbAES() {
        String key = "0123456789abcdefghijklmn";
        try {
//            String jniRes = Encrypts.aesEcbEncrypt(abc, key);
//            String jniData = Encrypts.aesEcbDecrypt(jniRes, key);
//            LogUtils.i("ECB模式 JniAesEnc = %s, JniAesDec = %s", jniRes, jniData);

            var abc = "123ABC";

            String javaRes = EncryptUtils.encryptAES(abc, key);
            String javaData = EncryptUtils.decryptAES(javaRes, key);


            System.out.println(javaRes);

            System.out.println(javaData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        testRsa();
        testEcbAES();
    }
}
