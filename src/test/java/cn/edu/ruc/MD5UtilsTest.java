package cn.edu.ruc;

import org.junit.jupiter.api.Test;
import cn.edu.ruc.hola.utils.MD5Utils;

public class MD5UtilsTest {

    @Test
    public void testForMD5() {
        // String str =
        // "181115.041650.10.88.168.148.2665.2419425653_1";181115.040908.10.88.181.118.3013.1655327821_1
        String str = "181115.040908.10.88.181.118.3013.1655327821_1";
        System.out.println("加密的字符串：" + str + "\t长度：" + str.length());
        MD5Utils.md5(str);
    }
}
