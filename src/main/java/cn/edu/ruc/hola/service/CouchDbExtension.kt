package cn.edu.ruc.hola.service

import cn.edu.ruc.hola.config.Host
import cn.edu.ruc.hola.entity.Node
import cn.edu.ruc.hola.utils.okhttp.AsyncCouchDBUtils
import cn.edu.ruc.hola.utils.okhttp.OkHttpResponseFuture
import cn.edu.ruc.hola.utils.okhttp.ResponseUtils
import cn.edu.ruc.hola.utils.okhttp.SuspendCouchDBUtils
import cn.edu.ruc.hola.utils.anno.Core
import cn.edu.ruc.hola.utils.anno.PerformanceOnly
import cn.edu.ruc.hola.utils.httpcient.CouchDbUtils

/**
 * extension for couch db , for add a default
 * if you are watch this ,it's better for u to make a file like this
 * <p>
 * for further optimize !
 */
class CouchDbExtension(host: Host) {
    private val dbUtils: SuspendCouchDBUtils
    private val asyncDbUtils: AsyncCouchDBUtils
//    private val dbUtilsClient: CouchDbUtils

    init {
        dbUtils = SuspendCouchDBUtils.checkDbUtils(host)
        asyncDbUtils = AsyncCouchDBUtils.checkDbUtils(host)
//        dbUtilsClient = CouchDbUtils.checkDbUtils(host)
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

    @Core
    fun queryWithClient(condition: String, db_name: String) {
//        val responseFuture = dbUtilsClient.filterDocs(db_name, condition)
    }

    @PerformanceOnly
    suspend fun performance(condition: String, db_name: String) {
        dbUtils.filterDocs(db_name, condition).use { res ->
            println("performance $db_name !!!")
        }
    }

}