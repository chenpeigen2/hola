package cn.ruc.edu.hola.client.encrypy;

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

//    static {
//        RSA_PUBLIC_KEY = "MIGdMA0GCSqGSIb3DQEBAQUAA4GLADCBhwKBgQDjuN5VvX/4Xph32GiNcmjW/qxb9Ud9tnSAqIUMbbMy7uDZoAOf1t/ZNHBQS3ZpFHJ14lXy9KXvcknRTFlhbwtjN/nKbMeyyhU0zd/s3PtWyAVOP9toOSXxMjuEDpYWdW2/I5+vqgijLG8yWUdiCncBphyeWKKzc8fG7/bsUFT5CwIBAw==";
//        RSA_PRIVATE_KEY = "MIIBNwIBADANBgkqhkiG9w0BAQEFAASCASEwggEdAgEAAoGBAOO43lW9f/hemHfYaI1yaNb+rFv1R322dICohQxtszLu4NmgA5/W39k0cFBLdmkUcnXiVfL0pe9ySdFMWWFvC2M3+cpsx7LKFTTN3+zc+1bIBU4/22g5JfEyO4QOlhZ1bb8jn6+qCKMsbzJZR2IKdwGmHJ5YorNzx8bv9uxQVPkLAgEAAoGBAJfQlDko//rpuvqQRbOhmzn/HZKjhP55owBwWLLzzMyfQJEVV7/klTt4SuAyTvC4TE6W4/dNw/T22+DdkOufXOuOBTa+Ra6M6VHuuy11Kn2y3JLOWMC7jIiFvdC8JiBfJXvCySTIM49krIUEK8wmC5rGSCkjoKKCD1OQFAZdHwtrAgEAAgEAAgEAAgEAAgEA";
//    }

    /**
     * 如果需要在后台使用将Base64换成后台的Base64即可
     * rsa加密
     *
     * @param source  加密的数据为AES key
     * @param pub_key 公钥
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
     * @param pri_key 私钥
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

    public static void testRsa() {
        //实际开发中RSA是加密AES的KEY使用, 注意1024生成的KEY加密长度限制
        String data = "helloABCDEFGHIJ123456";
        try {
//            String jniRsa = Encrypts.rsaEncrypt(data);
//            String jniData = Encrypts.rsaDecrypt(jniRsa);
            String javaRsa = EncryptUtils.rsaEncrypt(data, RSA_PUBLIC_KEY);
            String javaData = EncryptUtils.rsaDecrypt(javaRsa, RSA_PRIVATE_KEY);

            var zzz = EncryptUtils.rsaDecrypt("h6wNHjcPK+jFEDCJqCgjiuyyB0fBM4hFFxWvqV5mPI1InAmNJl+Jad7YGl+2GAZBMy2s7rvm8zkLRqWCNJ5zLQEdh+hnSeIRZFhRix7woEQKnpa/qVq8YF5xuq/NFTM6w1omZEODxbIA59qS6SMehloqMrfIqg4O0orROHXOdjQ=", RSA_PRIVATE_KEY);

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

            var abc = "fake1";
            abc = "{\"selector\": {\"data.countryid\": {\"$gt\": -1}},\"execution_stats\": true}";

            String javaRes = EncryptUtils.encryptAES(abc, key);
            String javaData = EncryptUtils.decryptAES(javaRes, key);


            System.out.println(javaRes);

            System.out.println(javaData);

            System.out.println(decryptAES("MHWAz7lun/x9nQDf64ncsRN0Wp1EJ1v6sPqkSlMF43/jYyz4++ZOX/J0QfIR/WwvGE/roh12tcff6KvPrCf08ZFXb52pwMOQHM/YkeX0EIEPS6+PGGApU/h3QBO9g5+i4Tar9tZRhkVKE0oBqOoB5fPjF5PETPxRsrPyfNRSj0GQLfCcqslyDy6UPCrHQHZtUJjZzpc9EIcYqdhFNM5MCMI+1t7l4UDXRWE1m8xmc7QguCLHNaLRCOWkee7M/lhKeWqRbJTAOTx6EAnvlkb1BbKlGQC/c/MsUJJFcJ6Aa7DJAq6P6j1BTbckA2FwP7vwkr6pAAICFpvykmgUUpVvqDZd8xn/Dc8fLiAjcvlE516ty8yrofG/Buq3CSeS7Yl5NGrKCjNwgQCSfs+YvsKh1RqG/GPkjHG/CRGAfkYkIi9aCbzhybe58tO8qJGbhLBDX8OEbaZATnqZ5HDWKXRiUFkdnDZMzhJogb7Dv50rRT8RKaQTmUMJNuSmwgBy4u4E9IE+uL89qWZlxWZf9cXeG/5g54wvL4AGyt7zXGc35CLsLCUzoEAvn5b/t2nABy/XraMJYiMvh8Q7dMNm9SWj8DU2VUxuBeTx8HWOB6syCoSBOrheV8ZuRgEy1mSJHvd0TSxJfPMO0cedZSpkW+TLGA==",
                    "0123456789abcdefghijklmn"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static String key1 = "MHWAz7lun/x9nQDf64ncsRN0Wp1EJ1v6sPqkSlMF43/jYyz4++ZOX/J0QfIR/WwvGE/roh12tcff6KvPrCf08ZFXb52pwMOQHM/YkeX0EIEPS6+PGGApU/h3QBO9g5+i4Tar9tZRhkVKE0oBqOoB5fPjF5PETPxRsrPyfNRSj0GQLfCcqslyDy6UPCrHQHZtUJjZzpc9EIcYqdhFNM5MCMI+1t7l4UDXRWE1m8xmc7QguCLHNaLRCOWkee7M/lhKeWqRbJTAOTx6EAnvlkb1BbKlGQC/c/MsUJJFcJ6Aa7DJAq6P6j1BTbckA2FwP7vwkr6pAAICFpvykmgUUpVvqDZd8xn/Dc8fLiAjcvlE516ty8yrofG/Buq3CSeS7Yl5NGrKCjNwgQCSfs+YvsKh1RqG/GPkjHG/CRGAfkYkIi9aCbzhybe58tO8qJGbhLBDX8OEbaZATnqZ5HDWKXRiUFkdnDZMzhJogb7Dv50rRT8RKaQTmUMJNuSmwgBy4u4E9IE+uL89qWZlxWZf9cXeG/5g54wvL4AGyt7zXGc35CLsLCUzoEAvn5b/t2nABy/XraMJYiMvh8Q7dMNm9SWj8DU2VUxuBeTx8HWOB6syCoSBOrheV8ZuRgEy1mSJHvd0LK5sDOj4PGcvQFGZFoe/Sw==";

    static String soruce = "{\"docs\":[\n" +
            "{\"_id\":\"2dde7d6ea7123c896632ea08a942a990\",\"_rev\":\"1-cf08c88c2f5405bd72fa7c064fb7a597\",\"code\":0,\"message\":\"success\",\"data\":{\"id\":1,\"name\":\"\",\"bfirstletter\":\"\",\"logo\":\"\",\"country\":\"China\",\"countryid\":0}}\n" +
            "],\n" +
            "\"bookmark\": \"g2wAAAACaAJkAA5zdGFydGtleV9kb2NpZG0AAAAgMmRkZTdkNmVhNzEyM2M4OTY2MzJlYTA4YTk0MmE5OTBoAmQACHN0YXJ0a2V5awABAGo\",\n" +
            "\"execution_stats\": {\"total_keys_examined\":0,\"total_docs_examined\":1,\"total_quorum_docs_examined\":0,\"results_returned\":1,\"execution_time_ms\":0.73}}\n";

    static {
        soruce = "aesdkjfkwkaefkmksdfmksdl;afm;erkmgkmsdafdsaf";
    }

    static String key3 = """
            {
              "docs": [
                {
                  "_id": "2dde7d6ea7123c896632ea08a942a990",
                  "_rev": "1-cf08c88c2f5405bd72fa7c064fb7a597",
                  "code": 0,
                  "message": "success",
                  "data": {
                    "id": 1,
                    "name": "",
                    "bfirstletter": "",
                    "logo": "",
                    "country": "China",
                    "countryid": 0
                  }
                }
              ],
              "bookmark": "g2wAAAACaAJkAA5zdGFydGtleV9kb2NpZG0AAAAgMmRkZTdkNmVhNzEyM2M4OTY2MzJlYTA4YTk0MmE5OTBoAmQACHN0YXJ0a2V5awABAGo",
              "execution_stats": {
                "total_keys_examined": 0,
                "total_docs_examined": 1,
                "total_quorum_docs_examined": 0,
                "results_returned": 1,
                "execution_time_ms": 0.73
              }
            }""";

    public static void main(String[] args) {
        var text = encryptAES(soruce, "0123456789abcdefghijklmn");
//        text = key1;
        System.out.println(text);

        System.out.println(key3.replaceAll(" ", ""));

        System.out.println(decryptAES(text, "0123456789abcdefghijklmn"));
//        testRsa();
//        testEcbAES();
    }
}
