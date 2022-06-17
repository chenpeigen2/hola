package cn.edu.ruc.hola.service

import cn.edu.ruc.hola.entity.Node
import cn.edu.ruc.hola.service.extension.couchdb.CouchDbExtension
import cn.edu.ruc.hola.service.extension.couchdb.CouchDbServiceHolder

class CouchService(sid: String) : AbsService(sid) {

    private val dbExtension: CouchDbExtension

    init {
        dbExtension = CouchDbExtension(CouchDbServiceHolder.getHost(sid))
    }

    override fun query(condition: String, db_name: String): Node? {
        return dbExtension.query(condition, db_name)
    }

    override fun queries(condition: String, db_names: List<String>): List<Node> {
        return dbExtension.queries(condition, db_names)
    }
}