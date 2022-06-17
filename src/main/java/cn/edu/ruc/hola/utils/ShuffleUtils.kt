package cn.edu.ruc.hola.utils

import cn.edu.ruc.hola.config.ServerNode

object ShuffleUtils {
    fun randomChoose(nodes: List<ServerNode>, cnt: Int): List<ServerNode> {
        return nodes.shuffled().subList(0, cnt)
    }

}