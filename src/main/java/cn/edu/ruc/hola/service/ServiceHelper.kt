package cn.edu.ruc.hola.service

import cn.edu.ruc.hola.utils.LoggingUtils

object ServiceHelper {
    private val logger = LoggingUtils.logger()

    /**
     * create a service that a man can hold
     */
    fun createService(sid: String, type: ServiceType): Service {
        when (type) {
            ServiceType.TYPE_COUCHDB -> {
                logger.info("couchdb !")
                return CouchService(sid)
            }
            ServiceType.TYPE_MYSQL -> logger.info("mysql !")
        }
        // return default
        return CouchService(sid)
    }
}