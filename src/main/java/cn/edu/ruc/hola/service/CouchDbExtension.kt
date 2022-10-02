package cn.edu.ruc.hola.service

import cn.edu.ruc.hola.config.Host
import cn.edu.ruc.hola.entity.Node
import cn.edu.ruc.hola.utils.AsyncCouchDBUtils
import cn.edu.ruc.hola.utils.OkHttpResponseFuture
import cn.edu.ruc.hola.utils.ResponseUtils
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
    private val asyncDbUtils: AsyncCouchDBUtils

    init {
        dbUtils = SuspendCouchDBUtils.checkDbUtils(host)
        asyncDbUtils = AsyncCouchDBUtils.checkDbUtils(host)
    }

    /**
     * can be optimized in the future version
     */
    @Core
    suspend fun query(condition: String, db_name: String): Node? {
        var node: Node?
        dbUtils.filterDocs(db_name, condition).use { res ->
            node = ResponseUtils.toNode(condition, res)
        }
        return node
    }

    @Core
    fun queryAsync(condition: String, db_name: String): OkHttpResponseFuture {
        val responseFuture = OkHttpResponseFuture(condition)
        asyncDbUtils.filterDocs(db_name, condition, responseFuture)
        return responseFuture
    }

    @PerformanceOnly
    suspend fun performance(condition: String, db_name: String) {
        dbUtils.filterDocs(db_name, condition).use { res ->
            println("performance $db_name !!!")
        }
    }

}