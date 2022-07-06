package cn.edu.ruc.hola.benchmark

import cn.edu.ruc.hola.config.Config
import cn.edu.ruc.hola.main.QueryWithMulti
import cn.edu.ruc.hola.utils.QuickQueue
import cn.edu.ruc.hola.utils.Response

object Bench {

    private fun QuickQueue.saveResult(res: Response) {
        inc(res["data"] as String)
    }

    private fun QuickQueue.readCredibility(len: Int): Float {
        return maxKeyCnt.toFloat() / len
    }

    private fun data(q: QuickQueue): String {
        return q.maxKey
    }


    fun executeSync(cnt: Int) {
        val config = Config(0.95f, 1000, 100, 80)
        val query = """
     {
     "selector": {
      "data.countryid": {"${'$'}gt": -1}
                            },
       "execution_stats": true
      }    
    """.trimIndent()

        val quickQueue = QuickQueue()

        repeat(cnt) {
            val ans = QueryWithMulti.queryWithTry(config, query)
            ans?.let {
                quickQueue.saveResult(it)
            }
        }

        val readCredibility = quickQueue.readCredibility(cnt)

        println(readCredibility)
    }
}

fun main() {
}