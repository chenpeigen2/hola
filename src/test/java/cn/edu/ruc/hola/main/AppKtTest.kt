package cn.edu.ruc.hola.main

import cn.edu.ruc.hola.benchmark.StopWatch
import cn.edu.ruc.hola.config.Config
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test

internal class AppKtTest {

    @BeforeEach
    fun beforeTest() {
        StopWatch.start()
    }

    @Test
    fun test01() {
        println(222)
    }

    @RepeatedTest(100)
    fun testMain() {
        val config = Config(0.8f, 1000, 100, 80)
        val query = """
     {
     "selector": {
      "data.countryid": {"${'$'}gt": -1}
                            },
       "execution_stats": true
      }    
    """.trimIndent()
        val ans = QueryWithMulti.queryWithTry(config, query)
        ans?.let {
            println(it)
        }
    }


    @RepeatedTest(10000)
    fun test02() {
        println(1)
    }

    @AfterEach
    fun afterTest() {
        StopWatch.stop()
    }
}