package cn.edu.ruc.hola.exec

import cn.edu.ruc.hola.config.Config
import cn.edu.ruc.hola.utils.LoggingUtils
import cn.edu.ruc.hola.entity.Response

/**
 *
 *
val config = Config(0.95f, 1000, 100, 80)
// the condition we are using , and now is couchdb
val query = """
{
"selector": {
"data.countryid": {"${'$'}gt": -1}
},
"execution_stats": true
}
""".trimIndent()
val ans = queryWithTry(config, query)
ans?.let {
println(it)
}
 */
object QueryWithMulti {
    private val logger = LoggingUtils.logger()
    var RetryCnt = 5

    /**
     * the entry method , core method
     */
    fun queryWithTry(config: Config, condition: String): Response? {
        for (i in 0 until RetryCnt) {
            val res = query(config, condition)
            if (Response.SUCCESS_CODE == res.code) {
                logger.info("=========$i=========")
                return res
            }
        }
        return null
    }

    fun query(config: Config, condition: String): Response {
        val exec = MultiExec(config)
        return exec.partition(condition)!!
    }
}