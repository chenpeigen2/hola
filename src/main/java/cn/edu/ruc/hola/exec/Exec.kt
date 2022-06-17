package cn.edu.ruc.hola.exec

import cn.edu.ruc.hola.config.Config
import cn.edu.ruc.hola.config.QueryConfig
import cn.edu.ruc.hola.entity.Node
import cn.edu.ruc.hola.query.Query
import cn.edu.ruc.hola.utils.Generations2
import cn.edu.ruc.hola.config.ServerHelper
import cn.edu.ruc.hola.config.ServerNode
import cn.edu.ruc.hola.utils.QuickQueue
import cn.edu.ruc.hola.utils.Response
import cn.edu.ruc.hola.utils.SHA256Utils.getSHA256Str
import cn.edu.ruc.hola.utils.ShuffleUtils.randomChoose
import java.util.logging.Logger

open class Exec(protected val config: Config) : Reachable {

    private val logger = Logger.getLogger("Exec")


    protected val query: Query = Query(config)
    protected val queryConfig: QueryConfig
        get() {
            return config.queryConfig
        }

    /**
     * the core method
     */
    open fun partition(condition: String): Response? {
        val cutIds: List<ServerNode> = randomChoose(serverNodes(), queryConfig.m)
        val queries = query.query(condition, cutIds, "fake")
        return analyze(queries)
    }

    /**
     * @param queries the given queries to analyze
     */
    protected fun analyze(queries: List<Node?>?): Response {
        val quickQueue = QuickQueue()
        val len = queries!!.size

        // data 数据
        queries.forEach { query: Node? ->
            quickQueue.inc(
                query!!.value
            )
        }
        val MAX_CNT = quickQueue.maxKeyCnt
        val DATA = quickQueue.maxKey
        val en = getSHA256Str(DATA)
        val percent = MAX_CNT.toFloat() / len
        logger.info("MAX_CNT = $MAX_CNT, len = $len , percent = $percent")
        if (percent < 0.5f) {
            return Response.ErrorCode()
        }
        if (percent < queryConfig.λ) {
            return Response.WeakErrorCode()
        }
        logger.info(queryConfig.toString())
        val m: MutableMap<String, String> = HashMap()
        m[en] = DATA //  but why
        m["θ"] = Generations2.infPBA(len, percent, config.M, config.H).toString()
        return Response.SuccessCode(m)
    }

    override fun serverNodes(): List<ServerNode> {
        val snods = mutableListOf<ServerNode>()
        ServerHelper.sids.forEach {
            snods.add(ServerNode(it, "fake"))
        }
        return snods
    }
}