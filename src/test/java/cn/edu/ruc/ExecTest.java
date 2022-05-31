package cn.edu.ruc;

import cn.edu.ruc.hola.config.Config;
import cn.edu.ruc.hola.exec.Exec;
import org.junit.jupiter.api.Test;
import cn.edu.ruc.hola.utils.Generations2;
import cn.edu.ruc.hola.utils.ShuffleUtils;

public class ExecTest {

    @Test
    public void testForExec() {
        Config co = new Config(0.89f, 1000, 26, 20);
        var app = new Exec(co);
        var ans = app.sids().toArray(String[]::new);
        System.out.println();
//        var p = new Generations().optimize(co.threshold());
        var p = Generations2.optimize(co.getThreshold(), 26, 20);
        System.out.println(p);
        String[] cut_ids = ShuffleUtils.randomChoose(ans, p.m());
        for (String an : cut_ids) {
            System.out.println(an);
        }
    }
}
