
package cn.edu.ruc;

import org.junit.jupiter.api.Test;
import cn.edu.ruc.hola.utils.Generations;

public class GenerationsTest {

    @Test
    public void testForGeneration() {
        var app = new Generations(8, 0.767f);

        var ans = new Generations().optimize(0.85f);
        System.out.println(ans);
//        var ans1 = app.infPBA();
//        System.out.println(ans1); // 可信度
    }
}
