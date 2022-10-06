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
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse
import java.util.concurrent.Future

/**
 * extension for couch db , for add a default
 * if you are watch this ,it's better for u to make a file like this
 * <p>
 * for further optimize !
 */

const val OKHTTP = 1

const val HTTPCLIENT = 2

// default choose the Http-client5
const val CHOOSE = HTTPCLIENT

class CouchDbExtension(host: Host) {
    private val dbUtils: SuspendCouchDBUtils
    private val asyncDbUtils: AsyncCouchDBUtils
    private val dbUtilsClient: CouchDbUtils

    init {
        dbUtils = SuspendCouchDBUtils.checkDbUtils(host)
        asyncDbUtils = AsyncCouchDBUtils.checkDbUtils(host)
        dbUtilsClient = CouchDbUtils.checkDbUtils(host)
    }

    /**
     * can be optimized in the future version
     *
     * sync query
     */
    @Core
    suspend fun query(condition: String, db_name: String): Node? {
        var node: Node?
        if (CHOOSE == HTTPCLIENT) {
            node = ResponseUtils.toNode(
                condition, dbUtilsClient.filterDocs(db_name, condition)
            )
        } else {
            dbUtils.filterDocs(db_name, condition).use { res ->
                node = ResponseUtils.toNode(condition, res)
            }
        }
        return node
    }

    /**
     * okhttp only
     */
    @Core
    fun queryAsync(condition: String, db_name: String): OkHttpResponseFuture {
        val responseFuture = OkHttpResponseFuture(condition)
        asyncDbUtils.filterDocs(db_name, condition, responseFuture)
        return responseFuture
    }

    /**
     * http-client 5 only
     */
    @Core
    fun queryAsyncClient(condition: String, db_name: String): Future<SimpleHttpResponse> {
        return dbUtilsClient.filterDocAsync(db_name, condition)
    }

    @PerformanceOnly
    suspend fun performance(condition: String, db_name: String) {
        dbUtils.filterDocs(db_name, condition).use { res ->
            println("performance $db_name !!!")
        }
    }

}