package cn.edu.ruc;

import cn.edu.ruc.hola.utils.SHA256Utils;
import org.junit.jupiter.api.Test;

public class SHA256UtilsTest {

    @Test
    public void testForVerify() {
        boolean flag = SHA256Utils.verify("hello world", SHA256Utils.getSHA256Str("hello world"));
        assert flag;
    }
}
