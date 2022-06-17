package cn.edu.ruc.hola.service

object ServiceHelper {
    /**
     * create a service that a man can hold
     */
    fun createService(sid: String, type: ServiceType): Service? {
        when (type) {
            ServiceType.TYPE_COUCHDB -> {
                return CouchService(sid)
            }
            ServiceType.TYPE_MYSQL -> println("TYPE_MYSQL")
        }
        return null
    }
}