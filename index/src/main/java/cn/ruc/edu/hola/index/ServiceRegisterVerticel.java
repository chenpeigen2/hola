package cn.ruc.edu.hola.index;

import cn.ruc.edu.hola.index.chaincode.ChainCodeBridge;
import cn.ruc.edu.hola.index.chaincode.Service;
import cn.ruc.edu.hola.index.json.JsonUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.UUID;

public class ServiceRegisterVerticel extends AbstractVerticle {
    final static int PORT = 2999;

    final ChainCodeBridge impl = new ChainCodeBridge();

    @Override
    public void start() throws Exception {
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route().handler(StaticHandler.create());

        router.get("/register").handler(ctx -> {
            Service ans;
            try {
                ans = impl.register(UUID.randomUUID().toString(), UUID.randomUUID().toString());
                System.out.println(JsonUtils.INSTANCE.getJson().toJson(ans));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            out(ctx, JsonUtils.INSTANCE.getJson().toJson(ans));
        });

        server.requestHandler(router).listen(PORT);
    }

    private void out(RoutingContext ctx, String msg) {
        if (msg == null) {
            ctx.fail(400);
            return;
        }
        System.err.println(msg);
        ctx.response().putHeader("Content-Type", "application/json; charset=utf-8")
                .end(msg);
    }
}
