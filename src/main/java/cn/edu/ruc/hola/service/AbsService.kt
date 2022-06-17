package cn.edu.ruc.hola.service

import cn.edu.ruc.hola.config.Host
import cn.edu.ruc.hola.config.ServerHelper

abstract class AbsService(val sid: String) : Service {
    companion object {
        fun getHost(sid: String): Host? {
            return ServerHelper.host(sid)
        }
    }
}