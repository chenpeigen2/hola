package cn.ruc.edu.hola.backbend.encrypy;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import org.bouncycastle.asn1.pkcs.RSAPrivateKeyStructure;
import org.bouncycastle.asn1.x509.RSAPublicKeyStructure;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
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
    private static final String PUBLICKEY = "MIGHAoGBALZrtub6dr1iEJ+JZhlTvH730yS0xb16XyyN36/QAS22H3k2H/p/tbNz\n3J3Cy44lK7w4oNadOwRgITjDInhmbDqcKlFiI7XPCC351EubsgiF1yOwjVKMsvKm\n1OgknVpvYTd2roKTWJ8yknfqWPxwsAUspGpbx51Jt/zuYXLFhlj/AgED";
    private static final String PRIVATE_KEY = "MIICWwIBAAKBgQC2a7bm+na9YhCfiWYZU7x+99MktMW9el8sjd+v0AEtth95Nh/6\nf7Wzc9ydwsuOJSu8OKDWnTsEYCE4wyJ4Zmw6nCpRYiO1zwgt+dRLm7IIhdcjsI1S\njLLyptToJJ1ab2E3dq6Ck1ifMpJ36lj8cLAFLKRqW8edSbf87mFyxYZY/wIBAwKB\ngHmdJJn8TyjsCxUGRBDifan6jMMjLn5RlMhelR/gAMkkFPt5aqb/zneikxPXMl7D\ncn17FeRo0gLqwNCCFvru8tCcvtCCbgkwre72cGuL/ggN0MvLFQZGwr3tCmhWSAGJ\nnSyg3zzqyiUay1FTqBDNqGmlXyLwIcCzqqQVJlcBmv87AkEA5grvj0F75uSHzzOn\n90FSim17VXWNVzr7iSerg6nAFnrTqk/dOe0/KYQ1O9gi1ZWvPPFdpI+YRbCfZaOi\nBpI6PQJBAMsBKQ89LB8ev7j4AlJzqeawdqp4O8tT2jodoB+HmAp6oNsPyfk8KGDd\nSLEEwGadt3ekWE8FY/aHZ2kETjyLn+sCQQCZXJ+01lKZ7a/fd8VPgOGxnlI4+Qjk\n0f0GGnJXxoAO/I0cNT4mniobrXjSkBc5DnTTS5PDCmWDyxTubRavDCbTAkEAh1Yb\nX34dahR/0KVW4aJxRHWkcaV9Mjfm0WkValplXFHAkgqGpiga6z4wdgMq7xPPpRg6\n31jtTwTvm1g0KF0VRwJAY0rj3NWey/4apiEdrNV143w83iQtFTDliKXB8voAxk6y\npZkunO7IzRcbnC2+132e0/G9LdLGbVqgHO4i2leMYw==";


    static final String test_key = """
            MIIBCAKCAQEAu4wnXhSe1bJueEaXkt6Pjp9vLuotCLa3RXUkiHA9lzSu9RB5ES75
            n2cz4o/CQXmi6Mfpyk499p9J/b9SGx3cqdhHcmwocc/RyhpROBaAlF4b1aSIriL/
            SB3LSkIkdZnfBlNPdOmSrq7ESg13/NH4g5wHwDsDRHTL9OzOr72408g5m6LOv4q9
            nTQxKUxPa6O8c7gIDR9Yh7zRcdc/nibwYhqICzKQJaSBqnMs/WRVAuSo6OhOZBzM
            u7qCWfNWJSw0Db9X3roS1lfWp/WfK6iTHrPj70NP+vUgEcLdXAORdYcC5jkiAqmf
            dYBp9wSfoqoIW1duxdMaBcqYOaqgtscIlQIBAw==
            """;

    private KeyUtils() {
    }

    public static void main(String[] args) {
        pemKey2DerKey();
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
            System.out.println(pemPublicKey2derPublicKey(test_key));
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

    public static String readPublicKey() throws Exception {
        // TODO
        String publicKeyPEM = PUBLICKEY;

        byte[] encoded =
                java.util.Base64.getMimeDecoder().decode(publicKeyPEM.getBytes());

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        var key = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        return java.util.Base64.getEncoder().encodeToString(key.getEncoded());

    }
}
