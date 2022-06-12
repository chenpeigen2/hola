package cn.edu.ruc.hola.main;

import cn.edu.ruc.hola.config.Config;
import cn.edu.ruc.hola.exec.Exec;
import cn.edu.ruc.hola.exec.MultiExec;
import cn.edu.ruc.hola.utils.Response;

import java.util.logging.Logger;

public class QueryWithMulti {

    private static final Logger logger = Logger.getLogger("MultiQuery");
    static int RetryCnt;

    static {
        RetryCnt = 5;
    }

    public Response queryWithTry(Config config, String condition) {
        for (int i = 0; i < RetryCnt; i++) {
            Response res = query(config, condition);
            if (Response.SUCCESS_CODE == (Integer) res.get("code")) {
                logger.info("=========" + i + "=========");
                return res;
            }
        }
        return null;
    }

    public Response query(Config config, String condition) {
        var exec = new MultiExec(config);
        return exec.partition(condition);
    }
}
