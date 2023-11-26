package cn.edu.ruc.hola.fake

import cn.edu.ruc.hola.config.Host
import cn.edu.ruc.hola.config.ServerHelper
import cn.edu.ruc.hola.entity.Node
import cn.edu.ruc.hola.fake.Faken.addFakeStuData
import cn.edu.ruc.hola.fake.Faken.createNamedDb
import cn.edu.ruc.hola.fake.Faken.createNamedFakeIndex
import cn.edu.ruc.hola.fake.Faken.deleteNamedFakeDb
import cn.edu.ruc.hola.fake.Faken.fakeComplexStudentQuery
import cn.edu.ruc.hola.utils.okhttp.SuspendCouchDBUtils
import kotlinx.coroutines.runBlocking
import java.util.*
import java.util.logging.Logger

object Faken {

    private val logger = Logger.getLogger("Faken")

    /**
     * make cnt to repeat create
     */
    private suspend fun createFakeDb(host: Host, cnt: Int) {
        val dbUtils = SuspendCouchDBUtils.checkDbUtils(host)
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

    suspend fun createNamedDb(name: String) {
        ServerHelper.hosts.forEach {
            val dbUtils = SuspendCouchDBUtils.checkDbUtils(it)
            dbUtils.createDb(name).use { res ->
                val ans = res.body?.string() ?: "null"
                logger.info(ans)
            }
        }
    }

    suspend fun createNamedFakeIndex(dbName: String, indexInfo: String) {

        ServerHelper.hosts.forEach {
            it.let { host ->
                val dbUtils = SuspendCouchDBUtils.checkDbUtils(host)
                dbUtils.createIndex(dbName, indexInfo).use { res ->
                    val bos = res.body?.string()
                    logger.info("$host")
                    logger.info(bos)
                }

            }
        }
    }

    /**
     * make cnt to repeat delete
     */
    private suspend fun deleteFakeDb(host: Host, cnt: Int) {
        val dbUtils = SuspendCouchDBUtils.checkDbUtils(host)
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

    suspend fun deleteNamedFakeDb(name: String) {
        ServerHelper.hosts.forEach {
            val dbUtils = SuspendCouchDBUtils.checkDbUtils(it)
            dbUtils.deleteDb(name).use { res ->
                logger.info("host at : $it")
                val ans = res.body?.string() ?: "null"
                logger.info(ans)
            }
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
            val dbUtils = SuspendCouchDBUtils.checkDbUtils(host)
            repeat(cnt) {
                val data = fakeData[idx * cnt + it]
                dbUtils.addDoc("fake$it", data).use { res ->
                    val bos = res.body?.string()
                    logger.info("$host")
                    logger.info("777777777777777777777")
                    logger.info("data : $data")
                    logger.info("888888888888888888888")
                    logger.info(bos)
                }
            }
        }
    }

    /**
     *
     */
    suspend fun addFakeStuData(cnt: Int, numberOfStudents: Int, classId: String, socialId: String) {
        val props = ServerHelper.hosts

        for (idx in props.indices) {
            val host: Host = props[idx]
            val dbUtils = SuspendCouchDBUtils.checkDbUtils(host)
            repeat(cnt) {
                repeat(numberOfStudents) { stuIdx ->
                    val gender = if (Random().nextBoolean()) 1 else 0
                    val uid = UUID.randomUUID().toString();
                    val nickName = NameRandom.getChineseName(gender)
                    val data = FakeData.fakeStuData(classId, gender, uid, nickName)
                    val dataSocial = FakeData.fakeSocialData(socialId, gender, uid, nickName)
                    dbUtils.addDoc("fake$it", data).use { res ->
                        val bos = res.body?.string()
//                        logger.info("$host")
                    }
                    dbUtils.addDoc("fake$it", dataSocial).use { res ->
                        val bos = res.body?.string()
                        logger.info("$host")
                    }
                }
            }
        }
    }

    suspend fun addFakeStuData(numberOfStudents: Int, classId: String, socialId: String) {
        val props = ServerHelper.hosts

        for (idx in props.indices) {
            val host: Host = props[idx]
            val dbUtils = SuspendCouchDBUtils.checkDbUtils(host)
            repeat(numberOfStudents) { stuIdx ->
                val gender = if (Random().nextBoolean()) 1 else 0
                val uid = UUID.randomUUID().toString();
                val nickName = NameRandom.getChineseName(gender)
                val data = FakeData.fakeStuData(classId, gender, uid, nickName)
                val dataSocial = FakeData.fakeSocialData(socialId, gender, uid, nickName)



                dbUtils.addDoc("class", data).use { res ->
                    val bos = res.body?.string()
                    logger.info("$host")
                }
                dbUtils.addDoc("social", dataSocial).use { res ->
                    val bos = res.body?.string()
                    logger.info("$host")
                }


            }
        }
    }

    suspend fun createFakeIndex(cnt: Int) {
        var info: String = """
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
                val dbUtils = SuspendCouchDBUtils.checkDbUtils(host)
                repeat(cnt) { cnt ->
                    dbUtils.createIndex("fake$cnt", info).use { res ->
                        val bos = res.body?.string()
                        logger.info("$host")
                        logger.info(bos)
                    }
                }
            }
        }

        info = """
                 {
                       "index": {
                            "fields": ["message"]
                        },
                        "name" : "message-index",
                        "type" : "json"
                    }
        """.trimIndent()

        ServerHelper.hosts.forEach {
            it.let { host ->
                val dbUtils = SuspendCouchDBUtils.checkDbUtils(host)
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
        val query = Action.query.trimIndent()
        val l = mutableListOf<Node>()
        ServerHelper.hosts.forEach {
            it.let { host ->
                val dbUtils = SuspendCouchDBUtils.checkDbUtils(host)
                repeat(cnt) {
                    dbUtils.filterDocs("fake$it", query).use { res ->
                        val result = res.body?.string()
                        logger.info("$host fake$it $result")
//                        val data: String = ServerHelper.gson
//                            .fromJson(result, FilterDocs::class.java)?.docs?.get(0)
//                            ?.data.toString()
//                        val n = Node(query, data)
//                        l.add(n)
                    }
                }
            }
        }
        return l
    }

    suspend fun fakeComplexStudentQuery(cnt: Int) {
        var query = Action.querySocial.trimIndent()
        ServerHelper.hosts.forEach {
            it.let { host ->
                val dbUtils = SuspendCouchDBUtils.checkDbUtils(host)
                repeat(cnt) {
                    dbUtils.filterDocs("fake$it", query).use { res ->
                        val result = res.body?.string()
                        println(result)
//                        logger.info("$host fake$it $result")
                    }
                }
            }
        }

        query = Action.queryStu.trimIndent()
        ServerHelper.hosts.forEach {
            it.let { host ->
                val dbUtils = SuspendCouchDBUtils.checkDbUtils(host)
                repeat(cnt) {
                    dbUtils.filterDocs("fake$it", query).use { res ->
                        val result = res.body?.string()
                        println(result)
//                        logger.info("$host fake$it $result")
                    }
                }
            }
        }
    }


    suspend fun fakeComplexStudentQuery() {
        var query = Action.querySocial.trimIndent()
        ServerHelper.hosts.forEach {
            println("========================")
            it.let { host ->
                val dbUtils = SuspendCouchDBUtils.checkDbUtils(host)
                dbUtils.filterDocs("social", query).use { res ->
                    val result = res.body?.string()
                    println(result)
//                        logger.info("$host fake$it $result")
                }
            }
        }

        query = Action.queryStu.trimIndent()
        ServerHelper.hosts.forEach {
            it.let { host ->
                val dbUtils = SuspendCouchDBUtils.checkDbUtils(host)
                dbUtils.filterDocs("class", query).use { res ->
                    val result = res.body?.string()
                    println(result)
//                        logger.info("$host fake$it $result")
                }
            }
        }
    }

}


suspend fun create() {
    Faken.createFakeDb(FakeConfig.count)
//    Faken.createFakeIndex(FakeConfig.count)
//    Faken.addFakeData(FakeConfig.count, FakeConfig.rate, FakeConfig.conspired)
    Faken.addFakeStuData(FakeConfig.count, 20, "17一班", "乒乓球")
    Faken.addFakeStuData(FakeConfig.count, 20, "17二班", "羽毛球")
    Faken.addFakeStuData(FakeConfig.count, 20, "17三班", "篮球")
}

suspend fun delete() {
    Faken.deleteFakeDb(FakeConfig.count)
}

suspend fun query() {
//    Faken.fakeComplexQuery(FakeConfig.count)
    Faken.fakeComplexStudentQuery(FakeConfig.count)
}


fun main() = runBlocking {

    val classInfo: String = """
                        {
                        "index": {
                            "fields": ["gender", "class_id"]
                        },
                        "type" : "json"
                    }
            
        """.trimIndent()


    val socialInfo: String = """
                        {
                        "index": {
                            "fields": ["gender", "social_id"]
                        },
                        "type" : "json"
                    }
            
        """.trimIndent()


    deleteNamedFakeDb("class")
    deleteNamedFakeDb("social")
    createNamedDb("class")
    createNamedDb("social")
    createNamedFakeIndex("class", classInfo)
    createNamedFakeIndex("social", socialInfo)
    addFakeStuData(1, "17一班", "乒乓球")
    addFakeStuData(1, "17二班", "羽毛球")
    addFakeStuData(1, "17三班", "篮球")
    addFakeStuData(1, "17四班", "足球")
    fakeComplexStudentQuery()
}