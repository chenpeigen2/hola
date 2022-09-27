package cn.edu.ruc.hola.service

import cn.edu.ruc.hola.config.Host
import cn.edu.ruc.hola.config.ServerHelper
import cn.edu.ruc.hola.entity.Node
import cn.edu.ruc.hola.models.couchdb.FilterDocs
import cn.edu.ruc.hola.utils.SuspendCouchDBUtils
import cn.edu.ruc.hola.utils.anno.Core
import cn.edu.ruc.hola.utils.anno.PerformanceOnly

/**
 * extension for couch db , for add a default
 * if you are watch this ,it's better for u to make a file like this
 * <p>
 * for further optimize !
 */
class CouchDbExtension(host: Host) {
    private val dbUtils: SuspendCouchDBUtils

    init {
        dbUtils = SuspendCouchDBUtils.checkDbUtils(host)
    }

    private val gson = ServerHelper.gson

    /**
     * can be optimized in the future version
     */
    @Core
    suspend fun query(condition: String, db_name: String): Node? {
        var node: Node? = null
        dbUtils.filterDocs(db_name, condition).use { res ->
            res.body?.let {
                val filterDocs: FilterDocs =
                    gson.fromJson(
                        it.string(),
                        FilterDocs::class.java
                    )

                val data = buildString {
                    filterDocs.docs?.forEach { doc ->
                        append(doc.data.toString())
                    }
                }

                node = Node(condition, data)
            }
        }
        return node
    }

    @PerformanceOnly
    suspend fun performance(condition: String, db_name: String) {
        dbUtils.filterDocs(db_name, condition).use { res ->
            println("performance $db_name !!!")
        }
    }

}