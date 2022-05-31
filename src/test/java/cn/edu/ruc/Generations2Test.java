package cn.edu.ruc;

import org.junit.jupiter.api.Test;
import cn.edu.ruc.hola.utils.Generations2;
import cn.edu.ruc.hola.utils.JsonUtils;

public class Generations2Test {

    @Test
    public void testForBuildInfBA() {
        var ans = Generations2.infPBA(6, 0.8f, 26, 20);
        System.out.println(ans);
    }

    @Test
    public void testForBuildOptimize() {
        var ans = Generations2.optimize(0.95f, 26, 20);
        System.out.println(ans);
        var an = JsonUtils.getGson().toJson(ans);
        System.out.println(an);
    }
}
