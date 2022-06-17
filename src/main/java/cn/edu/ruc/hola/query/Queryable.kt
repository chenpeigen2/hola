package cn.edu.ruc.hola.query

import cn.edu.ruc.hola.config.ServerNode
import cn.edu.ruc.hola.entity.Node

/**
 * we treat every
 */
interface Queryable {
    fun query(condition: String, serverNodes: List<ServerNode>): List<Node?>?
}