package cn.edu.ruc.hola.service.extension

import cn.edu.ruc.hola.config.Host
import cn.edu.ruc.hola.utils.JsonUtils

/**
 * class mapping sid to the spec service
 */
object ServiceHolder {
    fun getHost(sid: String): Host {
        return JsonUtils.host(sid)
    }
}