package cn.edu.ruc;

import org.junit.jupiter.api.Test;

import static cn.edu.ruc.hola.config.ServiceIdHelper.readProps;

public class JsonUtilsTest {
    @Test
    public void testForBuildJson() {
        var l = readProps();
        System.out.println(l);
    }
}
