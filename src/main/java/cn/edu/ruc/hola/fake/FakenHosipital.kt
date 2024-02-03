package cn.edu.ruc.hola.fake

import cn.edu.ruc.hola.config.Host
import cn.edu.ruc.hola.config.ServerHelper
import cn.edu.ruc.hola.utils.okhttp.SuspendCouchDBUtils
import kotlinx.coroutines.runBlocking
import java.util.*
import java.util.logging.Logger

private val logger = Logger.getLogger("FakenHospital")

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

suspend fun createNamedDb(name: String) {
    ServerHelper.hosts.forEach {
        val dbUtils = SuspendCouchDBUtils.checkDbUtils(it)
        dbUtils.createDb(name).use { res ->
            val ans = res.body?.string() ?: "null"
            logger.info(ans)
        }
    }
}

//Request[url='http://localhost:8083/_find/health', filter='{"selector":{"$or":[{"health.heartRate":546},{"medicalId":"17一班"}],"uid":0,"date":"2012-12-12"}}']
//Request[url='http://localhost:8085/_find/insurance', filter='{"selector":{"$or":[{"medicalId":"17一班"}],"uid":0,"date":"2012-12-12"}}']
//Request[url='http://localhost:8084/_find/medicalCare', filter='{"selector":{"$or":[{"medicalId":"17一班"}],"uid":0,"date":"2012-12-12"}}']


val personal_hospital_record = """
    {
        "uid": "C0001",
        "date": "2023-12-12",
        "health": {
          "heartRate": 196,
          "bloodPressure": 87,
          "bloodSugar": 70,
          "bloodLipids": 25,
          "bodyTemperature": 36.9
        },
        "reason": "blood pressure",
        "medicalId": "DX00C1",
        "payed": "198"
      }
""".trimIndent()


val query_personal_hospital_record = """
    {"selector":{"${'$'}or":[{"health.heartRate":196},{"medicalId":"DX00C1"}],"uid":"C0001","date":"2023-12-12"}}
""".trimIndent()

val medical_care = """
    {
        "record_id": "DX00C1-Y56882",
        "medicalId": "DX00C1",
        "uid": "C0001",
        "medicalCare": ["care_001", "care_002"],
        "date": "2023-12-12"
      }
""".trimIndent()

val query_medical_care = """
    {"selector":{"${'$'}or":[{"medicalId":"DX00C1"}],"uid":"C0001","date":"2023-12-12"}}
""".trimIndent()

val reimbursement_records = """
    {
        "uid": "C0001",
        "record_id": "DX00C1-Y56882",
        "total_amount": 675.5,
        "revenue_total_amount" : 234.5,
        "revenue_amount": 567.6,
        "payed_amount": 123.4,
        "revenue_percent": 0.67,
        "medicalId": "DX00C1",
        "date": "2023-12-12"
      }
""".trimIndent()

val query_reimbursement_records = """
    {"selector":{"${'$'}or":[{"medicalId":"DX00C1"}],"uid":"C0001","date":"2023-12-12"}}
""".trimIndent()

suspend fun generateSingleFakeData() {
    val props = ServerHelper.hosts

    for (idx in props.indices) {
        val host: Host = props[idx]
        val dbUtils = SuspendCouchDBUtils.checkDbUtils(host)


        dbUtils.addDoc("personal_hospital_record", personal_hospital_record).use { res ->
            val bos = res.body?.string()
            logger.info("$host")
        }
        dbUtils.addDoc("medical_care", medical_care).use { res ->
            val bos = res.body?.string()
            logger.info("$host")
        }

        dbUtils.addDoc("reimbursement_records", reimbursement_records).use { res ->
            val bos = res.body?.string()
            logger.info("$host")
        }
    }
}

suspend fun fakeComplexStudentQuery() {
    var query = query_personal_hospital_record
    ServerHelper.hosts.forEach {
        println("========================")
        it.let { host ->
            val dbUtils = SuspendCouchDBUtils.checkDbUtils(host)
            dbUtils.filterDocs("personal_hospital_record", query).use { res ->
                val result = res.body?.string()
                println(result)
                logger.info("$host fake$it $result")
            }
        }
    }

    query = query_medical_care
    ServerHelper.hosts.forEach {
        it.let { host ->
            val dbUtils = SuspendCouchDBUtils.checkDbUtils(host)
            dbUtils.filterDocs("medical_care", query).use { res ->
                val result = res.body?.string()
                println(result)
                logger.info("$host fake$it $result")
            }
        }
    }

    query = query_reimbursement_records
    ServerHelper.hosts.forEach {
        it.let { host ->
            val dbUtils = SuspendCouchDBUtils.checkDbUtils(host)
            dbUtils.filterDocs("reimbursement_records", query).use { res ->
                val result = res.body?.string()
                println(result)
                logger.info("$host fake$it $result")
            }
        }
    }
}

fun main() = runBlocking {
    Faken.deleteNamedFakeDb("personal_hospital_record")
    Faken.deleteNamedFakeDb("medical_care")
    Faken.deleteNamedFakeDb("reimbursement_records")

    Faken.createNamedDb("personal_hospital_record")
    Faken.createNamedDb("medical_care")
    Faken.createNamedDb("reimbursement_records")
    generateSingleFakeData()
    fakeComplexStudentQuery()

}