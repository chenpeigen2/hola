package cn.ruc.edu.hola.client.web;

import cn.ruc.edu.hola.client.ApiService;
import cn.ruc.edu.hola.client.Entry;
import cn.ruc.edu.hola.client.bridge.ChainCodeBridge;
import cn.ruc.edu.hola.client.entity.Request;
import cn.ruc.edu.hola.client.utils.RandomStringGenerator;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.ArrayList;
import java.util.List;

public class Verticle extends AbstractVerticle {
    final static int PORT = 3001;

    private final ApiService impl = ApiService.getInstance();

    @Override
    public void start() throws Exception {
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route().handler(StaticHandler.create());

        router.get("/trigger_new").handler(ctx -> {
            System.out.println("hello world");
            ChainCodeBridge bridge = new ChainCodeBridge();
//            Entry.triggerClient(); // trigger the query

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        bridge.trigger(); // trigger the chaincode
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                }
            }).start();

            out(ctx, "trigger success");
        });

        // delegate the query process
        router.get("/trigger_invoke_query").handler(ctx -> {
            impl.update("http", "127.0.0.1", 8888);
            List<Request> requests = new ArrayList<>();
            requests.add(new Request(RandomStringGenerator.UID, Entry.Action.query));
            var text = impl.complex(requests).getDecryptResponse();
            out(ctx, text);
        });

        router.get("/trigger_invoke_get_queries").handler(ctx -> {
            ChainCodeBridge bridge = new ChainCodeBridge();
            String resp = null;
            try {
                resp = bridge.invokeGetAllQueries();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            out(ctx, resp);
        });

        server.requestHandler(router).listen(PORT);
    }

    private void out(RoutingContext ctx, String msg) {
        if (msg == null) {
            ctx.fail(400);
            return;
        }
        ctx.response().putHeader("Content-Type", "application/json; charset=utf-8")
                .end(msg);
    }
}
