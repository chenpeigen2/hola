package cn.edu.ruc.hola.exec

import cn.edu.ruc.hola.config.ServerNode

interface Reachable {
    fun serverNodes(): List<ServerNode>
}