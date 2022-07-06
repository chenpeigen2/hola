package cn.edu.ruc.hola.main

import cn.edu.ruc.hola.benchmark.StopWatch
import cn.edu.ruc.hola.config.Config
import cn.edu.ruc.hola.main.QueryWithMulti.queryWithTry

fun main() {
    StopWatch.start()
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
    StopWatch.stop()
}