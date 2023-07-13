package cn.ruc.edu.hola.client.web;

import cn.ruc.edu.hola.client.bridge.ChainCodeBridge;
import cn.ruc.edu.hola.client.gateway.AssetTransferImpl;
import cn.ruc.edu.hola.client.impl.SgxConfig;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import org.hyperledger.fabric.client.CommitException;
import org.hyperledger.fabric.client.CommitStatusException;
import org.hyperledger.fabric.client.EndorseException;
import org.hyperledger.fabric.client.SubmitException;

import java.util.Date;
import java.util.UUID;

public class Verticle extends AbstractVerticle {
    final static int PORT = 3001;

    public static void main(String[] args) throws EndorseException, CommitException, SubmitException, CommitStatusException {
        new AssetTransferImpl(null);
    }

    @Override
    public void start() throws Exception {
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route().handler(StaticHandler.create());

        router.get("/trigger").handler(ctx -> {
            System.out.println("hello world");
            ChainCodeBridge bridge = new ChainCodeBridge();
//            Entry.triggerClient(); // trigger the query

            new Thread(new Runnable() {
                @Override
                public void run() {
                    bridge.triggerChaincode(); // trigger the chaincode

                }
            }).start();

            out(ctx, "trigger success");
        });

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

        router.get("/trigger_invoke_query").handler(ctx -> {
            ChainCodeBridge bridge = new ChainCodeBridge();
            String resp = null;
            try {
                resp = bridge.invokeQuery(new SgxConfig("shche", "host", 8080), UUID.randomUUID().toString(), "owner", new Date().toString(), "query", "resp");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            out(ctx, resp);
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
        System.err.println(msg);
        ctx.response().putHeader("Content-Type", "application/json; charset=utf-8")
                .end(msg);
    }
}
