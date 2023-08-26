package cn.ruc.edu.hola.client.web;

import cn.ruc.edu.hola.client.performance.*;
import cn.ruc.edu.hola.client.performance.Package;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class PerformanceVerticle extends Verticle {

    final static int PORT = 3002;

    @Override
    public void start() throws Exception {
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route().handler(StaticHandler.create());

        // define yr own query process
        router.post("/_find").handler(ctx -> {
            var postData = ctx.body().asString();
            postData += "\n";
            postData += Thread.currentThread().getName();
            out(ctx, postData);
        });

        router.post("/_find1").blockingHandler(ctx -> {

//            HttpServerResponse response = ctx.response();
//            response.setChunked(true);
//            var postData = ctx.body().asString();
//            postData += Thread.currentThread().getName();
//
//            BackgroudThread thread = (BackgroudThread) BackgroudThread.INSTANCE;
//
//            EventBus eb = vertx.eventBus();
//            String thread_name = Thread.currentThread().getName();
//
//            eb.send("ping-address", new Package(thread_name));
//
//            // then we need to subscribe the response
//            eb.consumer(thread_name, message -> {
//                System.out.println("Consumer news on consumer 1: " + message.body());
//                out(ctx, "77788899");
//            });
//            eb.u


//            boolean submit = thread.submit(cargo);
//            if (submit) {
//                for (; !thread.isFinish(cargo); ) {
//                    System.out.println(thread.isFinish(cargo));
//                }
//                ;
//                System.out.println(cargo);
//            }

            System.out.println(Thread.currentThread().getName());
//
            PerformanceHandler handler = (PerformanceHandler) PerformanceHandler.getInstance();
//
//            String object = new String("" + Thread.currentThread().getName() + System.currentTimeMillis());

//            for (; !handler.hasResult(object); ) {
//
//            }

            Callback callback = new Callback() {
                @Override
                public void onFinish(String text) {
                    out(ctx, text);
                }
            };

            handler.offer(callback);


        });

//        https://blog.csdn.net/qq_40276626/article/details/120381161
        router.post("/blocing")
                .blockingHandler(ctx -> {
                    HttpServerResponse response = ctx.response();
                    response.setChunked(true);
                    for (int i = 0; i < 10; i++) {
                        try {
                            Thread.sleep(500L);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                        response.write("test " + i + "\n" + Thread.currentThread().getName());
                    }
                    ctx.end("over");
                }, true);

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
