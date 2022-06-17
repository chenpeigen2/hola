package cn.edu.ruc.hola.query

import cn.edu.ruc.hola.config.Config
import cn.edu.ruc.hola.config.ServerNode
import cn.edu.ruc.hola.entity.Node
import cn.edu.ruc.hola.service.AbsService
import cn.edu.ruc.hola.service.Service
import cn.edu.ruc.hola.service.ServiceHelper.createService
import cn.edu.ruc.hola.service.ServiceType
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.function.Consumer
import java.util.logging.Logger

class Query(val config: Config) : Serviceable, Queryable {
    private val logger = Logger.getLogger("Query")

    /**
     * the service
     */
    companion object {
        val service: ThreadLocal<Service> = ThreadLocal()
    }

    /**
     * temporary define the thread counts
     */
    private val nThreads = 12

    override fun query(condition: String, serverNodes: List<ServerNode>): List<Node> {
        val query: MutableList<Node> = CopyOnWriteArrayList()
        executeAsync(query, condition, serverNodes)
        return query
    }

    private fun executeAsync(query: MutableList<Node>, condition: String, serverNodes: List<ServerNode>) {
        val pool = Executors.newFixedThreadPool(nThreads)
        val latch = CountDownLatch(serverNodes.size)
        serverNodes.forEach(Consumer { (sid, databaseName): ServerNode ->
            pool.submit {
                loadService(sid)
                val node = service.get().query(condition, databaseName)
                node?.let {
                    logger.info(node.toString())
                    query.add(node)
                }
                latch.countDown()
            }
        })
        latch.await()
        if (!pool.isShutdown) pool.shutdown()
    }

    /**
     * load the service we are calling
     */
    @Synchronized
    fun loadService(sid: String) {
        if (!isNull() && checkSid(sid)) return
        service.set(createService(sid))
    }

    /**
     * @return true if the service ,false service is not null
     */
    @Synchronized
    private fun isNull(): Boolean {
        return service.get() == null
    }

    /**
     * @param sid service id
     * @return if thread hold the same return true,else false
     */
    private fun checkSid(sid: String): Boolean {
        if (service.get() == null) return false
        val s = service.get() as AbsService
        return s.sid == sid
    }

    /**
     * create the given service according to the service id
     *
     * @param sid service id
     * @return the service
     */
    override fun createService(sid: String): Service? {
        return createService(sid, ServiceType.TYPE_COUCHDB)
    }
}