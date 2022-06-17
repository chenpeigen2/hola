package cn.edu.ruc.hola.fake

import cn.edu.ruc.hola.config.Host
import cn.edu.ruc.hola.entity.Node
import cn.edu.ruc.hola.service.extension.couchdb.models.FilterDocs
import cn.edu.ruc.hola.utils.DbUtils
import cn.edu.ruc.hola.config.ServiceIdHelper
import java.util.logging.Logger

object Faken {

    private val logger = Logger.getLogger("Faken")

    /**
     * make cnt to repeat create
     */
    private fun createFakeDb(host: Host, cnt: Int) {
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
    fun createFakeDb(cnt: Int) {
        ServiceIdHelper.hosts.forEach {
            createFakeDb(it, cnt)
        }
    }

    /**
     * make cnt to repeat delete
     */
    private fun deleteFakeDb(host: Host, cnt: Int) {
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
    fun deleteFakeDb(cnt: Int) {
        ServiceIdHelper.hosts.forEach {
            deleteFakeDb(it, cnt)
        }
    }

    /**
     * generate the fake data and then shuffle
     */
    private fun generateFakeData(cnt: Int, threshold: Float = 0.8f, conspired: Boolean = false): List<String> {
        val size = ServiceIdHelper.hosts.size
        val l = FakeData.basicFake(size * cnt, threshold, conspired)
        l.shuffle() // shuffle it
        return l
    }

    /**
     * very complex operation , do not change
     */
    fun addFakeData(cnt: Int) {
        val props = ServiceIdHelper.hosts
        val fakeData: List<String> = generateFakeData(cnt)
        for (idx in props.indices) {
            val host: Host = props[idx]
            val dbUtils = DbUtils.checkDbUtils(host)
            repeat(cnt) {
                val data = fakeData[idx * cnt + it]
                dbUtils.addDoc("fake$it", data).use { res ->
                    val bos = res?.body?.string()
                    logger.info("$host")
                    logger.info("data : $data")
                    logger.info(bos)
                }
            }
        }
    }

    fun createFakeIndex(cnt: Int) {

        val info: String = """
                        {
                        "index": {
                            "fields": ["data.countryid"]
                        },
                        "name" : "data.countryid-index",
                        "type" : "json"
                    }        
            
        """.trimIndent()

        ServiceIdHelper.hosts.forEach {
            it.let { host ->
                val dbUtils = DbUtils.checkDbUtils(host)
                repeat(cnt) { cnt ->
                    dbUtils.createIndex("fake$cnt", info).use { res ->
                        val bos = res?.body?.string()
                        logger.info("$host")
                        logger.info(bos)
                    }
                }
            }
        }
    }

    fun fakeComplexQuery(cnt: Int): List<Node> {
        val query = """
                   {
                "selector": {
                        "data.countryid": {"${'$'}gt": -1}
                    },
                    "execution_stats": true
                }         
        """.trimIndent()
        val l = mutableListOf<Node>()
        ServiceIdHelper.hosts.forEach {
            it.let { host ->
                val dbUtils = DbUtils.checkDbUtils(host)
                repeat(cnt) {
                    dbUtils.filterDocs("fake$it", query).use { res ->
                        val result = res?.body?.string()
                        logger.info("$host fake$it $result")
                        val data = ServiceIdHelper.gson
                            .fromJson(result, FilterDocs::class.java).docs[0]
                            .data.toString()
                        val n = Node(query, data)
                        l.add(n)
                    }
                }
            }
        }
        return l
    }


}

fun main() {
    val cnt = 10
//    Faken.createFakeDb(cnt)

//    Faken.createFakeIndex(cnt)

//    Faken.addFakeData(cnt)
    val ans = Faken.fakeComplexQuery(cnt)
    print(ans.size)
}