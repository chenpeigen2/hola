package cn.edu.ruc.hola.web;

import cn.edu.ruc.hola.benchmark.Bench;
import cn.edu.ruc.hola.benchmark.Inspect;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;


public class CouchVerticle extends AbstractVerticle {

    private final Gson json = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    @Override
    public void start() {
        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route().handler(StaticHandler.create());
        router.get("/query").handler(this::query);
        server.requestHandler(router).listen(8080);
    }

//    kotlin.UninitializedPropertyAccessException: lateinit property docs has not been initialized
//    at cn.edu.ruc.hola.models.couchdb.FilterDocs.getDocs(FilterDocs.kt:4)
//    at cn.edu.ruc.hola.service.CouchDbExtension.query(CouchDbExtension.kt:34)
//    at cn.edu.ruc.hola.service.CouchService.query(CouchService.kt:15)
//    at cn.edu.ruc.hola.query.Query$executeAsync$1$1$1.invokeSuspend(Query.kt:31)
//    at kotlin.coroutines.jvm.internal.BaseContinuationImpl.resumeWith(ContinuationImpl.kt:33)
//    at kotlinx.coroutines.DispatchedTask.run(DispatchedTask.kt:106)
//    at kotlinx.coroutines.EventLoopImplBase.processNextEvent(EventLoop.common.kt:284)
//    at kotlinx.coroutines.BlockingCoroutine.joinBlocking(Builders.kt:85)
//    at kotlinx.coroutines.BuildersKt__BuildersKt.runBlocking(Builders.kt:59)
//    at kotlinx.coroutines.BuildersKt.runBlocking(Unknown Source)
//    at kotlinx.coroutines.BuildersKt__BuildersKt.runBlocking$default(Builders.kt:38)
//    at kotlinx.coroutines.BuildersKt.runBlocking$default(Unknown Source)
//    at cn.edu.ruc.hola.query.Query.executeAsync(Query.kt:27)
//    at cn.edu.ruc.hola.query.Query.query(Query.kt:23)
//    at cn.edu.ruc.hola.exec.Exec.partition(Exec.kt:32)
//    at cn.edu.ruc.hola.exec.QueryWithMulti.query(QueryWithMulti.kt:45)
//    at cn.edu.ruc.hola.exec.QueryWithMulti.queryWithTry(QueryWithMulti.kt:34)
//    at cn.edu.ruc.hola.benchmark.Bench.executeSync(Bench.kt:48)
//    at cn.edu.ruc.hola.web.CouchVerticle.query(CouchVerticle.java:37)
//    at io.vertx.ext.web.impl.RouteState.handleContext(RouteState.java:1284)
//    at io.vertx.ext.web.impl.RoutingContextImplBase.iterateNext(RoutingContextImplBase.java:173)
//    at io.vertx.ext.web.impl.RoutingContextImpl.next(RoutingContextImpl.java:140)
//    at io.vertx.ext.web.handler.impl.StaticHandlerImpl.sendStatic(StaticHandlerImpl.java:232)
//    at io.vertx.ext.web.handler.impl.StaticHandlerImpl.handle(StaticHandlerImpl.java:191)
//    at io.vertx.ext.web.handler.impl.StaticHandlerImpl.handle(StaticHandlerImpl.java:73)
//    at io.vertx.ext.web.impl.RouteState.handleContext(RouteState.java:1284)
//    at io.vertx.ext.web.impl.RoutingContextImplBase.iterateNext(RoutingContextImplBase.java:173)
//    at io.vertx.ext.web.impl.RoutingContextImpl.next(RoutingContextImpl.java:140)
//    at io.vertx.ext.web.handler.impl.BodyHandlerImpl.handle(BodyHandlerImpl.java:93)
//    at io.vertx.ext.web.handler.impl.BodyHandlerImpl.handle(BodyHandlerImpl.java:46)
//    at io.vertx.ext.web.impl.RouteState.handleContext(RouteState.java:1284)
//    at io.vertx.ext.web.impl.RoutingContextImplBase.iterateNext(RoutingContextImplBase.java:173)
//    at io.vertx.ext.web.impl.RoutingContextImpl.next(RoutingContextImpl.java:140)
//    at io.vertx.ext.web.impl.RouterImpl.handle(RouterImpl.java:68)
//    at io.vertx.ext.web.impl.RouterImpl.handle(RouterImpl.java:37)
//    at io.vertx.core.http.impl.Http1xServerRequestHandler.handle(Http1xServerRequestHandler.java:67)
//    at io.vertx.core.http.impl.Http1xServerRequestHandler.handle(Http1xServerRequestHandler.java:30)
//    at io.vertx.core.impl.EventLoopContext.emit(EventLoopContext.java:56)
//    at io.vertx.core.impl.DuplicatedContext.emit(DuplicatedContext.java:174)
//    at io.vertx.core.http.impl.Http1xServerConnection.handleMessage(Http1xServerConnection.java:145)
//    at io.vertx.core.net.impl.ConnectionBase.read(ConnectionBase.java:155)
//    at io.vertx.core.net.impl.VertxHandler.channelRead(VertxHandler.java:153)
//    at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379)
//    at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365)
//    at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357)
//    at io.netty.channel.ChannelInboundHandlerAdapter.channelRead(ChannelInboundHandlerAdapter.java:93)
//    at io.netty.handler.codec.http.websocketx.extensions.WebSocketServerExtensionHandler.channelRead(WebSocketServerExtensionHandler.java:99)
//    at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379)
//    at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365)
//    at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357)
//    at io.netty.handler.codec.ByteToMessageDecoder.fireChannelRead(ByteToMessageDecoder.java:327)
//    at io.netty.handler.codec.ByteToMessageDecoder.channelRead(ByteToMessageDecoder.java:299)
//    at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379)
//    at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365)
//    at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357)
//    at io.netty.channel.DefaultChannelPipeline$HeadContext.channelRead(DefaultChannelPipeline.java:1410)
//    at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379)
//    at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365)
//    at io.netty.channel.DefaultChannelPipeline.fireChannelRead(DefaultChannelPipeline.java:919)
//    at io.netty.channel.nio.AbstractNioByteChannel$NioByteUnsafe.read(AbstractNioByteChannel.java:166)
//    at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:722)
//    at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:658)
//    at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:584)
//    at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:496)
//    at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:995)
//    at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
//    at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
//    at java.base/java.lang.Thread.run(Thread.java:833)

    private void query(RoutingContext ctx) {
        String cnt = ctx.request().getParam("cnt");
        Inspect inspect;
        if (cnt == null) {
            inspect = Bench.INSTANCE.executeSync(1);
        } else {
            inspect = Bench.INSTANCE.executeSync(Integer.parseInt(cnt));
        }
        out(ctx, json.toJson(inspect));
    }

    private void out(RoutingContext ctx, String msg) {
        ctx.response().putHeader("Content-Type", "application/json; charset=utf-8")
                .end(msg);
    }
}
