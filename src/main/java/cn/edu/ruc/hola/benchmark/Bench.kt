package cn.edu.ruc.hola.benchmark

import cn.edu.ruc.hola.config.Config
import cn.edu.ruc.hola.entity.Response
import cn.edu.ruc.hola.exec.QueryWithMulti
import cn.edu.ruc.hola.utils.LoggingUtils
import cn.edu.ruc.hola.utils.QuickQueue
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.util.logging.Logger

/**
 * in bench, we use the running block
 */
object Bench {

    private val logger: Logger = LoggingUtils.logger()

    fun QuickQueue.saveResult(res: Response) {
        val results = res.data
        inc(results.data)
    }

    fun QuickQueue.readCredibility(len: Int): Float {
        return maxKeyCnt.toFloat() / len
    }

    fun data(q: QuickQueue): String {
        return q.maxKey
    }

    fun basicExecute(threshHold: Float, M: Int, H: Int, query: String): Response? = runBlocking {
        val config = async { Config(threshHold, 1000, M, H) }
        logger.info(config.toString())
        logger.info(query)
        return@runBlocking QueryWithMulti.queryWithTry(config.await(), query)
    }

    fun executeSync(cnt: Int = 1): Inspect = runBlocking {

        val execTimes = mutableListOf<Long>()
        val theoreticalCredibility = mutableListOf<Double>()

        val config = Config(0.95f, 1000, 100, 80)
        logger.info(config.toString())
        val query = """
     {
     "selector": {
      "data.countryid": {"${'$'}gt": -1}
                            },
       "execution_stats": true
      }    
    """.trimIndent()
        logger.info(query)
        val quickQueue = QuickQueue()

        repeat(cnt) {
            StopWatch.start()
            val ans = QueryWithMulti.queryWithTry(config, query)
            execTimes.add(StopWatch.stop())
            theoreticalCredibility.add(ans?.data?.θ ?: -1.0)
            ans?.let {
                quickQueue.saveResult(it)
            }
        }

        val readCredibility = quickQueue.readCredibility(cnt)
        logger.info(readCredibility.toString())
        return@runBlocking Inspect(execTimes, readCredibility, theoreticalCredibility, data(quickQueue))
    }
}

fun main() {
    val inspect = Bench.executeSync(1000)
    println(inspect)
    println(inspect.realCredibility)
    println(inspect.avgExecTimes)
}