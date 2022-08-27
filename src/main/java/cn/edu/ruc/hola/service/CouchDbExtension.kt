package cn.edu.ruc.hola.service

import cn.edu.ruc.hola.config.Host
import cn.edu.ruc.hola.config.ServerHelper
import cn.edu.ruc.hola.entity.Node
import cn.edu.ruc.hola.models.couchdb.FilterDocs
import cn.edu.ruc.hola.utils.CouchDBUtils
import cn.edu.ruc.hola.utils.OkHttpCallBack
import cn.edu.ruc.hola.utils.OkHttpCallBackImpl
import kotlin.math.log

/**
 * extension for couch db , for add a default
 * if you are watch this ,it's better for u to make a file like this
 * <p>
 * for further optimize !
 */
class CouchDbExtension(host: Host) {
    private val dbUtils: CouchDBUtils

    private val callback = OkHttpCallBackImpl()

    init {
        dbUtils = CouchDBUtils.checkDbUtils(host)
        dbUtils.callback = callback
    }

    private val gson = ServerHelper.gson

    /**
     * can be optimized in the future version
     */
    suspend fun query(condition: String, db_name: String): Node? {
        dbUtils.filterDocs(db_name, condition)
        callback.condition = condition
        println(callback.node)
        return callback.node
    }

    suspend fun performance(condition: String, db_name: String) {
        dbUtils.filterDocs(db_name, condition)
    }

}