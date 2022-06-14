package cn.edu.ruc;

import cn.edu.ruc.hola.config.Config;
import cn.edu.ruc.hola.exec.Exec;
import cn.edu.ruc.hola.utils.ShuffleUtils;
import org.junit.jupiter.api.Test;
import cn.edu.ruc.hola.utils.Generations2;

import java.util.List;

public class ExecTest {

    @Test
    public void testForExec() {
        Config co = new Config(0.89f, 1000, 26, 20);
        var app = new Exec(co);
        var ans = app.sids();
        System.out.println();
//        var p = new Generations().optimize(co.threshold());
        var p = Generations2.optimize(co.getThreshold(), 26, 20);
        System.out.println(p);
        List<String> cut_ids = ShuffleUtils.INSTANCE.randomChoose(ans, p.m());
        for (String an : cut_ids) {
            System.out.println(an);
        }
    }
}
