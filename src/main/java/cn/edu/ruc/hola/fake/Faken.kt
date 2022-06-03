package cn.edu.ruc.hola.fake

import cn.edu.ruc.hola.config.model.Host
import cn.edu.ruc.hola.utils.DbUtils
import cn.edu.ruc.hola.utils.JsonUtils
import cn.edu.ruc.hola.utils.ShuffleUtils
import java.util.Collections
import java.util.logging.Logger

object Faken {

    private val logger = Logger.getLogger("Faken")

    /**
     * make cnt to repeat create
     */
    fun createFakeDb(host: Host, cnt: Int) {
        val dbUtils = DbUtils.checkDbUtils(host)
        repeat(cnt) {
            dbUtils.createDb("fake$it").use { res ->
                val ans = res?.body?.string() ?: "null"
                logger.info(ans)
            }
        }
    }

    /**
     * with repeat count 10 by default
     */
    fun createFakeDb() {
        JsonUtils.readProps().forEach {
            it?.let {
                createFakeDb(it, 10)
            }
        }
    }

    /**
     * make cnt to repeat delete
     */
    fun deleteFakeDb(host: Host, cnt: Int) {
        val dbUtils = DbUtils.checkDbUtils(host)
        repeat(cnt) {
            dbUtils.deleteDb("fake$it").use { res ->
                logger.info("host at : $host")
                val ans = res?.body?.string() ?: "null"
                logger.info(ans)
            }
        }
    }

    /**
     * with repeat count 10 by default
     */
    fun deleteFakeDb() {
        JsonUtils.readProps().forEach {
            it?.let {
                deleteFakeDb(it, 10)
            }
        }
    }

    /**
     * generate the fake data and then shuffle
     */
    fun generateFakeData(cnt: Int, threshold: Float = 0.8f, conspired: Boolean = false): List<String> {
        val size = JsonUtils.readProps()?.size ?: 0
        val l = FakeData.basicFake(size * cnt, threshold, conspired)
        l.shuffle() // shuffle it
        return l
    }

    fun addFakeData() {
        JsonUtils.readProps().forEach { host ->
            repeat(10) { cnt ->

            }
        }
    }

}

fun main() {
    repeat(10) {
        println(it)
    }
}