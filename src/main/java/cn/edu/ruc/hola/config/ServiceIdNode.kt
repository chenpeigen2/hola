package cn.edu.ruc.hola.config

data class ServiceIdNode(val sid: String, val databaseName: String) {
    val host: Host?
        get() {
            if (_h != null) return _h;
            val hosts = ServiceIdHelper.hosts
            for (h in hosts) {
                if (sid == h.sid) {
                    _h = h
                    return h
                }
            }
            return null
        }

    private var _h: Host? = null
}

