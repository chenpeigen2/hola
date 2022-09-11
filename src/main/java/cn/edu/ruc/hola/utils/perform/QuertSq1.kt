package cn.edu.ruc.hola.utils.perform

import cn.edu.ruc.hola.utils.CouchDBUtils
import cn.edu.ruc.hola.utils.anno.PerformanceOnly

@PerformanceOnly
object QuertSq1 {
    private val dao = CouchDBUtils("127.0.0.1", 5985)

    suspend fun query(): String {
        val query = """
                   {
                "selector": {
                        "data.countryid": {"${'$'}gt": -1}
                    },
                    "execution_stats": true
                }         
        """.trimIndent()

        dao.filterDocs("fake0", query).use { res ->
            val result = res.body?.string()
            return result!!
        }
    }
}