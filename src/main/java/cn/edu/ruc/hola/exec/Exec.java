package cn.edu.ruc.hola.exec;

import cn.edu.ruc.hola.config.Config;
import cn.edu.ruc.hola.entity.Node;
import lombok.Getter;
import cn.edu.ruc.hola.query.Query;
import cn.edu.ruc.hola.utils.*;
import cn.edu.ruc.hola.config.model.Pair;

import java.util.*;
import java.util.logging.Logger;

public class Exec implements Reachable {

    private static final Logger logger = Logger.getLogger("Exec");

    @Getter
    protected Config config;

    @Getter
    protected Query query;

    protected Pair pair;

    public Exec(Config config) {
        this.config = config;
        query = new Query(config);
    }

    /**
     * the core method
     */
    public Response partition(String condition) {
        pair = Generations2.optimize(config.getThreshold(), config.getM(), config.getH());
        List<String> cut_ids = ShuffleUtils.INSTANCE.randomChoose(sids(), pair.m());
        List<Node> queries = query.query(condition, cut_ids, "fake");
        return analyze(queries);
    }

    /**
     * @param queries the given queries to analyze
     */
    protected Response analyze(List<Node> queries) {
        QuickQueue quickQueue = new QuickQueue();

        int len = queries.size();

        // data 数据
        queries.forEach(query -> {
            quickQueue.inc(query.getValue());
        });

        int MAX_CNT = quickQueue.getMaxKeyCnt();
        String DATA = quickQueue.getMaxKey();
        String en = SHA256Utils.INSTANCE.getSHA256Str(DATA);

        float percent = (float) MAX_CNT / len;

        logger.info("MAX_CNT = " + MAX_CNT + ", len = " + len + " , percent = " + percent);

        if (percent < 0.5f) {
            return Response.ErrorCode();
        }

        if (percent < pair.λ()) {
            return Response.WeakErrorCode();
        }

        logger.info(pair.toString());

        Map<String, String> m = new HashMap<>();
        m.put(en, DATA); //  but why

        m.put("θ", String.valueOf(Generations2.infPBA(len, percent, config.getM(), config.getH())));

        return Response.SuccessCode(m);
    }

    @Override
    public List<String> sids() {
        return JsonUtils.sids();
    }

}
