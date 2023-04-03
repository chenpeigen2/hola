package cn.ruc.edu.hola.client.encrypy.dh;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public class DHCoder {
    public static final String ALGORITHM = "DH";

    /**
     * 默认密钥字节数
     *
     * <pre>
     * DH
     * Default Keysize 1024
     * Keysize must be a multiple of 64, ranging from 512 to 1024 (inclusive).
     * </pre>
     */
    private static final int KEY_SIZE = 1024;

    /**
     * DH加密下需要一种对称加密算法对数据加密，这里我们使用DES，也可以使用其他对称加密算法。
     */
    public static final String SECRET_ALGORITHM = "AES";
    private static final String PUBLIC_KEY = "DHPublicKey";
    private static final String PRIVATE_KEY = "DHPrivateKey";

    /**
     * 初始化甲方密钥
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> initKey() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator
                .getInstance(ALGORITHM);
        keyPairGenerator.initialize(KEY_SIZE);

        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // 甲方公钥
        DHPublicKey publicKey = (DHPublicKey) keyPair.getPublic();

        // 甲方私钥
        DHPrivateKey privateKey = (DHPrivateKey) keyPair.getPrivate();

        Map<String, Object> keyMap = new HashMap<>(2);

        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * 初始化乙方密钥
     *
     * @param key 甲方公钥
     * @return
     * @throws Exception
     */
    public static Map<String, Object> initKey(String key) throws Exception {
        // 解析甲方公钥
        byte[] keyBytes = decryptBASE64(key);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);

        // 由甲方公钥构建乙方密钥
        DHParameterSpec dhParamSpec = ((DHPublicKey) pubKey).getParams();

        KeyPairGenerator keyPairGenerator = KeyPairGenerator
                .getInstance(keyFactory.getAlgorithm());
        System.out.println(keyFactory.getAlgorithm());

        keyPairGenerator.initialize(dhParamSpec);
        keyPairGenerator.initialize(KEY_SIZE);

        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // 乙方公钥
        DHPublicKey publicKey = (DHPublicKey) keyPair.getPublic();

        // 乙方私钥
        DHPrivateKey privateKey = (DHPrivateKey) keyPair.getPrivate();

        Map<String, Object> keyMap = new HashMap<>(2);

        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);

        return keyMap;
    }

    /**
     * 加密<br>
     *
     * @param data       待加密数据
     * @param publicKey  甲方公钥
     * @param privateKey 乙方私钥
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String publicKey,
                                 String privateKey) throws Exception {

        // 生成本地密钥
        SecretKey secretKey = getSecretKey(publicKey, privateKey);

        // 数据加密
        Cipher cipher = Cipher.getInstance(secretKey.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        return encryptBASE64(cipher.doFinal(data.getBytes()));
    }

    /**
     * 解密<br>
     *
     * @param data       待解密数据
     * @param publicKey  乙方公钥
     * @param privateKey 乙方私钥
     * @return
     * @throws Exception
     */
    public static String decrypt(String data, String publicKey,
                                 String privateKey) throws Exception {

        // 生成本地密钥
        SecretKey secretKey = getSecretKey(publicKey, privateKey);
        // 数据解密
        Cipher cipher = Cipher.getInstance(secretKey.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return new String(cipher.doFinal(decryptBASE64(data)));
    }

    private static volatile SecretKey secretKey;

    public static void resetSecretKey() {
        secretKey = null;
    }

    /**
     * 构建密钥
     *
     * @param publicKey  公钥
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    private static synchronized SecretKey getSecretKey(String publicKey, String privateKey)
            throws Exception {
        if (secretKey != null) return secretKey;
        // 初始化公钥
        byte[] pubKeyBytes = decryptBASE64(publicKey);

        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(pubKeyBytes);
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);

        // 初始化私钥
        byte[] priKeyBytes = decryptBASE64(privateKey);

        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(priKeyBytes);
        Key priKey = keyFactory.generatePrivate(pkcs8KeySpec);

        KeyAgreement keyAgree = KeyAgreement.getInstance(keyFactory
                .getAlgorithm());
        keyAgree.init(priKey);
        keyAgree.doPhase(pubKey, true);
        // 生成本地密钥
        secretKey = keyAgree.generateSecret(SECRET_ALGORITHM);
        return secretKey;
    }

    /**
     * 取得私钥
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);

        return encryptBASE64(key.getEncoded());
    }

    /**
     * 取得公钥
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);

        return encryptBASE64(key.getEncoded());
    }

    /**
     * Base64解码，返回byte[]
     *
     * @param key 待解码字符串
     * @return 解码后的byte[]
     */
    public static byte[] decryptBASE64(String key) {
        return Base64.getMimeDecoder().decode(key);
    }

    /**
     * 将byte[]进行Base64编码
     *
     * @param key 待编码的byte[]
     * @return 编码后的字符串
     */
    public static String encryptBASE64(byte[] key) {
        return Base64.getMimeEncoder().encodeToString(key);
    }


    public static void main(String[] args) throws Exception {
        System.getProperties().setProperty("jdk.crypto.KeyAgreement.legacyKDF", "true");

//        // 生成甲方密钥对儿
//        Map<String, Object> aKeyMap = DHCoder.initKey();
//        String aPublicKey = DHCoder.getPublicKey(aKeyMap);
//        String aPrivateKey = DHCoder.getPrivateKey(aKeyMap);
//
//        System.err.println("甲方公钥:\r" + aPublicKey);
//        System.err.println("甲方私钥:\r" + aPrivateKey);
//
//        // 由甲方公钥产生本地密钥对儿
//        Map<String, Object> bKeyMap = DHCoder.initKey(aPublicKey);
//        String bPublicKey = DHCoder.getPublicKey(bKeyMap);
//        String bPrivateKey = DHCoder.getPrivateKey(bKeyMap);
//
//        System.err.println("乙方公钥:\r" + bPublicKey);
//        System.err.println("乙方私钥:\r" + bPrivateKey);

        String aPublicKey = Keys.SELF_PUB;
        String aPrivateKey = Keys.SELF_PRIV;

        String bPublicKey = Keys.PUB;
        String bPrivateKey = Keys.PRIV;

        String aInput = "abc ";
        System.err.println("原文: " + aInput);

        // 由甲方公钥，乙方私钥构建密文
        var aCode = DHCoder.encrypt(aInput, aPublicKey,
                bPrivateKey);

        // 由乙方公钥，甲方私钥解密
        String aOutput = (DHCoder.decrypt(aCode, bPublicKey, aPrivateKey));

        System.err.println("解密: " + aOutput);


        System.err.println(" ===============反过来加密解密================== ");
        String bInput = "def ";
        System.err.println("原文: " + bInput);

        // 由乙方公钥，甲方私钥构建密文
        var bCode = DHCoder.encrypt(bInput, bPublicKey,
                aPrivateKey);

        // 由甲方公钥，乙方私钥解密
        var bDecode = DHCoder.decrypt(bCode, aPublicKey, bPrivateKey);

        System.err.println("解密: " + (bDecode));
    }
}