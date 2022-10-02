package cn.edu.ruc.hola.exec

import cn.edu.ruc.hola.config.Config
import cn.edu.ruc.hola.config.QueryConfig
import cn.edu.ruc.hola.config.ServerHelper
import cn.edu.ruc.hola.config.ServerNode
import cn.edu.ruc.hola.entity.Node
import cn.edu.ruc.hola.entity.Response
import cn.edu.ruc.hola.entity.Results
import cn.edu.ruc.hola.fake.FakeConfig
import cn.edu.ruc.hola.query.Query
import cn.edu.ruc.hola.utils.Debug
import cn.edu.ruc.hola.utils.Generations2
import cn.edu.ruc.hola.utils.LoggingUtils
import cn.edu.ruc.hola.utils.QuickQueue
import cn.edu.ruc.hola.utils.anno.Core
import cn.edu.ruc.hola.utils.anno.PerformanceOnly

open class Exec(protected val config: Config) : Reachable {

    private val logger = LoggingUtils.logger()


    protected val query: Query = Query(config)
    private val queryConfig: QueryConfig
        get() {
            return config.queryConfig
        }

    /**
     * the core method
     */
    @Core
    open fun exe(condition: String): Response? {
        val cutIds: List<ServerNode> = randomChoose(serverNodes(), queryConfig.m)
        val queries = query.query(condition, cutIds)
        return analyze(queries)
    }

    @PerformanceOnly
    open fun performance(condition: String? = null) {
        val cutIds: List<ServerNode> = randomChoose(serverNodes(), queryConfig.m)
        query.performance(condition, cutIds)
    }

    /**
     * @param queries the given queries to analyze
     */
    private fun analyze(queries: List<Node>): Response {
        val quickQueue = QuickQueue()
        val len = queries.size
        // data 数据
        queries.forEach { query: Node ->
            quickQueue.inc(
                query.value
            )
        }
        val maxCnt = quickQueue.maxKeyCnt
        val data = quickQueue.maxKey
        val percent = maxCnt.toFloat() / len
        if (percent < 0.5f) {
            return Response.ErrorCode()
        }
        if (Debug.enabled) {
            logger.info("maxCnt = $maxCnt, len = $len , percent = $percent")
            logger.info(queryConfig.toString())
        }
        if (percent < queryConfig.λ) {
            return Response.WeakErrorCode()
        }
        val results = Results(data, Generations2.evaluate(len, percent, config.M, config.H))
        return Response.SuccessCode(results)
    }

    /** make cache for snodes */
    protected var snodes: MutableList<ServerNode>? = null

    override fun serverNodes(): List<ServerNode> {
        if (snodes != null) return snodes as MutableList<ServerNode>
        snodes = mutableListOf()
        ServerHelper.sids.forEach {
            snodes!!.add(ServerNode(it, FakeConfig.databaseName))
        }
        return snodes as MutableList<ServerNode>
    }

    private fun randomChoose(nodes: List<ServerNode>, cnt: Int): List<ServerNode> {
        return nodes.shuffled().subList(0, cnt)
    }
}