package cn.edu.ruc;

import org.junit.jupiter.api.Test;
import cn.edu.ruc.hola.utils.Generations2;

public class Generations2Test {

    @Test
    public void testForBuildInfBA() {
        var ans = Generations2.evaluate(6, 0.8f, 26, 20);
        System.out.println(ans);
    }

    @Test
    public void testForBuildOptimize() {
        var ans = Generations2.select(0.95f, 100, 67, 0.9);
        System.out.println(ans);
    }
}
