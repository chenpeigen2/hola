package cn.edu.ruc.hola.fake

import cn.edu.ruc.hola.config.Host
import cn.edu.ruc.hola.entity.Node
import cn.edu.ruc.hola.models.couchdb.FilterDocs
import cn.edu.ruc.hola.utils.CouchDBUtils
import cn.edu.ruc.hola.config.ServerHelper
import kotlinx.coroutines.runBlocking
import java.util.logging.Logger

object Faken {

    private val logger = Logger.getLogger("Faken")

    /**
     * make cnt to repeat create
     */
    private suspend fun createFakeDb(host: Host, cnt: Int) {
        val dbUtils = CouchDBUtils.checkDbUtils(host)
        repeat(cnt) {
            dbUtils.createDb("fake$it").use { res ->
                val ans = res.body?.string() ?: "null"
                logger.info(ans)
            }
        }
    }

    /**
     * with repeat count 10 by default
     */
    suspend fun createFakeDb(cnt: Int) {
        ServerHelper.hosts.forEach {
            createFakeDb(it, cnt)
        }
    }

    /**
     * make cnt to repeat delete
     */
    private suspend fun deleteFakeDb(host: Host, cnt: Int) {
        val dbUtils = CouchDBUtils.checkDbUtils(host)
        repeat(cnt) {
            dbUtils.deleteDb("fake$it").use { res ->
                logger.info("host at : $host")
                val ans = res.body?.string() ?: "null"
                logger.info(ans)
            }
        }
    }

    /**
     * with repeat count 10 by default
     */
    suspend fun deleteFakeDb(cnt: Int) {
        ServerHelper.hosts.forEach {
            deleteFakeDb(it, cnt)
        }
    }

    /**
     * generate the fake data and then shuffle
     */
    private fun generateFakeData(cnt: Int, threshold: Float = 0.8f, conspired: Boolean = false): List<String> {
        val size = ServerHelper.hosts.size
        val l = FakeData.basicFake(size * cnt, threshold, conspired)
        l.shuffle() // shuffle it
        return l
    }

    /**
     * very complex operation , do not change
     */
    suspend fun addFakeData(cnt: Int, threshold: Float = 0.8f, conspired: Boolean = true) {
        val props = ServerHelper.hosts
        val fakeData: List<String> = generateFakeData(cnt, threshold, conspired)
        for (idx in props.indices) {
            val host: Host = props[idx]
            val dbUtils = CouchDBUtils.checkDbUtils(host)
            repeat(cnt) {
                val data = fakeData[idx * cnt + it]
                dbUtils.addDoc("fake$it", data).use { res ->
                    val bos = res.body?.string()
                    logger.info("$host")
                    logger.info("data : $data")
                    logger.info(bos)
                }
            }
        }
    }

    suspend fun createFakeIndex(cnt: Int) {

        val info: String = """
                        {
                        "index": {
                            "fields": ["data.countryid"]
                        },
                        "name" : "data.countryid-index",
                        "type" : "json"
                    }        
            
        """.trimIndent()

        ServerHelper.hosts.forEach {
            it.let { host ->
                val dbUtils = CouchDBUtils.checkDbUtils(host)
                repeat(cnt) { cnt ->
                    dbUtils.createIndex("fake$cnt", info).use { res ->
                        val bos = res.body?.string()
                        logger.info("$host")
                        logger.info(bos)
                    }
                }
            }
        }
    }

    suspend fun fakeComplexQuery(cnt: Int): List<Node> {
        val query = """
                   {
                "selector": {
                        "data.countryid": {"${'$'}gt": -1}
                    },
                    "execution_stats": true
                }         
        """.trimIndent()
        val l = mutableListOf<Node>()
        ServerHelper.hosts.forEach {
            it.let { host ->
                val dbUtils = CouchDBUtils.checkDbUtils(host)
                repeat(cnt) {
                    dbUtils.filterDocs("fake$it", query).use { res ->
                        val result = res.body?.string()
                        logger.info("$host fake$it $result")
                        val data: String = ServerHelper.gson
                            .fromJson(result, FilterDocs::class.java)?.docs?.get(0)
                            ?.data.toString()
                        val n = Node(query, data)
                        l.add(n)
                    }
                }
            }
        }
        return l
    }


}



suspend fun create() {
    Faken.createFakeDb(FakeConfig.count)
    Faken.createFakeIndex(FakeConfig.count)
    Faken.addFakeData(FakeConfig.count, FakeConfig.rate, FakeConfig.conspired)
}

suspend fun delete() {
    Faken.deleteFakeDb(FakeConfig.count)
}

suspend fun query() {
    Faken.fakeComplexQuery(FakeConfig.count)
}


fun main() = runBlocking {
    delete()
    create()
    query()
}