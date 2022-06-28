package cn.edu.ruc.hola.service

import cn.edu.ruc.hola.entity.Node

class CouchService(sid: String) : AbsService(sid) {

    private val dbExtension: CouchDbExtension

    init {
        val h = getHost(sid) ?: throw RuntimeException("host is null")
        dbExtension = CouchDbExtension(h)
    }

    override fun query(condition: String, db_name: String): Node? {
        return dbExtension.query(condition, db_name)
    }
}