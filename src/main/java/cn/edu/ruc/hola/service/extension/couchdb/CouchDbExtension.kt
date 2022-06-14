package cn.edu.ruc.hola.service.extension.couchdb

import cn.edu.ruc.hola.config.model.Host
import cn.edu.ruc.hola.entity.Node
import cn.edu.ruc.hola.service.extension.couchdb.models.FilterDocs
import cn.edu.ruc.hola.utils.DbUtils
import cn.edu.ruc.hola.utils.JsonUtils
import java.io.IOException
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.function.Consumer
import java.util.logging.Logger

/**
 * extension for couch db , for add a default
 * if you are watch this ,it's better for u to make a file like this
 * <p>
 * for further optimize !
 */
class CouchDbExtension(host: Host) {
    private var dbUtils: DbUtils? = null

    init {
        dbUtils = DbUtils.checkDbUtils(host)
    }

    private val logger = Logger.getLogger("CouchDbExtension")

    private val gson = JsonUtils.gson

    private val POOL_SIZE = 12


    fun query(condition: String, db_name: String?): Node? {
        var node: Node? = null
        dbUtils!!.filterDocs(db_name, condition).use { res ->
            res.body?.let {
                println(res)
                node = try {
                    val body = it.string()
                    val filterDocs: FilterDocs =
                        gson.fromJson(
                            body,
                            FilterDocs::class.java
                        )
                    val doc = filterDocs.docs[0]
                    val data = doc.data
                    Node(condition, data.toString())
                } catch (e: IOException) {
                    e.printStackTrace()
                    throw RuntimeException(e)
                }
            }
        }
        return node
    }

    fun queries(condition: String, db_names: List<String?>): List<Node?> {
        val pool = Executors.newFixedThreadPool(POOL_SIZE)
        val latch = CountDownLatch(db_names.size)
        val queriesNode: MutableList<Node?> = CopyOnWriteArrayList()
        db_names.forEach(Consumer { db_name: String? ->
            val n = query(condition, db_name)
            queriesNode.add(n)
            latch.countDown()
        })
        try {
            latch.await()
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }
        if (!pool.isShutdown) pool.shutdown()
        return queriesNode
    }

}