package cn.deu.ruc.sample

object Query {

    private val dao = Dao("127.0.0.1", 5985)

    fun query(): String {
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

fun main() {
    var ans = Query.query()
    println(ans)
}