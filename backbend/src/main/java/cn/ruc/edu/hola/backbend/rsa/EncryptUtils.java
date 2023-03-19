package cn.ruc.edu.hola.backbend.rsa;

import javax.crypto.Cipher;
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

    public static void testRsa() {
        //实际开发中RSA是加密AES的KEY使用, 注意1024生成的KEY加密长度限制
        String data = "ABCDEFGHIJ123456";
        try {
//            String jniRsa = Encrypts.rsaEncrypt(data);
//            String jniData = Encrypts.rsaDecrypt(jniRsa);
            String javaRsa = EncryptUtils.rsaEncrypt(data, RSA_PUBLIC_KEY);
            String javaData = EncryptUtils.rsaDecrypt(javaRsa, RSA_PRIVATE_KEY);

            var zzz = EncryptUtils.rsaDecrypt("H85QONlBhrcfYl3BOhqnzbUqrGZcUlxKeQaE3hKXZ7WkyaTK3r7vDklMMe6bqICerzkcms" +
                    "5put9cHVOHt0lroRVjI5g9Bc/eZ9rarfvtpSuwfBUB2fdP6Jm1666b0IjLsMGADwQ4xcQwH1E2DLH0PYEyRNYwdrW+FSPl1ZMCkXI=", RSA_PRIVATE_KEY);

            System.out.println(zzz);

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

    public static void main(String[] args) {
        testRsa();
    }
}
