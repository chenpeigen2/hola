package cn.edu.ruc.hola.query

import cn.edu.ruc.hola.config.ServerNode
import cn.edu.ruc.hola.entity.Node

interface Queryable {
    fun query(condition: String, serverNodes: List<ServerNode>, db_name: String): List<Node?>?

    fun queries(condition: String, serverNodes: List<ServerNode>, db_names: List<String>): List<Node?>?
}