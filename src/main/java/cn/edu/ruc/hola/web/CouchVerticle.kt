package cn.edu.ruc.hola.web

import cn.edu.ruc.hola.benchmark.Bench.basicExecute
import cn.edu.ruc.hola.benchmark.Bench.executeSync
import cn.edu.ruc.hola.benchmark.Inspect
import com.google.gson.GsonBuilder
import io.vertx.core.AbstractVerticle
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.StaticHandler
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

internal class CouchVerticle : AbstractVerticle() {
    private val json = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()
    override fun start(): Unit = runBlocking {
        val server = vertx.createHttpServer()
        val router = Router.router(vertx)
        router.route().handler(BodyHandler.create())
        router.route().handler(StaticHandler.create())
        router["/hello"].handler { ctx: RoutingContext -> out(ctx, "helloworld11") }
        router["/query"].handler { ctx: RoutingContext -> launch { query(ctx) } }
        router["/basic"].handler { ctx: RoutingContext -> launch { basicQuery(ctx) } }
        router.post("/postQuery").handler { ctx: RoutingContext -> postQuery(ctx) }
        server.requestHandler(router).listen(8080)
    }

    /**
     * sample parameters
     * 0.95 100 80
     *
     *
     * condition:
     * {
     * "selector": {
     * "data.countryid": {"$gt": -1}
     * },
     * "execution_stats": true
     * }
     *
     *
     * {"selector": {"data.countryid": {"$gt": -1}},"execution_stats": true}
     *
     *
     * the sample url:
     * http://localhost:8080/basic?th=0.95&M=100&H=80&query={%22selector%22:%20{%22data.countryid%22:%20{%22$gt%22:%20-1}},%22execution_stats%22:%20true}
     */
    private suspend fun basicQuery(ctx: RoutingContext) {
        val request = ctx.request()
        val threshHold = request.getParam("th").toFloat()
        val M = request.getParam("M").toInt()
        val H = request.getParam("H").toInt()
        val query = request.getParam("query")
        val response = basicExecute(threshHold, M, H, query)
        out(ctx, json.toJson(response))
    }

    private fun postQuery(ctx: RoutingContext) = runBlocking {
        val requestBody = ctx.body()
        val jsonObject = requestBody.asJsonObject()
        val threshHold = jsonObject.getFloat("th")
        val M = jsonObject.getInteger("M")
        val H = jsonObject.getInteger("H")
        val query = jsonObject.getJsonObject("query")
        val response = basicExecute(threshHold, M, H, query.toString())
        out(ctx, json.toJson(response))
    }

    /**
     * {
     * "th" : 0.95,
     * "M" : 100,
     * "H" : 80,
     * "query" : {"selector":{"data.countryid":{"$gt":-1}},"execution_stats":true}
     * }
     */
    private suspend fun query(ctx: RoutingContext) {
        val cnt = ctx.request().getParam("cnt")
        val inspect: Inspect
        inspect = if (cnt == null) {
            executeSync(1)
        } else {
            executeSync(cnt.toInt())
        }
        out(ctx, json.toJson(inspect))
    }

    private fun out(ctx: RoutingContext, msg: String) {
        ctx.response().putHeader("Content-Type", "application/json; charset=utf-8")
            .end(msg)
    }
}