package cn.edu.ruc.hola.exec;

import cn.edu.ruc.hola.config.Config;
import cn.edu.ruc.hola.entity.Node;
import cn.edu.ruc.hola.utils.Generations2;
import cn.edu.ruc.hola.utils.Response;
import cn.edu.ruc.hola.utils.ShuffleUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MultiExec extends Exec {

    private static final int DEFAULT_CNT = 10;

    public MultiExec(Config config) {
        super(config);
    }


    public List<String> getDBNames(int cnt) {
        List<String> l = new ArrayList<>();
        for (int i = 0; i < cnt; i++) {
            l.add("fake" + i);
        }
        return l;
    }

    @Override
    public Response partition(String condition) {
        pair = Generations2.optimize(config.getThreshold(), config.getM(), config.getH());
        List<String> cut_ids = ShuffleUtils.INSTANCE.randomChoose(sids(), pair.m());
        List<Node> queries = query.queries(condition, cut_ids, getDBNames(DEFAULT_CNT));

        System.out.println(queries.size());
        queries.forEach(new Consumer<Node>() {
            @Override
            public void accept(Node node) {
                System.out.println(node);
            }
        });
        return analyze(queries);
    }
}
