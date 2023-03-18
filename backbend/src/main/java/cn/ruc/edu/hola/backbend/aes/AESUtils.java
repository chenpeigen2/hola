package cn.ruc.edu.hola.backbend.aes;

import cn.ruc.edu.hola.backbend.SecretKeyUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.util.Base64;

/**
 * @author zhupig3028
 * @ClassName: AESUtils
 * @Description: AES 对称算法加密/解密工具类
 * @date 2022年5月10日
 */
public class AESUtils {


    /**
     * 加解密统一编码方式
     */
    private final static String ENCODING = "utf-8";

    /**
     * 加解密方式
     */
    private final static String ALGORITHM = "AES";

    /**
     * 加密模式及填充方式
     */
    private final static String PATTERN = "AES/ECB/pkcs5padding";

    /**
     * AES加密
     *
     * @param plainText
     * @param key
     * @return
     * @throws Exception
     */
    public static String encrypt(String plainText, String key) throws Exception {
        if (key == null) {
            System.out.print("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (key.length() != 16) {
            System.out.print("Key长度不是16位");
            return null;
        }
        SecretKey secretKey = new SecretKeySpec(key.getBytes(ENCODING), ALGORITHM);
        // AES加密采用pkcs5padding填充
        Cipher cipher = Cipher.getInstance(PATTERN);
        //用密匙初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        //执行加密操作
        byte[] encryptData = cipher.doFinal(plainText.getBytes(ENCODING));
        return Base64.getEncoder().encodeToString(encryptData);
    }


    /**
     * AES解密
     *
     * @param plainText
     * @param key
     * @return
     * @throws Exception
     */
    public static String decrypt(String plainText, String key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key.getBytes(ENCODING), ALGORITHM);
        // 获取 AES 密码器
        Cipher cipher = Cipher.getInstance(PATTERN);
        // 初始化密码器（解密模型）
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        // 解密数据, 返回明文
        byte[] encryptData = cipher.doFinal(Base64.getDecoder().decode(plainText));
        return new String(encryptData, ENCODING);
    }

    public static void main(String[] args) throws Exception {
        var aa = SecretKeyUtils.generateAESKey();
        System.out.println(aa);
        // JTMBUd0yiEgjUxF6vXhjNi1bt0b1/V9LoPrtlniGZe6TWi+7+JMseJJidoLhmyjDb3tiYioWRqSBDAoktnZ7BPDtnKR1spuxJ7DidICrZfw=
        var aaa = encrypt("test", "mIbzhgCRECELsGB5");
        System.out.println(aaa);
    }
}