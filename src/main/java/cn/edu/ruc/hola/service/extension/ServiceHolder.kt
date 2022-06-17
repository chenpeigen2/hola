package cn.edu.ruc.hola.service.extension

import cn.edu.ruc.hola.config.Host
import cn.edu.ruc.hola.config.ServiceIdHelper

/**
 * class mapping sid to the spec service
 */
object ServiceHolder {
    fun getHost(sid: String): Host {
        return ServiceIdHelper.host(sid)!!
    }
}