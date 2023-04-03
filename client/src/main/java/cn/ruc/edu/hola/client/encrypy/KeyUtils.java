package cn.ruc.edu.hola.client.encrypy;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.StringReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * author: you : 2019/6/20
 */
public final class KeyUtils {

//    #define PUBLICKEY "-----BEGIN RSA PUBLIC KEY-----\nMIGHAoGBALZrtub6dr1iEJ+JZhlTvH730yS0xb16XyyN36/QAS22H3k2H/p/tbNz\n3J3Cy44lK7w4oNadOwRgITjDInhmbDqcKlFiI7XPCC351EubsgiF1yOwjVKMsvKm\n1OgknVpvYTd2roKTWJ8yknfqWPxwsAUspGpbx51Jt/zuYXLFhlj/AgED\n-----END RSA PUBLIC KEY-----"
//    #define PRIVATE_KEY "-----BEGIN RSA PRIVATE KEY-----\nMIICWwIBAAKBgQC2a7bm+na9YhCfiWYZU7x+99MktMW9el8sjd+v0AEtth95Nh/6\nf7Wzc9ydwsuOJSu8OKDWnTsEYCE4wyJ4Zmw6nCpRYiO1zwgt+dRLm7IIhdcjsI1S\njLLyptToJJ1ab2E3dq6Ck1ifMpJ36lj8cLAFLKRqW8edSbf87mFyxYZY/wIBAwKB\ngHmdJJn8TyjsCxUGRBDifan6jMMjLn5RlMhelR/gAMkkFPt5aqb/zneikxPXMl7D\ncn17FeRo0gLqwNCCFvru8tCcvtCCbgkwre72cGuL/ggN0MvLFQZGwr3tCmhWSAGJ\nnSyg3zzqyiUay1FTqBDNqGmlXyLwIcCzqqQVJlcBmv87AkEA5grvj0F75uSHzzOn\n90FSim17VXWNVzr7iSerg6nAFnrTqk/dOe0/KYQ1O9gi1ZWvPPFdpI+YRbCfZaOi\nBpI6PQJBAMsBKQ89LB8ev7j4AlJzqeawdqp4O8tT2jodoB+HmAp6oNsPyfk8KGDd\nSLEEwGadt3ekWE8FY/aHZ2kETjyLn+sCQQCZXJ+01lKZ7a/fd8VPgOGxnlI4+Qjk\n0f0GGnJXxoAO/I0cNT4mniobrXjSkBc5DnTTS5PDCmWDyxTubRavDCbTAkEAh1Yb\nX34dahR/0KVW4aJxRHWkcaV9Mjfm0WkValplXFHAkgqGpiga6z4wdgMq7xPPpRg6\n31jtTwTvm1g0KF0VRwJAY0rj3NWey/4apiEdrNV143w83iQtFTDliKXB8voAxk6y\npZkunO7IzRcbnC2+132e0/G9LdLGbVqgHO4i2leMYw==\n-----END RSA PRIVATE KEY-----"

    /**
     * 下面的key是 C端生成的openssl的KEY, 是PEM需要转换成der
     */
    private static String PUBLICKEY = "MIGHAoGBALZrtub6dr1iEJ+JZhlTvH730yS0xb16XyyN36/QAS22H3k2H/p/tbNz\n3J3Cy44lK7w4oNadOwRgITjDInhmbDqcKlFiI7XPCC351EubsgiF1yOwjVKMsvKm\n1OgknVpvYTd2roKTWJ8yknfqWPxwsAUspGpbx51Jt/zuYXLFhlj/AgED";
    private static String PRIVATE_KEY = "MIICWwIBAAKBgQC2a7bm+na9YhCfiWYZU7x+99MktMW9el8sjd+v0AEtth95Nh/6\nf7Wzc9ydwsuOJSu8OKDWnTsEYCE4wyJ4Zmw6nCpRYiO1zwgt+dRLm7IIhdcjsI1S\njLLyptToJJ1ab2E3dq6Ck1ifMpJ36lj8cLAFLKRqW8edSbf87mFyxYZY/wIBAwKB\ngHmdJJn8TyjsCxUGRBDifan6jMMjLn5RlMhelR/gAMkkFPt5aqb/zneikxPXMl7D\ncn17FeRo0gLqwNCCFvru8tCcvtCCbgkwre72cGuL/ggN0MvLFQZGwr3tCmhWSAGJ\nnSyg3zzqyiUay1FTqBDNqGmlXyLwIcCzqqQVJlcBmv87AkEA5grvj0F75uSHzzOn\n90FSim17VXWNVzr7iSerg6nAFnrTqk/dOe0/KYQ1O9gi1ZWvPPFdpI+YRbCfZaOi\nBpI6PQJBAMsBKQ89LB8ev7j4AlJzqeawdqp4O8tT2jodoB+HmAp6oNsPyfk8KGDd\nSLEEwGadt3ekWE8FY/aHZ2kETjyLn+sCQQCZXJ+01lKZ7a/fd8VPgOGxnlI4+Qjk\n0f0GGnJXxoAO/I0cNT4mniobrXjSkBc5DnTTS5PDCmWDyxTubRavDCbTAkEAh1Yb\nX34dahR/0KVW4aJxRHWkcaV9Mjfm0WkValplXFHAkgqGpiga6z4wdgMq7xPPpRg6\n31jtTwTvm1g0KF0VRwJAY0rj3NWey/4apiEdrNV143w83iQtFTDliKXB8voAxk6y\npZkunO7IzRcbnC2+132e0/G9LdLGbVqgHO4i2leMYw==";

    //    https://stackoverflow.com/questions/4032985/how-do-we-convert-a-string-from-pem-to-der-format
    static {
        PRIVATE_KEY = """
                MIICXAIBAAKBgQDjuN5VvX/4Xph32GiNcmjW/qxb9Ud9tnSAqIUMbbMy7uDZoAOf
                1t/ZNHBQS3ZpFHJ14lXy9KXvcknRTFlhbwtjN/nKbMeyyhU0zd/s3PtWyAVOP9to
                OSXxMjuEDpYWdW2/I5+vqgijLG8yWUdiCncBphyeWKKzc8fG7/bsUFT5CwIBAwKB
                gQCX0JQ5KP/66br6kEWzoZs5/x2So4T+eaMAcFiy88zMn0CRFVe/5JU7eErgMk7w
                uExOluP3TcP09tvg3ZDrn1zrjgU2vkWujOlR7rstdSp9stySzljAu4yIhb3QvCYg
                XyV7wskkyDOPZKyFBCvMJguaxkgpI6Cigg9TkBQGXR8LawJBAPT1NLxfn0wiJErw
                CxgQes7rT2X/zjPI+gkI38B1F7xST14xB0DyG539gST3TunJUo4EkWfd8AhYjEHR
                SNeOeRsCQQDt/MOS/42qlRWc1x2VKx9szyKkVnjsCipglesz584qYzYhQPE8yTB3
                bumuDmDnnEbuq887U8+oWD1WB5ntF+7RAkEAo04jKD+/iBbC3KAHZWBR30eKQ//e
                zTCmsLCVKvi6fYw06XYE1fa9E/5WGKTfRoY3CVhg7+lKsDsIK+DbOl77ZwJBAJ6o
                gmH/s8cODmiPaQ4cv53fbG2O+0gGxusOnM1FNBxCJBYrS33bdaT0m8le60US2fRy
                iieNNRrlfjlaZp4P9IsCQEfZSWrOMZD5vb/RRbPYvmhF2o7MadSAQaF61p+iRNhe
                X72GPqov8VL7zxwi5h3J4/m+0zTGIRMIsiw86VONZpU=""";
        PUBLICKEY = """
                MIGHAoGBAOO43lW9f/hemHfYaI1yaNb+rFv1R322dICohQxtszLu4NmgA5/W39k0
                cFBLdmkUcnXiVfL0pe9ySdFMWWFvC2M3+cpsx7LKFTTN3+zc+1bIBU4/22g5JfEy
                O4QOlhZ1bb8jn6+qCKMsbzJZR2IKdwGmHJ5YorNzx8bv9uxQVPkLAgED""";
    }

    private KeyUtils() {
    }

    public static void main(String[] args) {
        pemKey2DerKey();
//        try {
//            readPublicKey();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }

    /**
     * 将openssl生成的pem key转成java的der格式的key,  将 -----BEGIN RSA PUBLIC KEY----- 与结尾去除即可
     */
    public static void pemKey2DerKey() {
        try {
            String der_publicKey = pemPublicKey2derPublicKey(PUBLICKEY);
            String der_privateKey = pemPrivateKey2derPrivateKey(PRIVATE_KEY);
            System.out.println(der_publicKey.equals(EncryptUtils.RSA_PUBLIC_KEY));
            System.out.println(der_privateKey.equals(EncryptUtils.RSA_PRIVATE_KEY));
            //这就是JAVA端的DER公钥与私钥
            System.out.println(der_publicKey);
//            System.out.println(pemPublicKey2derPublicKey(test_key));
            System.out.println(der_privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * pem格式的privateKey转der格式的privateKey
     *
     * @param pemPriKey 将openssl生成的pem key转成java的der格式的key,  将 -----BEGIN RSA PUBLIC KEY-----\n 与结尾去除即可
     */
    public static String pemPrivateKey2derPrivateKey(String pemPriKey) {
        try {
            ASN1InputStream in = new ASN1InputStream(java.util.Base64.getMimeDecoder().decode(pemPriKey));
            var obj = in.readObject();
//            RSAPrivateKeyStructure pStruct = RSAPrivateKeyStructure.getInstance(obj);
            RSAPrivateKey pStruct = RSAPrivateKey.getInstance(obj);
            RSAPrivateKeySpec spec = new RSAPrivateKeySpec(pStruct.getModulus(), pStruct.getPrivateExponent());
            PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(spec);
            in.close();
            return java.util.Base64.getEncoder().encodeToString(privateKey.getEncoded());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * pem格式的publicKey转der格式的publicKey
     *
     * @param pemPubKey 将openssl生成的pem key转成java的der格式的key,  将 -----BEGIN RSA PUBLIC KEY-----\n 与结尾去除即可
     */
    public static String pemPublicKey2derPublicKey(String pemPubKey) {
        try {
            ASN1InputStream in = new ASN1InputStream(java.util.Base64.getMimeDecoder().decode(pemPubKey));
            var obj = in.readObject();
//            RSAPublicKeyStructure pStruct = RSAPublicKeyStructure.getInstance(obj);
            org.bouncycastle.asn1.pkcs.RSAPublicKey pStruct = org.bouncycastle.asn1.pkcs.RSAPublicKey.getInstance(obj);
            RSAPublicKeySpec spec = new RSAPublicKeySpec(pStruct.getModulus(), pStruct.getPublicExponent());
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(spec);
            in.close();
            return java.util.Base64.getEncoder().encodeToString(publicKey.getEncoded());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void readPublicKey() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        String pemKey = """
                -----BEGIN RSA PUBLIC KEY-----
                MIGHAoGBAOO43lW9f/hemHfYaI1yaNb+rFv1R322dICohQxtszLu4NmgA5/W39k0
                cFBLdmkUcnXiVfL0pe9ySdFMWWFvC2M3+cpsx7LKFTTN3+zc+1bIBU4/22g5JfEy
                O4QOlhZ1bb8jn6+qCKMsbzJZR2IKdwGmHJ5YorNzx8bv9uxQVPkLAgED
                -----END RSA PUBLIC KEY-----
                """;
        PemReader pemReader = new PemReader(new StringReader(pemKey));
        var rsaPubKey = pemReader.readPemObject();



        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(rsaPubKey.getContent());
        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(keySpec);

//        System.out.println("Public key: " + publicKey.getEncoded());
    }
}

