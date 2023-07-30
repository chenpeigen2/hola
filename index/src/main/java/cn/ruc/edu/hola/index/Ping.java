package cn.ruc.edu.hola.index;

import java.io.IOException;

public final class Ping {

    private Ping() {}

    public static void pong() {

        Indexer2.registerPath.forEach((backBendConfigInfo, s) -> {
            var resp = HttpClient.INSTANCE.executePing(backBendConfigInfo.host(), backBendConfigInfo.port(), backBendConfigInfo.path().substring(1));
            try {
                var text = resp.body().string();
                if (!text.equals("pong")) {
                    Indexer2.unregister(backBendConfigInfo);
                }
                resp.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        System.out.println("pong !");
    }
}
