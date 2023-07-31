package cn.ruc.edu.hola.index;

import java.util.logging.Logger;

public final class Ping {
    private final static Logger logger = Logger.getLogger(Ping.class.getSimpleName());

    private Ping() {}

    public static void pong() {

        var it = Indexer2.registerPath.entrySet().iterator();
        while (it.hasNext()) {
            var mm = it.next();
            try {
                System.out.println(mm.getKey().toString());
                var backBendConfigInfo = mm.getKey();
                var resp = HttpClient.INSTANCE.executePing(backBendConfigInfo.host(), backBendConfigInfo.port(), backBendConfigInfo.path().substring(1));
                var text = resp.body().string();
                if (!text.equals("pong")) {
                    Indexer2.unregister(mm.getValue());
                    it.remove();
                }
                resp.close();
            } catch (Exception e) {
                logger.info(e.toString());
                Indexer2.unregister(mm.getValue());
                it.remove();
            }
        }

        System.out.println("pong !");
    }
}
