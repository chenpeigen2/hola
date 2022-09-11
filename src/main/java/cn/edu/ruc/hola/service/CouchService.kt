package cn.edu.ruc.hola.service

import cn.edu.ruc.hola.entity.Node
import cn.edu.ruc.hola.utils.anno.PerformanceOnly
import cn.edu.ruc.hola.utils.tools.QuerySq

class CouchService(sid: String) : AbsService(sid) {

    private val dbExtension: CouchDbExtension

    init {
        val h = getHost(sid) ?: throw RuntimeException("host is null")
        dbExtension = CouchDbExtension(h)
    }

    override suspend fun query(condition: String, db_name: String): Node? {
        return dbExtension.query(condition, db_name)
    }

    @PerformanceOnly
    override suspend fun performance(condition: String, db_name: String) {
        val str = QuerySq.query()
        // here
        println(str)
    }
}