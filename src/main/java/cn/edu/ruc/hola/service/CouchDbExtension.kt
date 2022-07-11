package cn.edu.ruc.hola.service

import cn.edu.ruc.hola.config.Host
import cn.edu.ruc.hola.config.ServerHelper
import cn.edu.ruc.hola.entity.Node
import cn.edu.ruc.hola.models.couchdb.FilterDocs
import cn.edu.ruc.hola.utils.CouchDBUtils

/**
 * extension for couch db , for add a default
 * if you are watch this ,it's better for u to make a file like this
 * <p>
 * for further optimize !
 */
class CouchDbExtension(host: Host) {
    private val dbUtils: CouchDBUtils

    init {
        dbUtils = CouchDBUtils.checkDbUtils(host)
    }

    private val gson = ServerHelper.gson

    fun query(condition: String, db_name: String): Node? {
        var node: Node? = null
        dbUtils.filterDocs(db_name, condition).use { res ->
            res.body?.let {
                val body = it.string()
                val filterDocs: FilterDocs =
                    gson.fromJson(
                        body,
                        FilterDocs::class.java
                    )
                val doc = filterDocs.docs?.get(0)
                val data = doc?.data
                node = Node(condition, data.toString())
            }
        }
        return node
    }

}