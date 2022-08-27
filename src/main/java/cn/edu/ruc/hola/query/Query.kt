package cn.edu.ruc.hola.query

import cn.edu.ruc.hola.config.Config
import cn.edu.ruc.hola.config.ServerNode
import cn.edu.ruc.hola.entity.Node
import cn.edu.ruc.hola.service.Service
import cn.edu.ruc.hola.service.ServiceHelper.createService
import cn.edu.ruc.hola.service.ServiceType
import cn.edu.ruc.hola.utils.channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.ConcurrentHashMap

class Query(val config: Config) : Serviceable, Queryable {
    /**
     * the service
     */
    companion object {
        val service = ConcurrentHashMap<String, Service>()
    }

    override fun query(condition: String, serverNodes: List<ServerNode>): List<Node> {
        val query: MutableList<Node> = mutableListOf()
        executeAsync(query, condition, serverNodes)
        return query
    }

    /**
     * execute multi with kotlin-Coroutine
     */
    private fun executeAsync(query: MutableList<Node>, condition: String, serverNodes: List<ServerNode>) = runBlocking {
        serverNodes.forEach {
            launch {
                val anService = loadService(it.sid)
                anService.query(condition, it.databaseName)
            }
        }
        for (node in channel) {
            node.let {
                query.add(it)
            }
        }
    }

    fun performance(condition: String, serverNodes: List<ServerNode>) = runBlocking {
        serverNodes.forEach {
            launch {
                val anService = loadService(it.sid)
                anService.performance(condition, it.databaseName)
            }
        }
    }

    /**
     * load the service we are calling
     */
    private fun loadService(sid: String): Service {
        return service[sid] ?: run {
            val anService = createService(sid)
            service[sid] = anService
            anService
        }
    }

    /**
     * create the given service according to the service id
     *
     * @param sid service id
     * @return the service
     */
    override fun createService(sid: String): Service {
        return createService(sid, ServiceType.TYPE_COUCHDB)
    }
}