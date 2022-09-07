package cn.deu.ruc.sample

object Query {
    fun query() {
        val query = """
                   {
                "selector": {
                        "data.countryid": {"${'$'}gt": -1}
                    },
                    "execution_stats": true
                }         
        """.trimIndent()

        val dao = Dao("127.0.0.1", 5985)

        dao.filterDocs("fake0", query).use { res ->
            val result = res.body?.string()
            println(result)
        }
    }
}

fun main() {
    Query.query()
}