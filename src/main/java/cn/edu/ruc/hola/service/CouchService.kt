package cn.edu.ruc.hola.service

import cn.edu.ruc.hola.entity.Node
import cn.edu.ruc.hola.service.extension.couchdb.CouchDbExtension

class CouchService(sid: String) : AbsService(sid) {

    private val dbExtension: CouchDbExtension

    init {
        val h = getHost(sid) ?: throw RuntimeException("host is null")
        dbExtension = CouchDbExtension(h)
    }

    override fun query(condition: String, db_name: String): Node? {
        return dbExtension.query(condition, db_name)
    }

    override fun queries(condition: String, db_names: List<String>): List<Node> {
        return dbExtension.queries(condition, db_names)
    }
}