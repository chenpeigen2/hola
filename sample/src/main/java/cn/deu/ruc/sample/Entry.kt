package cn.deu.ruc.sample

import io.vertx.core.AbstractVerticle
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.StaticHandler

class EntryVerticle : AbstractVerticle() {
    override fun start() {
        super.start()
        val server = vertx.createHttpServer()

        val router = Router.router(vertx)
        router.route().handler(BodyHandler.create())
        router.route().handler(StaticHandler.create())


        router.post("/performance").handler { ctx: RoutingContext ->
            val requestBody = ctx!!.body()

            val jsonObject = requestBody.asJsonObject()
            val threshHold = jsonObject.getFloat("th")
            val M = jsonObject.getInteger("M")
            val H = jsonObject.getInteger("H")
            val query = jsonObject.getJsonObject("query")
            println(jsonObject)
            println(threshHold)
            println(M)
            println(H)
            println(query)
            out(ctx, QuerySq.query())
        }

        server.requestHandler(router).listen(8081)
    }

    private fun out(ctx: RoutingContext, msg: String) {
        ctx.response().putHeader("Content-Type", "application/json; charset=utf-8")
            .end(msg)
    }
}

fun main() {
    val vertx = Vertx.vertx()
    vertx.deployVerticle(EntryVerticle::class.java.name)
}