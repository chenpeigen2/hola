package cn.edu.ruc.hola.query

import cn.edu.ruc.hola.service.Service

interface Serviceable {
    fun createService(sid: String?): Service?
}