package cn.edu.ruc.hola.main

import cn.edu.ruc.hola.config.Config
import cn.edu.ruc.hola.main.QueryWithMulti.queryWithTry

fun main() {
    val config = Config(0.8f, 1000, 10, 8)
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
    if (ans != null) {
        println("============ result ============")
        println(ans)
    }
}