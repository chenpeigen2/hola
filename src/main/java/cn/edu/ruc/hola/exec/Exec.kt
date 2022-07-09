package cn.edu.ruc.hola.exec

import cn.edu.ruc.hola.config.Config
import cn.edu.ruc.hola.config.QueryConfig
import cn.edu.ruc.hola.entity.Node
import cn.edu.ruc.hola.query.Query
import cn.edu.ruc.hola.utils.Generations2
import cn.edu.ruc.hola.config.ServerHelper
import cn.edu.ruc.hola.config.ServerNode
import cn.edu.ruc.hola.entity.Results
import cn.edu.ruc.hola.fake.FakeConfig
import cn.edu.ruc.hola.utils.QuickQueue
import cn.edu.ruc.hola.entity.Response
import java.util.logging.Logger

open class Exec(protected val config: Config) : Reachable {

    private val logger = Logger.getLogger("Exec")


    protected val query: Query = Query(config)
    private val queryConfig: QueryConfig
        get() {
            return config.queryConfig
        }

    /**
     * the core method
     */
    open fun partition(condition: String): Response? {
        val cutIds: List<ServerNode> = randomChoose(serverNodes(), queryConfig.m)
        val queries = query.query(condition, cutIds)
        return analyze(queries)
    }

    /**
     * @param queries the given queries to analyze
     */
    private fun analyze(queries: List<Node>): Response {
        val quickQueue = QuickQueue()
        val len = queries.size
        // data 数据
        queries.forEach { query: Node ->
            print(query)
            quickQueue.inc(
                query.value
            )
        }
        val maxCnt = quickQueue.maxKeyCnt
        val data = quickQueue.maxKey

        val percent = maxCnt.toFloat() / len
        logger.info("MAX_CNT = $maxCnt, len = $len , percent = $percent")
        if (percent < 0.5f) {
            return Response.ErrorCode()
        }
        println("MAX_CNT = $maxCnt, len = $len , percent = $percent")
        println(queryConfig)
        if (percent < queryConfig.λ) {
            return Response.WeakErrorCode()
        }
        logger.info(queryConfig.toString())
        val results = Results(data, Generations2.infPBA(len, percent, config.M, config.H))
        return Response.SuccessCode(results)
    }

    override fun serverNodes(): List<ServerNode> {
        val snods = mutableListOf<ServerNode>()
        ServerHelper.sids.forEach {
            snods.add(ServerNode(it, FakeConfig.databaseName))
        }
        return snods
    }

    private fun randomChoose(nodes: List<ServerNode>, cnt: Int): List<ServerNode> {
        return nodes.shuffled().subList(0, cnt)
    }
}