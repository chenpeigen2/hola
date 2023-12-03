package cn.edu.ruc.hola.fake

import cn.edu.ruc.hola.config.Host
import cn.edu.ruc.hola.config.ServerHelper
import cn.edu.ruc.hola.utils.okhttp.SuspendCouchDBUtils
import kotlinx.coroutines.runBlocking
import java.util.*
import java.util.logging.Logger


private val logger = Logger.getLogger("Faken")

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


suspend fun generateInsuranceInfo(date: Date): List<Insurance> {
    val a = Insurance("a", "a", 12, 0.5f, date)
    val b = Insurance("b", "b", 13, 0.6f, date)
    val c = Insurance("c", "c", 14, 0.7f, date)
    val d = Insurance("d", "d", 15, 0.4f, date)
    val e = Insurance("e", "e", 16, 0.8f, date)
    return listOf(a, b, c, d, e)
}


suspend fun addFakeInfo(numberOfCounts: Int) {
    val props = ServerHelper.hosts
    val date = Date()
    for (idx in props.indices) {
        val host: Host = props[idx]
        val dbUtils = SuspendCouchDBUtils.checkDbUtils(host)
        val insurances = generateInsuranceInfo(date)
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